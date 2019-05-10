package com.myownb3.dominic.timerecording.work.businessday;

public enum ValueTypes {

    /** Number of the Jira-Ticket */
    TICKET_NR,
    /** The additionally description of the charging */
    DESCRIPTION,
    /** The Begin of a {@link BusinessDayIncremental} */
    BEGIN,
    /** The end of a {@link BusinessDayIncremental} */
    END,
    /** The total amount of time of a {@link TimeSnippet} */
    AMOUNT_OF_TIME,

    /** no specific value */
    NONE;
}
