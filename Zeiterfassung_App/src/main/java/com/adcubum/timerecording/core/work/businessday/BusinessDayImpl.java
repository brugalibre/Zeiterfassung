/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
import com.adcubum.timerecording.settings.round.TimeRounder;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.timerecording.work.date.TimeType;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;
import com.adcubum.timerecording.work.date.TimeUtil;
import com.adcubum.util.parser.NumberFormat;
import com.adcubum.util.utils.StringUtil;

/**
 * The {@link BusinessDayImpl} defines an entire day full of work. Such a day may
 * consist of several increments, called {@link BusinessDayIncrement}. <br>
 * Each such incremental is defined by several {@link TimeSnippet}s. And each of
 * those snippet's has a begin time-stamp, an end time-stamp and a specific
 * amount of hours <br>
 * 
 * A {@link BusinessDayIncrement} can have several of those snippet's -
 * scattered over the entire day. But each of this {@link BusinessDayIncrement}
 * belongs to the same <br>
 * project. One or more such {@link BusinessDayIncrement} are put together - to
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

   /**
    * Creates a new {@link BusinessDayImpl} from the {@link BusinessDayFactory}
    */
   public BusinessDayImpl(UUID id, List<BusinessDayIncrement> businessDayIncrements, BusinessDayIncrement currentBDIncrement,
         ComeAndGoes comeAndGoes) {
      this.comeAndGoes = comeAndGoes;
      this.increments = new CopyOnWriteArrayList<>(businessDayIncrements);
      this.currentBussinessDayIncremental = currentBDIncrement;
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
   public void resumeLastIncremental() {
      currentBussinessDayIncremental.resumeLastTimeSnippet();
   }

   @Override
   public void comeOrGo() {
      comeAndGoes = comeAndGoes.comeOrGo();
   }

   @Override
   public void flagComeAndGoesAsRecorded() {
      comeAndGoes = comeAndGoes.flagComeAndGoesAsRecorded();
   }

   @Override
   public void startNewIncremental() {
      Time time = TimeFactory.createNew(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
      createNewIncremental();
      currentBussinessDayIncremental.startCurrentTimeSnippet(time);
   }

   /**
    * Stops the current incremental and add the
    * {@link #currentBussinessDayIncremental} to the list with increments. After
    * that, a new incremental is created
    */
   @Override
   public void stopCurrentIncremental() {
      Time endTimeStamp = TimeFactory.createNew(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
      currentBussinessDayIncremental.stopCurrentTimeSnippet(endTimeStamp);
   }

   @Override
   public void flagBusinessDayAsCharged() {
      increments.stream()//
            .forEach(BusinessDayIncrement::flagAsCharged);
   }

   @Override
   public void refreshDummyTickets() {
      increments.stream()
            .forEach(BusinessDayIncrement::refreshDummyTicket);
   }

   @Override
   public boolean hasNotChargedElements() {
      if (increments.isEmpty()) {
         return false;
      }
      return increments//
            .stream()//
            .anyMatch(increment -> !increment.isCharged());
   }

   @Override
   public boolean hasDescription() {
      return increments.stream()//
            .anyMatch(bDayInc -> StringUtil.isNotEmptyOrNull(bDayInc.getDescription()));
   }

   @Override
   public void clearFinishedIncrements() {
      increments.clear();
      clearComeAndGoes();
   }

   @Override
   public void clearComeAndGoes() {
      comeAndGoes = comeAndGoes.clearDoneComeAndGoes();
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
   public Date getDate() {
      if (increments.isEmpty()) {
         return new Date();
      }
      return increments.get(0).getDate();
   }

   @Override
   public void removeIncrementAtIndex(int index) {
      if (index >= 0 && index < increments.size()) {
         increments.remove(index);
      }
   }

   @Override
   public void addBusinessIncrement(BusinessDayIncrementAdd update) {
      addBusinessIncrementInternal(() -> BusinessDayIncrementImpl.of(update));
   }

   @Override
   public void addBusinessIncrement(BusinessDayIncrementImport businessDayIncrementImport) {
      addBusinessIncrementInternal(() -> BusinessDayIncrementImpl.of(businessDayIncrementImport));
   }

   private void addBusinessIncrementInternal(Supplier<BusinessDayIncrement> bdIncSupplier) {
      BusinessDayIncrement businessDayIncrement = bdIncSupplier.get();
      validateIfCanAdd(businessDayIncrement);
      increments.add(businessDayIncrement);
      // recreate / reset the currentIncrement in 
      createNewIncremental();
   }

   private void validateIfCanAdd(BusinessDayIncrement businessDayIncrement) {
      Time newBDIncTime = TimeFactory.createNew(businessDayIncrement.getDate());
      increments.stream()
            .filter(newBusinessDayIncIsBeforeOrAfter(newBDIncTime))
            .findAny()
            .ifPresent(throwException());
   }

   private static Predicate<BusinessDayIncrement> newBusinessDayIncIsBeforeOrAfter(Time newBDIncTime) {
      return bDayInc -> TimeUtil.isTimeBeforeOrAfterMidnightOfGivenDate(newBDIncTime, bDayInc.getDate());
   }

   private static Consumer<BusinessDayIncrement> throwException() {
      return bdIncrement -> {
         throw new BusinessIncrementBevorOthersException(
               "Can not add a BusinessDayIncrement which takes place before or after the day of this BusinessDayImpl!");
      };
   }

   @Override
   public void changeBusinesDayIncrement(ChangedValue changeValue) {
      Optional<BusinessDayIncrement> businessDayIncOpt = getBusinessIncrement(changeValue.getSequence());
      businessDayIncOpt.ifPresent(businessDayIncrement -> handleBusinessDayChangedInternal(businessDayIncrement, changeValue));
   }

   @Override
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      comeAndGoes = comeAndGoes.changeComeAndGo(changedComeAndGoValue);
      return comeAndGoes;
   }

   @Override
   public boolean hasUnfinishedBusinessDayIncrement() {
      TimeSnippet currentTimeSnippet = currentBussinessDayIncremental.getCurrentTimeSnippet();
      return nonNull(currentTimeSnippet) && nonNull(currentTimeSnippet.getBeginTimeStamp())
            && isNull(currentTimeSnippet.getEndTimeStamp());
   }

   @Override
   public boolean hasElementsFromPrecedentDays() {
      Date now = new Date();
      return increments.stream()
            .anyMatch(bDayInc -> {
               Time bdIncTime = TimeFactory.createNew(bDayInc.getDate());
               return TimeUtil.isTimeBeforeMidnightOfGivenDate(bdIncTime, now);
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

   private TimeSnippet getCurrentComeAndGoTimeSnippet() {
      return comeAndGoes.getCurrentComeAndGo()
            .map(ComeAndGo::getComeAndGoTimeStamp)
            .orElseThrow(() -> new IllegalStateException("This should never happen! If we are in state 'ComeOrGo' then there must be at least one"));
   }

   @Override
   public String getCapturingInactiveSinceMsg() {
      TimeSnippet endPoint = getLastTimeSnippet();
      if (endPoint != null) {
         return TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INCTIVE_SINCE + " "
               + endPoint.getEndTimeStamp();
      }
      return TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE;
   }

   @Override
   public UUID getId() {
      return id;
   }

   private Optional<BusinessDayIncrement> getBusinessIncrement(int orderNr) {
      BusinessDayIncrement businessDayIncremental = null;
      for (int i = 0; i < increments.size(); i++) {
         if (orderNr == i) {
            businessDayIncremental = increments.get(i);
         }
      }
      return Optional.ofNullable(businessDayIncremental);
   }

   /**
    * Returns the last {@link TimeSnippet} which was added to this
    * {@link BusinessDayImpl}
    */
   private TimeSnippet getLastTimeSnippet() {
      return increments.stream().map(BusinessDayIncrement::getCurrentTimeSnippet)
            .sorted(new TimeStampComparator().reversed()).findFirst().orElse(null);
   }

   private void handleBusinessDayChangedInternal(BusinessDayIncrement businessDayIncremental,
         ChangedValue changedValue) {

      switch (changedValue.getValueTypes()) {
         case DESCRIPTION:
            businessDayIncremental.setDescription(changedValue.getNewValue());
            break;
         case BEGIN:
            businessDayIncremental.updateBeginTimeSnippetAndCalculate(changedValue.getNewValue());
            break;
         case END:
            businessDayIncremental.updateEndTimeSnippetAndCalculate(changedValue.getNewValue());
            break;
         case TICKET:
            businessDayIncremental.setTicket(changedValue.getNewTicket());
            break;
         case CHARGE_TYPE:
            try {
               businessDayIncremental.setChargeType(changedValue.getNewValue());
            } catch (InvalidChargeTypeRepresentationException e) {
               e.printStackTrace();
               // ignore failures
            }
            break;
         case AMOUNT_OF_TIME:
            changeAmountOfTime4BDIncrement(businessDayIncremental, changedValue);
            break;
         default:
            throw new UnsupportedOperationException(
                  "ChargeType '" + changedValue.getValueTypes() + "' not implemented!");
      }
   }

   /*
    * The duration of the timeSnippet within the given BusinessDayIncrement is expanded so that the new total Duration of 
    * the BusinessDayIncrement matches with the desired duration
    * 
    * Therefore we first calculate the new duration of this last TimeSnipped. Then we calculate the difference between the new and current duration
    * This difference is than added to the last TimeSnippet
    */
   private static void changeAmountOfTime4BDIncrement(BusinessDayIncrement businessDayIncremental, ChangedValue changedValue) {
      float newTotalDurationOfBDInc = NumberFormat.parseFloatOrDefault(changedValue.getNewValue(), 0);
      float currentDuration = businessDayIncremental.getTotalDuration();
      float newDuration = newTotalDurationOfBDInc - businessDayIncremental.getTotalDuration() + currentDuration;

      if (newDuration > 0) {
         TimeSnippet currentTimeSnippet = businessDayIncremental.getCurrentTimeSnippet();
         currentTimeSnippet.addAdditionallyTime(String.valueOf(newDuration));
      }
   }

   private void createNewIncremental() {
      currentBussinessDayIncremental = BusinessDayIncrementImpl.of(currentBussinessDayIncremental);
   }

   private float getTotalDuration(TIME_TYPE type) {
      float sum = 0;

      for (BusinessDayIncrement incremental : increments) {
         sum = sum + incremental.getTotalDuration(type);
      }
      return NumberFormat.parseFloat(NumberFormat.format(sum));
   }
}
