package com.adcubum.timerecording.core.work.businessday;

import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;

public interface BusinessDayIncrement extends DomainModel {


   /**
    * Updates the {@link TimeSnippet} at the given index and recalulates the entire
    * {@link BusinessDay}. If the update would lead to a negative duration, it's skipped
    * 
    * @param newTimeStampValue
    *        the new value for the time stamp
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement updateEndTimeSnippetAndCalculate(String newTimeStampValue);

   /**
    * Updates the {@link TimeSnippet} at the given index and recalulates the entire
    * {@link BusinessDay}. If the update would lead to a negative duration, it's skipped
    * 
    * @param newTimeStampValue
    *        the new value for the time stamp
    * @return a new {@link BusinessDayIncrement} with the changes
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement updateBeginTimeSnippetAndCalculate(String newTimeStampValue);

   /**
    * @param endTimeStamp
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement stopCurrentTimeSnippet(DateTime endTimeStamp);

   /**
    * @param beginTimeStamp
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement startCurrentTimeSnippet(DateTime beginTimeStamp);

   /**
    * Adds the given amount of time to the {@link TimeSnippet} of this {@link BusinessDayIncrement}
    * 
    * @param time2Add
    *        the additionally time to add to this {@link BusinessDayIncrement}
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement addAdditionallyTime(float time2Add);

   /**
    * If the {@link Ticket} of this {@link BusinessDayIncrement} is a dummy-Ticket, then it's
    * read again from the
    * 
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement refreshDummyTicket();

   /**
    * Flags this {@link BusinessDayIncrement} as booked
    * 
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement flagAsBooked();

   /**
    * Defines a new {@link Ticket}
    * 
    * @param ticket
    *        the new {@link Ticket}
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement setTicket(Ticket ticket);

   /**
    * Sets the service code int from the given representation of
    * a service code
    * 
    * @param serviceCodeRep
    *        the new representation of a service-code
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement setServiceCode4Description(String serviceCodeRep);

   /**
    * Defines the service code of this {@link BusinessDayIncrement}
    * 
    * @param serviceCode
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement setServiceCode(int serviceCode);

   BusinessDayIncrement setDescription(String description);

   /**
    * @return <code>true</code> if there this {@link BusinessDayIncrement} contains a {@link Ticket} for which are all relevant value
    *         present or <code>false</code> if not
    */
   boolean isBookable();

   /**
    * @return <code>true</code> if this {@link BusinessDayIncrement} is charged or <code>false</code> if not
    */
   boolean isBooked();

   /**
    * @return <code>true<code> if this {@link BusinessDayIncrement} has a description or <code>false<code>, if not
    */
   boolean hasDescription();

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

   String getServiceCodeDescription();

   String getDescription();

   float getTotalDuration();

   /**
    * @return the {@link DateTime} of this {@link BusinessDayIncrement}
    */
   DateTime getDateTime();
}
