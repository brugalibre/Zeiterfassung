package com.adcubum.timerecording.jira.data.ticket;

/**
 * A {@link TicketActivity} is an activity like programming or meeting which is possible for a specific ticket
 * @author dstalder
 */
public interface TicketActivity {

    /**
     * @return the name of this activity
     */
    String getActivityName();

    /**
     * @return the code of this activity
     */
    int getActivityCode();
}
