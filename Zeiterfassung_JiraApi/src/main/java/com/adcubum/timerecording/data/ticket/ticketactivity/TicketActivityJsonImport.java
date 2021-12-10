package com.adcubum.timerecording.data.ticket.ticketactivity;

import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

public class TicketActivityJsonImport {
    private String activityName;
    private int activityCode;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public long getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(int activityCode) {
        this.activityCode = activityCode;
    }

    public static TicketActivity mapToProlesActivity(TicketActivityJsonImport ticketActivityJsonImport) {
        return new TicketActivityImpl(ticketActivityJsonImport.activityName, ticketActivityJsonImport.activityCode, false, ticketActivityJsonImport.activityName);
    }
}
