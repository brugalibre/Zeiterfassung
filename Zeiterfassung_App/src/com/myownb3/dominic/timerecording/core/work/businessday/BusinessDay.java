/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.core.book.coolguys.exception.InvalidChargeTypeRepresentationException;
import com.myownb3.dominic.timerecording.core.importexport.in.businessday.BusinessDayIncrementImport;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement.TimeStampComparator;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.myownb3.dominic.timerecording.core.work.date.Time;
import com.myownb3.dominic.timerecording.core.work.date.TimeType;
import com.myownb3.dominic.timerecording.core.work.date.TimeType.TIME_TYPE;
import com.myownb3.dominic.timerecording.settings.round.TimeRounder;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * The {@link BusinessDay} defines an entire day full of work. Such a day may
 * consist of several increments, called {@link BusinessDayIncrement}. <br>
 * Each such incremental is defined by several {@link TimeSnippet}s. And each of
 * those snippet's has a begin time-stamp, an end time-stamp and a specific
 * amount of hours <br>
 * 
 * A {@link BusinessDayIncrement} can have several of those snippet's -
 * scattered over the entire day. But each of this {@link BusinessDayIncrement}
 * belongs to the same <br>
 * project. One or more such {@link BusinessDayIncrement} are put together - to
 * a whole {@link BusinessDay}
 * 
 * @author Dominic
 * 
 */
public class BusinessDay {
   // all increments of this BusinessDay (those are all finished!)
   private CopyOnWriteArrayList<BusinessDayIncrement> increments;
   // the current increment which has been started but not yet finished so far
   private BusinessDayIncrement currentBussinessDayIncremental;

   /**
    * Creates a new {@link BusinessDay}
    */
   public BusinessDay() {
      initialize(new Date());
   }

   /**
    * Creates a new {@link BusinessDay} for the given {@link Date}
    * 
    * @param date
    *        the given Date
    */
   public BusinessDay(Date date) {
      initialize(date);
   }

   private void initialize(Date date) {
      increments = new CopyOnWriteArrayList<>();
      currentBussinessDayIncremental = new BusinessDayIncrement(date);
   }

   /**
    * Resumes the {@link #currentBussinessDayIncremental}
    */
   public void resumeLastIncremental() {

      currentBussinessDayIncremental.resumeLastTimeSnippet();
   }

