package com.adcubum.timerecording.core.work.businessday;

public enum ValueTypes {

   /** The Jira-Ticket itself */
   TICKET,
   /** Number of the Jira-Ticket */
   TICKET_NR,
   /** The additionally description of the charging */
   DESCRIPTION,
   /** The Begin of a business-day-increment */
   BEGIN,
   /** The end of a business-day-increment */
   END,
   /** The total amount of time of a {@link TimeSnippet} */
   AMOUNT_OF_TIME,
   /**
    * The type of charge such as '113 - Programmieren', '122 - Testing' and so on. This is kind of deprecated since here we have to work
    * with the description of a service code, rather with the value itself
    **/
   TICKET_ACTIVITY,
   /** no specific value */
   NONE;
}
