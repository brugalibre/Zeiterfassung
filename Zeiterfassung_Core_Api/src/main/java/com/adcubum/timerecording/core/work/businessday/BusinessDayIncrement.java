package com.adcubum.timerecording.core.work.businessday;

import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;

public interface BusinessDayIncrement extends DomainModel {


   /**
    * Updates the {@link TimeSnippet} at the given index and recalulates the entire
    * {@link BusinessDay}. If the update would lead to a negative duration, it's skipped
    * Note: if this {@link BusinessDayIncrement} is already booked, this {@link BusinessDayIncrement} is returned unchanged
    * 
    * @param newTimeStampValue
    *        the new value for the time stamp
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement updateEndTimeSnippetAndCalculate(String newTimeStampValue);

   /**
    * Updates the {@link TimeSnippet} at the given index and recalulates the entire
    * {@link BusinessDay}. If the update would lead to a negative duration, it's skipped
    * Note: if this {@link BusinessDayIncrement} is already booked, this {@link BusinessDayIncrement} is returned unchanged
    * 
    * @param newTimeStampValue
    *        the new value for the time stamp
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement updateBeginTimeSnippetAndCalculate(String newTimeStampValue);

   /**
    * @param endTimeStamp the {@link DateTime} as the new end-time stamp
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement stopCurrentTimeSnippet(DateTime endTimeStamp);

   /**
    * @param beginTimeStamp the {@link DateTime} as the new begin-time stamp
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement startCurrentTimeSnippet(DateTime beginTimeStamp);

   /**
    * Adds the given amount of time to the {@link TimeSnippet} of this {@link BusinessDayIncrement}
    * Note: if this {@link BusinessDayIncrement} is already booked, this {@link BusinessDayIncrement} is returned unchanged
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
    * Flags this {@link BusinessDayIncrement} as sent
    *
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement flagAsSent();

   /**
    * Defines a new {@link Ticket}
    * Note: if this {@link BusinessDayIncrement} is already booked, this {@link BusinessDayIncrement} is returned unchanged
    * 
    * @param ticket
    *        the new {@link Ticket}
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement setTicket(Ticket ticket);

   /**
    * Sets a new {@link TicketActivity} with a different name and code
    * Note: if this {@link BusinessDayIncrement} is already booked, this {@link BusinessDayIncrement} is returned unchanged
    * 
    * @param ticketActivity
    *        the new {@link TicketActivity}
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement setTicketActivity(TicketActivity ticketActivity);

   /**
    * Sets a new description
    * Note: if this {@link BusinessDayIncrement} is already booked, this {@link BusinessDayIncrement} is returned unchanged
    *
    * @param description
    *        the new description
    * @return a new {@link BusinessDayIncrement} with the changes
    */
   BusinessDayIncrement setDescription(String description);

   /**
    * @return <code>true</code> if there this {@link BusinessDayIncrement} contains a {@link Ticket} for which are all relevant value
    *         present or <code>false</code> if not
    */
   boolean isBookable();

   /**
    * @return <code>true</code> if this {@link BusinessDayIncrement} is booked or <code>false</code> if not
    */
   boolean isBooked();

   /**
    * @return <code>true</code> if this {@link BusinessDayIncrement} is sent or <code>false</code> if not
    */
   boolean isSent();

   /**
    * @return <code>true<code> if this {@link BusinessDayIncrement} has a description or <code>false<code>, if not
    */
   boolean hasDescription();

   /**
    * Calculates the total amount of working minuts of the@Override
    * current {@link TimeSnippet}
    * 
    * @param type
    *           the {@link TIME_TYPE}
    * @return the total amount of working minuts of the current {@link TimeSnippet}
    */
   float getTotalDuration(TIME_TYPE type);

   TimeSnippet getCurrentTimeSnippet();

   Ticket getTicket();

   /**
    * @return the {@link TicketActivity} of this {@link BusinessDayIncrement}
    */
   TicketActivity getTicketActivity();

   String getDescription();

   float getTotalDuration();

   /**
    * @return the {@link DateTime} of this {@link BusinessDayIncrement}
    */
   DateTime getDateTime();
}
