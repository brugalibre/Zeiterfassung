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
   void stopCurrentTimeSnippet(DateTime endTimeStamp);

   /**
    * @param beginTimeStamp
    */
   void startCurrentTimeSnippet(DateTime beginTimeStamp);

   /**
    * If the {@link Ticket} of this {@link BusinessDayIncrement} is a dummy-Ticket, then it's
    * read again from the
    */
   void refreshDummyTicket();

   void flagAsCharged();

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

   String getServiceCodeDescription();

   String getDescription();

   float getTotalDuration();

   void setTicket(Ticket ticket);

   /**
    * Sets the service code int from the given representation of
    * a service code
    * 
    * @param serviceCodeRep
    *        the new representation of a service-code
    */
   void setServiceCode4Description(String serviceCodeRep);

   /**
    * Defines the service code of this {@link BusinessDayIncrement}
    * 
    * @param serviceCode
    */
   void setServiceCode(int serviceCode);

   void setDescription(String description);

   /**
    * @return the {@link DateTime} of this {@link BusinessDayIncrement}
    */
   DateTime getDateTime();
}
