package com.adcubum.timerecording.core.work.businessday.vo;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.jira.data.Ticket;

public interface BusinessDayIncrementVO {

   /**
    * Returns <code>true</code> if this {@link BusinessDayIncrement} has a valid
    * description or <code>false</code> if not
    * 
    * @return<code>true</code> if this {@link BusinessDayIncrement} has a valid
    * description or <code>false</code> if not
    */
   boolean hasDescription();

   String getTotalDurationRep();

   String getDescription();

   Ticket getTicket();

   String getTicketNumber();

   int getChargeType();

   boolean isBooked();

   TimeSnippet getCurrentTimeSnippet();

   /**
    * @return the description for it's service-code
    * @see BusinessDayIncrementVO#getChargeType()
    */
   String getServiceCodeDescription4ServiceCode();

}
