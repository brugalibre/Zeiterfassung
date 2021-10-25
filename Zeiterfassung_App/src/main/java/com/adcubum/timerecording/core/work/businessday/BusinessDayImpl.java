/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.coolguys.exception.InvalidChargeTypeRepresentationException;
import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayIncrementImport;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.compare.BusinessDayIncrementComparator;
import com.adcubum.timerecording.core.work.businessday.compare.TimeStampComparator;
import com.adcubum.timerecording.core.work.businessday.exception.BusinessIncrementBevorOthersException;
import com.adcubum.timerecording.core.work.businessday.factory.BusinessDayFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.settings.round.TimeRounder;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.timerecording.work.date.DateTimeUtil;
import com.adcubum.timerecording.work.date.TimeType;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;
import com.adcubum.util.parser.NumberFormat;

/**
 * The {@link BusinessDayImpl} defines an entire day full of work. Such a day may
 * consist of several increments, called {@link BusinessDayIncrement}. <br>
 * Each such incremental is defined by several {@link TimeSnippet}s. And each of
 * those snippet's has a begin time-stamp, an end time-stamp and a specific
 * amount of hours <br>
 * 
 * A {@link BusinessDayIncrement} has exactly two snippet's. One or more such {@link BusinessDayIncrement} are put together - to
 * a whole {@link BusinessDayImpl}
 * 
 * @author Dominic
 * 
 */
public class BusinessDayImpl implements BusinessDay {
   private UUID id;
   // all increments of this BusinessDayImpl (those are all finished!)
   private CopyOnWriteArrayList<BusinessDayIncrement> increments;
   // the current increment which has been started but not yet finished so far
   private BusinessDayIncrement currentBussinessDayIncremental;
   // the amount of times a user comes and goes during the day
   private ComeAndGoes comeAndGoes;
   // true if this BusinessDay is alreay booked
   private boolean isBooked;

   /**
    * Creates a new {@link BusinessDayImpl} from the {@link BusinessDayFactory}
    */
   public BusinessDayImpl(UUID id, boolean isBooked, List<BusinessDayIncrement> businessDayIncrements, BusinessDayIncrement currentBDIncrement,
         ComeAndGoes comeAndGoes) {
      this.comeAndGoes = comeAndGoes;
      this.increments = new CopyOnWriteArrayList<>(businessDayIncrements);
      this.currentBussinessDayIncremental = currentBDIncrement;
      this.isBooked = isBooked;
      this.id = id;
   }

   /**
    * Creates a new {@link BusinessDayImpl}
    */
   public BusinessDayImpl() {
      this(ComeAndGoesImpl.of());
   }

   /**
    * Creates a new {@link BusinessDayImpl}
    * Constructor only for testing purpose!
    */
   public BusinessDayImpl(ComeAndGoes comeAndGoes) {
      initialize(comeAndGoes);
   }

   private void initialize(ComeAndGoes comeAndGoes) {
      increments = new CopyOnWriteArrayList<>();
      currentBussinessDayIncremental = new BusinessDayIncrementImpl();
      this.comeAndGoes = comeAndGoes;
   }

   @Override
   public BusinessDay resumeLastIncremental() {
      BusinessDayIncrement changedCurrentBusinessDayIncrement = currentBussinessDayIncremental.resumeLastTimeSnippet();
      return createCopyWithCurrentBusinessDayIncrement(changedCurrentBusinessDayIncrement);
   }

   @Override
   public BusinessDay comeOrGo() {
      return createCopy(comeAndGoes.comeOrGo());
   }

   @Override
   public BusinessDay flagComeAndGoesAsRecorded() {
      return createCopy(comeAndGoes.flagComeAndGoesAsRecorded());
   }

