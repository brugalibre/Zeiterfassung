/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday;

import static java.util.Objects.isNull;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.myownb3.dominic.timerecording.core.callbackhandler.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.BusinessDayIncrementImport;
import com.myownb3.dominic.timerecording.core.charge.ChargeType;
import com.myownb3.dominic.timerecording.core.charge.exception.InvalidChargeTypeRepresentationException;
import com.myownb3.dominic.timerecording.core.work.date.Time;
import com.myownb3.dominic.timerecording.core.work.date.TimeType;
import com.myownb3.dominic.timerecording.core.work.date.TimeType.TIME_TYPE;
import com.myownb3.dominic.util.parser.NumberFormat;

/**
 * A {@link BusinessDay} consist of one or more {@link BusinessDayIncrement}.
 * Where as a {@link BusinessDayIncrement} consist of one or more
 * {@link TimeSnippet}. Two different {@link BusinessDayIncrement} are <i> not
 * </i> dependent!
 * 
 * @author Dominic
 */
public class BusinessDayIncrement {
   private TimeSnippet currentTimeSnippet;

   private Date date;
   private String description;
   private String ticketNumber;
   private int chargeType;
   private boolean isCharged;

   public BusinessDayIncrement(Date date) {
      this.date = date;
      ticketNumber = "SYRIUS-";
   }

   /**
    * @param beginTimeStamp
    */
   public void startCurrentTimeSnippet(Time beginTimeStamp) {
      createNewTimeSnippet();
      currentTimeSnippet.setBeginTimeStamp(beginTimeStamp);
   }

   /**
    * @param endTimeStamp
    */
   public void stopCurrentTimeSnippet(Time endTimeStamp) {
      currentTimeSnippet.setEndTimeStamp(endTimeStamp);
   }

   public void resumeLastTimeSnippet() {
      currentTimeSnippet.setEndTimeStamp(null);
   }

   private void createNewTimeSnippet() {
      currentTimeSnippet = new TimeSnippet(date);
   }

   public Date getDate() {
      return date;
   }

   public float getTotalDuration() {
      return getTotalDuration(TimeType.DEFAULT);
   }

   public void flagAsCharged() {
      this.isCharged = true;
   }

   /**
    * Calculates the total amount of working minuts of the current {@link TimeSnippet}
    * 
    * @param type
    *        the {@link TIME_TYPE}
    * @return the total amount of working minuts of the current {@link TimeSnippet}
    */
   public float getTotalDuration(TIME_TYPE type) {
      if (isNull(currentTimeSnippet)) {
         return 0f;
      }
      float sum = currentTimeSnippet.getDuration(type);
      return NumberFormat.parseFloat(NumberFormat.format(sum));
   }

   public TimeSnippet getCurrentTimeSnippet() {
      return currentTimeSnippet;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Sets the charge-type as int from the given representation.
    * 
    * @param chargeTypeRep
    *        the new representation of a charge type
    * @throws InvalidChargeTypeRepresentationException
    */
   public void setChargeType(String chargeTypeRep) throws InvalidChargeTypeRepresentationException {
      this.chargeType = ChargeType.getLeistungsartForRep(chargeTypeRep);
   }

   public String getTicketNumber() {
      return ticketNumber;
   }

   public void setTicketNumber(String ticketNumber) {
      this.ticketNumber = ticketNumber;
   }

   public int getChargeType() {
      return chargeType;
   }


   public boolean isCharged() {
      return isCharged;
   }

   /**
    * Updates the {@link TimeSnippet} at the given index and recalulates the entire
    * {@link BusinessDay}. If the update would lead to a negative duration, it's skipped
    * 
    * @param newTimeStampValue
    *        the new value for the time stamp
    */
   public void updateBeginTimeSnippetAndCalculate(String newTimeStampValue) {
      currentTimeSnippet.updateAndSetBeginTimeStamp(newTimeStampValue, true);
   }

   /**
    * Updates the {@link TimeSnippet} at the given index and recalulates the entire
    * {@link BusinessDay}. If the update would lead to a negative duration, it's skipped
    * 
    * @param newTimeStampValue
    *        the new value for the time stamp
    */
   public void updateEndTimeSnippetAndCalculate(String newTimeStampValue) {
      currentTimeSnippet.updateAndSetEndTimeStamp(newTimeStampValue, true);
   }

   public static class TimeStampComparator implements Comparator<TimeSnippet> {
      @Override
      public int compare(TimeSnippet timeSnippet, TimeSnippet timeSnippet2) {
         Time beginTimeStamp1 = timeSnippet.getBeginTimeStamp();
         Time beginTimeStamp2 = timeSnippet2.getBeginTimeStamp();
         return beginTimeStamp1.compareTo(beginTimeStamp2);
      }
   }

   /**
    * Creates a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementAdd}
    * 
    * @param update
    *        the {@link BusinessDayIncrementAdd} with the new values
    * @return a new {@link BusinessDayIncrement}
    */
   public static BusinessDayIncrement of(BusinessDayIncrementAdd update) {

      BusinessDayIncrement businessDayIncremental = new BusinessDayIncrement(update.getTimeSnippet().getDate());
      businessDayIncremental.description = update.getDescription();
      businessDayIncremental.ticketNumber = update.getTicketNo();
      businessDayIncremental.chargeType = update.getKindOfService();
      businessDayIncremental.startCurrentTimeSnippet(update.getTimeSnippet().getBeginTimeStamp());
      businessDayIncremental.stopCurrentTimeSnippet(update.getTimeSnippet().getEndTimeStamp());
      return businessDayIncremental;
   }

   /**
    * Creates a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementImport} with all its {@link TimeSnippet}s
    * 
    * @param businessDayIncrementImport
    * @return a new {@link BusinessDayIncrement}
    */
   public static BusinessDayIncrement of(BusinessDayIncrementImport businessDayIncrementImport) {

      List<TimeSnippet> timeSnippets2Add = businessDayIncrementImport.getTimeSnippets();
      Date date = new Date();
      if (!timeSnippets2Add.isEmpty()) {
         date = timeSnippets2Add.get(0).getDate();
      }
      BusinessDayIncrement businessDayIncremental = new BusinessDayIncrement(date);
      businessDayIncremental.description = businessDayIncrementImport.getDescription();
      businessDayIncremental.ticketNumber = businessDayIncrementImport.getTicketNo();
      businessDayIncremental.chargeType = businessDayIncrementImport.getKindOfService();

      for (TimeSnippet timeSnippet : timeSnippets2Add) {
         businessDayIncremental.startCurrentTimeSnippet(timeSnippet.getBeginTimeStamp());
         businessDayIncremental.stopCurrentTimeSnippet(timeSnippet.getEndTimeStamp());
      }
      return businessDayIncremental;
   }

   /**
    * Returns <code>true</code> if this {@link BusinessDayIncrement} has started
    * before the given date. This method returns <code>false</code> if this
    * {@link BusinessDayIncrement} was created on the same day the given Time
    * instance has.
    * 
    * @param time2Check
    *        the {@link Time} to check
    * @return <code>true</code> if this {@link BusinessDayIncrement} was created
    *         before the given date. Otherwise return <code>false</code>
    */
   public boolean isBefore(Time time2Check) {

      long days = time2Check.getDays();
      Time bdTime = new Time(date.getTime());
      return days > bdTime.getDays();
   }
}