   /**
    * Starts a new {@link BusinessDayIncrement}. That means to create a new
    * instance of {@link Time} <br>
    * and forward that to the
    */
   public void startNewIncremental() {
      Time time = new Time(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
      createNewIncremental();
      currentBussinessDayIncremental.startCurrentTimeSnippet(time);
   }

   /**
    * Stops the current incremental and add the
    * {@link #currentBussinessDayIncremental} to the list with increments. After
    * that, a new incremental is created
    */
   public void stopCurrentIncremental() {
      Time endTimeStamp = new Time(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
      currentBussinessDayIncremental.stopCurrentTimeSnippet(endTimeStamp);
   }

   public void flagBusinessDayAsCharged() {
      increments.stream()//
            .forEach(BusinessDayIncrement::flagAsCharged);
   }

   /**
    * All {@link BusinessDayIncrement}s are checked if they have a dummy-{@link Ticket} set
    * If so, this dummy-Ticket is tried to update. This can be necessary, if the jira-api was not reachable
    * during the creating of a {@link BusinessDayIncrement}
    */
   public void refreshDummyTickets() {
      increments.stream()
            .forEach(BusinessDayIncrement::refreshDummyTicket);
   }

   /**
    * Returns <code>true</code> if this {@link BusinessDay} has at least one
    * element which is not yed charged. Otherwise returns <code>false</code>
    * 
    * @return <code>true</code> if this {@link BusinessDay} has at least one
    *         element which is not yed charged. Otherwise returns
    *         <code>false</code>
    */
   public boolean hasNotChargedElements() {
      if (increments.isEmpty()) {
         return false;
      }
      return increments//
            .stream()//
            .anyMatch(increment -> !increment.isCharged());
   }

   /**
    * @return <code>true</code> if this {@link BusinessDay} has at least one
    *         element with a description. Otherwise returns <code>false</code>
    */
   public boolean hasDescription() {
      return increments.stream()//
            .anyMatch(bDayInc -> StringUtil.isNotEmptyOrNull(bDayInc.getDescription()));
   }

   /**
    * Deletes all {@link BusinessDayIncrement} which are already finished
    */
   public void clearFinishedIncrements() {
      increments.clear();
   }

   public float getTotalDuration() {
      return getTotalDuration(TimeType.DEFAULT);
   }

   public BusinessDayIncrement getCurrentBussinessDayIncremental() {
      return currentBussinessDayIncremental;
   }

   public List<BusinessDayIncrement> getIncrements() {
      return increments;
   }

   /**
    * Returns the {@link Date} of this {@link BusinessDay}. If this
    * {@link BusinessDay} has no <br>
    * {@link BusinessDayIncrement}, so the {@link #increments} is empty, a new
    * instance of {@link Date} is returned.
    * 
    * @return the {@link Date} of this BussinessDay.
    */
   public Date getDate() {
      if (increments.isEmpty()) {
         return new Date();
      }
      return increments.get(0).getDate();
   }

   /**
    * Removes the {@link BusinessDayIncrement} at the given index. If there is no
    * {@link BusinessDayIncrement} for this index nothing is done
    * 
    * @param index
    *        the given index
    */
   public void removeIncrementAtIndex(int index) {
      if (index >= 0 && index < increments.size()) {
         increments.remove(index);
      }
   }

   /**
    * Creates and adds a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementAdd}
    * 
    * @param update
    *        the {@link BusinessDayIncrementAdd} which defines the new
    *        {@link BusinessDayIncrement}
    */
   public void addBusinessIncrement(BusinessDayIncrementAdd update) {
      BusinessDayIncrement newBusinessDayInc = BusinessDayIncrement.of(update);
      increments.add(newBusinessDayInc);
   }

   /**
    * Creates and adds a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementImport}
    * 
    * @param businessDayIncrementImport
    *        the {@link BusinessDayIncrementImport}
    *        which defines the new
    *        {@link BusinessDayIncrement}
    */
   public void addBusinessIncrements(BusinessDayIncrementImport businessDayIncrementImport) {
      BusinessDayIncrement newBusinessDayInc = BusinessDayIncrement.of(businessDayIncrementImport);
      increments.add(newBusinessDayInc);
   }

   /**
    * According to the given {@link ChangedValue} the corresponding
    * {@link BusinessDayIncrement} evaluated. If there is one then the value is
    * changed
    * 
    * @param changeValue
    *        the param which defines what value is changed
    * @see ValueTypes
    */
   public void changeBusinesDayIncrement(ChangedValue changeValue) {
      Optional<BusinessDayIncrement> businessDayIncOpt = getBusinessIncrement(changeValue.getSequence());
      businessDayIncOpt.ifPresent(businessDayIncrement -> handleBusinessDayChangedInternal(businessDayIncrement, changeValue));
   }

   /**
    * Verifies if there is any {@link BusinessDayIncrement} which was recorded e.g.
    * during a preceding day
    * 
    * @return <code>true</code> if there is at least one
    *         {@link BusinessDayIncrement} which was recorded on a preceding day or
    *         <code>false</code> if not
    */
   public boolean hasElementsFromPrecedentDays() {
      Time now = new Time();
      return increments.stream().anyMatch(bDayInc -> bDayInc.isBefore(now));
   }

   /**
    * Returns a message since when the capturing is active
    * 
    * @return the message since when the capturing is active
    */
   public String getCapturingActiveSinceMsg() {
      TimeSnippet startPoint = currentBussinessDayIncremental.getCurrentTimeSnippet();
      String time = startPoint.getDuration() > 0 ? " (" + startPoint.getDuration() + "h)" : "";
      return TextLabel.CAPTURING_ACTIVE_SINCE + " " + startPoint.getBeginTimeStamp() + time;
   }

   /**
    * @return a message since when the capturing is inactive
    */
   public String getCapturingInactiveSinceMsg() {
      TimeSnippet endPoint = getLastTimeSnippet();
      if (endPoint != null) {
         return TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INCTIVE_SINCE + " "
               + endPoint.getEndTimeStamp();
      }
      return TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE;
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
    * {@link BusinessDay}
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
      currentBussinessDayIncremental = new BusinessDayIncrement(new Date());
   }


   private float getTotalDuration(TIME_TYPE type) {
      float sum = 0;

      for (BusinessDayIncrement incremental : increments) {
         sum = sum + incremental.getTotalDuration(type);
      }
      return NumberFormat.parseFloat(NumberFormat.format(sum));
   }
}