   @Override
   public BusinessDay startNewIncremental() {
      DateTime time = DateTimeFactory.createNew(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
      BusinessDayIncrement changedCurrentBusinessDayIncrement = BusinessDayIncrementImpl.of(currentBussinessDayIncremental)
            .startCurrentTimeSnippet(time);
      return createCopyWithCurrentBusinessDayIncrement(changedCurrentBusinessDayIncrement);
   }

   /**
    * Stops the current incremental and add the
    * {@link #currentBussinessDayIncremental} to the list with increments. After
    * that, a new incremental is created
    */
   @Override
   public BusinessDay stopCurrentIncremental() {
      DateTime endTimeStamp = DateTimeFactory.createNew(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
      BusinessDayIncrement changedCurrentBussinessDayIncremental = currentBussinessDayIncremental.stopCurrentTimeSnippet(endTimeStamp);
      return createCopyWithCurrentBusinessDayIncrement(changedCurrentBussinessDayIncremental);
   }

   @Override
   public BusinessDay flagBusinessDayAsCharged() {
      List<BusinessDayIncrement> changedBusinessDayIncrements = increments.stream()
            .map(BusinessDayIncrement::flagAsBooked)
            .collect(Collectors.toList());
      return createCopy(changedBusinessDayIncrements);
   }

   @Override
   public BusinessDay refreshDummyTickets() {
      List<BusinessDayIncrement> changedBusinessDayIncrements = increments.stream()
            .map(BusinessDayIncrement::refreshDummyTicket)
            .collect(Collectors.toList());
      return createCopy(changedBusinessDayIncrements);
   }

   @Override
   public boolean hasNotChargedElements() {
      if (increments.isEmpty()) {
         return false;
      }
      return increments//
            .stream()//
            .anyMatch(increment -> !increment.isBooked());
   }

   @Override
   public boolean hasDescription() {
      return increments.stream()//
            .anyMatch(BusinessDayIncrement::hasDescription);
   }

   @Override
   public BusinessDay clearFinishedIncrements() {
      return createCopy(Collections.emptyList())
            .clearComeAndGoes();
   }

   @Override
   public BusinessDay clearComeAndGoes() {
      return createCopy(comeAndGoes.clearDoneComeAndGoes());
   }

   @Override
   public float getTotalDuration() {
      return getTotalDuration(TimeType.DEFAULT);
   }

   @Override
   public BusinessDayIncrement getCurrentBussinessDayIncremental() {
      return currentBussinessDayIncremental;
   }

   @Override
   public List<BusinessDayIncrement> getIncrements() {
      List<BusinessDayIncrement> incrementsCopy = new ArrayList<>(increments);
      Collections.sort(incrementsCopy, new BusinessDayIncrementComparator());
      return incrementsCopy;
   }

   @Override
   public ComeAndGoes getComeAndGoes() {
      return comeAndGoes;
   }

   @Override
   public boolean hasNotRecordedComeAndGoContent() {
      return comeAndGoes.hasNotRecordedComeAndGoContent();
   }

   @Override
   public DateTime getDateTime() {
      if (increments.isEmpty()) {
         return DateTimeFactory.createNew();
      }
      return increments.get(0).getDateTime();
   }

   @Override
   public BusinessDay removeIncrement4Id(UUID id) {
      if (getBusinessIncrement(id).isPresent()) {
         List<BusinessDayIncrement> remainingBusinessDayIncrements = collectBusinessDayIncrementsWithOtherId(id);
         return createCopy(remainingBusinessDayIncrements);
      }
      return this;
   }

   @Override
   public BusinessDay addBusinessIncrement(BusinessDayIncrementAdd update) {
      return addBusinessIncrementInternal(BusinessDayIncrementImpl.of(update));
   }

   @Override
   public BusinessDay addBusinessIncrement(BusinessDayIncrementImport businessDayIncrementImport) {
      return addBusinessIncrementInternal(BusinessDayIncrementImpl.of(businessDayIncrementImport));
   }

   private BusinessDay addBusinessIncrementInternal(BusinessDayIncrement businessDayIncrement) {
      validateIfCanAdd(businessDayIncrement);
      // recreate / reset the currentIncrement in 
      return createCopy(businessDayIncrement)
            .createNewIncremental();
   }

   private void validateIfCanAdd(BusinessDayIncrement businessDayIncrement) {
      DateTime newBDIncTime = businessDayIncrement.getDateTime();
      validateCurrentTimeSnippet(businessDayIncrement);
      increments.stream()
            .filter(newBusinessDayIncIsBeforeOrAfter(newBDIncTime))
            .findAny()
            .ifPresent(throwException());
   }

   private void validateCurrentTimeSnippet(BusinessDayIncrement businessDayIncrement) {
      TimeSnippet currentTimeSnippet = businessDayIncrement.getCurrentTimeSnippet();
      requireNonNull(currentTimeSnippet.getBeginTimeStamp(), "BeginTimeStamp must not be null");
      requireNonNull(currentTimeSnippet.getEndTimeStamp(), "EndTimeStamp must not be null");
   }

   private static Predicate<BusinessDayIncrement> newBusinessDayIncIsBeforeOrAfter(DateTime newBDIncTime) {
      return bDayInc -> DateTimeUtil.isTimeBeforeOrAfterMidnightOfGivenDate(bDayInc.getDateTime(), newBDIncTime);
   }

   private static Consumer<BusinessDayIncrement> throwException() {
      return bdIncrement -> {
         throw new BusinessIncrementBevorOthersException(
               "Can not add a BusinessDayIncrement which takes place before or after the day of this BusinessDayImpl!");
      };
   }

   @Override
   public BusinessDay changeBusinesDayIncrement(ChangedValue changeValue) {
      Optional<BusinessDayIncrement> businessDayIncOpt = getBusinessIncrement(changeValue.getId());
      return businessDayIncOpt.map(businessDayIncrement -> {
         BusinessDayIncrement changedBusinessDayIncrement = handleBusinessDayChangedInternal(businessDayIncrement, changeValue);
         return createCopy(changedBusinessDayIncrement);
      }).orElse(this);
   }

   @Override
   public BusinessDay changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      return createCopy(comeAndGoes.changeComeAndGo(changedComeAndGoValue));
   }

   @Override
   public boolean hasUnfinishedBusinessDayIncrement() {
      TimeSnippet currentTimeSnippet = currentBussinessDayIncremental.getCurrentTimeSnippet();
      return nonNull(currentTimeSnippet) && nonNull(currentTimeSnippet.getBeginTimeStamp())
            && isNull(currentTimeSnippet.getEndTimeStamp());
   }

   @Override
   public boolean hasElementsFromPrecedentDays() {
      DateTime now = DateTimeFactory.createNew();
      return increments.stream()
            .anyMatch(bDayInc -> {
               DateTime bdIncTime = DateTimeFactory.createNew(bDayInc.getDateTime());
               return DateTimeUtil.isTimeBeforeMidnightOfGivenDate(bdIncTime, now);
            });
   }

   @Override
   public boolean hasComeAndGoesFromPrecedentDays() {
      return comeAndGoes.hasComeAndGoesFromPrecedentDays();
   }

   @Override
   public String getCapturingActiveSinceMsg() {
      TimeSnippet startPoint = currentBussinessDayIncremental.getCurrentTimeSnippet();
      String time = startPoint.getDuration() > 0 ? " (" + startPoint.getDuration() + "h)" : "";
      return TextLabel.CAPTURING_ACTIVE_SINCE + " " + startPoint.getBeginTimeStamp() + time;
   }

   @Override
   public String getComeAndGoMsg() {
      TimeSnippet currentComeAndGoTimeSnippet = getCurrentComeAndGoTimeSnippet();
      return TextLabel.CAPTURING_INACTIVE + ". " + TextLabel.COME_OR_GO + ": " + currentComeAndGoTimeSnippet.getBeginTimeStampRep();
   }

   @Override
   public boolean isBooked() {
      return isBooked;
   }

   private TimeSnippet getCurrentComeAndGoTimeSnippet() {
      return comeAndGoes.getCurrentComeAndGo()
            .map(ComeAndGo::getComeAndGoTimeStamp)
            .orElseThrow(() -> new IllegalStateException("This should never happen! If we are in state 'ComeOrGo' then there must be at least one"));
   }

   @Override
   public String getCapturingInactiveSinceMsg() {
      TimeSnippet endPoint = getLastTimeSnippet();
      if (endPoint != null) {
         return TextLabel.CAPTURING_INCTIVE_SINCE + " " + endPoint.getEndTimeStamp();
      }
      return TextLabel.CAPTURING_INACTIVE;
   }

   @Override
   public UUID getId() {
      return id;
   }

   private Optional<BusinessDayIncrement> getBusinessIncrement(UUID id) {
      return increments.stream()
            .filter(businessDayIncrements -> businessDayIncrements.getId().equals(id))
            .findFirst();
   }

   /**
    * Returns the last {@link TimeSnippet} which was added to this
    * {@link BusinessDayImpl}
    */
   private TimeSnippet getLastTimeSnippet() {
      return increments.stream().map(BusinessDayIncrement::getCurrentTimeSnippet)
            .sorted(new TimeStampComparator().reversed()).findFirst().orElse(null);
   }

   private BusinessDayIncrement handleBusinessDayChangedInternal(BusinessDayIncrement businessDayIncremental,
         ChangedValue changedValue) {
      switch (changedValue.getValueTypes()) {
         case DESCRIPTION:
            businessDayIncremental = businessDayIncremental.setDescription(changedValue.getNewValue());
            break;
         case BEGIN:
            businessDayIncremental = businessDayIncremental.updateBeginTimeSnippetAndCalculate(changedValue.getNewValue());
            break;
         case END:
            businessDayIncremental = businessDayIncremental.updateEndTimeSnippetAndCalculate(changedValue.getNewValue());
            break;
         case TICKET:
            businessDayIncremental = businessDayIncremental.setTicket(changedValue.getNewTicket());
            break;
         case TICKET_NR:
            Ticket newTicket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(changedValue.getNewValue());
            businessDayIncremental = businessDayIncremental.setTicket(newTicket);
            break;
         case SERVICE_CODE:
            businessDayIncremental = businessDayIncremental.setServiceCode(Integer.parseInt(changedValue.getNewValue()));
            break;
         case SERVICE_CODE_DESCRIPTION:
            try {
               businessDayIncremental = businessDayIncremental.setServiceCode4Description(changedValue.getNewValue());
            } catch (InvalidChargeTypeRepresentationException e) {
               e.printStackTrace();
               // ignore failures
            }
            break;
         case AMOUNT_OF_TIME:
            businessDayIncremental = changeAmountOfTime4BDIncrement(businessDayIncremental, changedValue);
            break;
         default:
            throw new UnsupportedOperationException(
                  "ChargeType '" + changedValue.getValueTypes() + "' not implemented!");
      }
      return businessDayIncremental;
   }

   /*
    * The duration of the timeSnippet within the given BusinessDayIncrement is expanded so that the new total Duration of 
    * the BusinessDayIncrement matches with the desired duration
    * 
    * Therefore we first calculate the new duration of this last TimeSnipped. Then we calculate the difference between the new and current duration
    * This difference is than added to the last TimeSnippet
    */
   private static BusinessDayIncrement changeAmountOfTime4BDIncrement(BusinessDayIncrement businessDayIncremental, ChangedValue changedValue) {
      float newTotalDurationOfBDInc = NumberFormat.parseFloatOrDefault(changedValue.getNewValue(), 0);
      float currentDuration = businessDayIncremental.getTotalDuration();
      float newDuration = newTotalDurationOfBDInc - businessDayIncremental.getTotalDuration() + currentDuration;

      if (newDuration > 0) {
         businessDayIncremental = businessDayIncremental.addAdditionallyTime(newDuration);
      }
      return businessDayIncremental;
   }

   private BusinessDay createNewIncremental() {
      currentBussinessDayIncremental = BusinessDayIncrementImpl.of(currentBussinessDayIncremental, false);
      return this;
   }

   private float getTotalDuration(TIME_TYPE type) {
      float sum = 0;

      for (BusinessDayIncrement incremental : increments) {
         sum = sum + incremental.getTotalDuration(type);
      }
      return NumberFormat.parseFloat(NumberFormat.format(sum));
   }

   private BusinessDayImpl createCopy(List<BusinessDayIncrement> businessDayIncrements) {
      return new BusinessDayImpl(this.id, this.isBooked, businessDayIncrements, this.currentBussinessDayIncremental, comeAndGoes);
   }

   private BusinessDayImpl createCopy(ComeAndGoes comeAndGoes) {
      return new BusinessDayImpl(this.id, this.isBooked, this.increments, this.currentBussinessDayIncremental, comeAndGoes);
   }

   private BusinessDayImpl createCopy(BusinessDayIncrement changedBusinessDayIncrement) {
      List<BusinessDayIncrement> unchangedBDayIncrements =
            new ArrayList<>(collectBusinessDayIncrementsWithOtherId(changedBusinessDayIncrement.getId()));
      unchangedBDayIncrements.add(changedBusinessDayIncrement);
      return new BusinessDayImpl(this.id, this.isBooked, unchangedBDayIncrements, this.currentBussinessDayIncremental, this.comeAndGoes);
   }

   private BusinessDayImpl createCopyWithCurrentBusinessDayIncrement(BusinessDayIncrement changedCurrentBusinessDayIncrement) {
      return new BusinessDayImpl(this.id, this.isBooked, this.increments, changedCurrentBusinessDayIncrement, this.comeAndGoes);
   }

   private List<BusinessDayIncrement> collectBusinessDayIncrementsWithOtherId(UUID changedBDIncrementId) {
      if (isNull(changedBDIncrementId)) {
         return increments;// no id, so we can't compare
      }
      return increments.stream()
            .filter(bDayIncrement -> !changedBDIncrementId.equals(bDayIncrement.getId()))
            .collect(Collectors.toList());
   }
}
