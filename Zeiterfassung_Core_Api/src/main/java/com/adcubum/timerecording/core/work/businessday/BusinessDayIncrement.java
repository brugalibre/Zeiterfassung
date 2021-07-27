package com.adcubum.timerecording.core.work.businessday;

import java.util.Date;

import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;

public interface BusinessDayIncrement extends DomainModel {


   /**
    * Updates the {@link TimeSnippet} at the given index and recalulates the entire
    * {@link BusinessDay}. If the update would lead to a negative duration, it's skipped
    * 
    * @param newTimeStampValue
    *        the new value for the time stamp
    */
   void updateEndTimeSnippetAndCalculate(String newTimeStampValue);

   /**
    * Updates the {@link TimeSnippet} at the given index and recalulates the entire
    * {@link BusinessDay}. If the update would lead to a negative duration, it's skipped
    * 
    * @param newTimeStampValue
    *        the new value for the time stamp
    */
   void updateBeginTimeSnippetAndCalculate(String newTimeStampValue);

   void resumeLastTimeSnippet();

   /**
    * @param endTimeStamp
    */
   void stopCurrentTimeSnippet(Time endTimeStamp);

   /**
    * @param beginTimeStamp
    */
   void startCurrentTimeSnippet(Time beginTimeStamp);

   /**
    * If the {@link Ticket} of this {@link BusinessDayIncrement} is a dummy-Ticket, then it's
    * read again from the
    */
   void refreshDummyTicket();

   void flagAsCharged();

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
   boolean isBefore(Time time2Check);

   /**
    * @return <code>true</code> if there this {@link BusinessDayIncrement} contains a {@link Ticket} for which are all relevant value
    *         present or <code>false</code> if not
    */
   boolean isBookable();

   /**
    * @return <code>true</code> if this {@link BusinessDayIncrement} is charged or <code>false</code> if not
    */
   boolean isCharged();

   /**
    * Calculates the total amount of working minuts of the@Override
    * current {@link TimeSnippet}
    * 
    * @param type
    * @Override
    *           the {@link TIME_TYPE}
    * @return the total amount of working minuts of the current {@link TimeSnippet}
    * @Override
    */
   float getTotalDuration(TIME_TYPE type);

   TimeSnippet getCurrentTimeSnippet();

   Ticket getTicket();

   /**
    * @return the charge type of this {@link BusinessDayIncrement}
    */
   int getChargeType();

   String getDescription();

   float getTotalDuration();

   Date getDate();

   void setTicket(Ticket ticket);

   /**
    * Sets the charge-type as int from @Override
    * the given representation.
    * 
    * @param chargeTypeRep
    *        the new representation of a charge type
    */
   void setChargeType(String chargeTypeRep);

   void setChargeType(int chargeType);

   void setDescription(String description);

}
