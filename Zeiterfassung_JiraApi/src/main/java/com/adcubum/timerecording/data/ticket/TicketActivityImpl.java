package com.adcubum.timerecording.data.ticket;

import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

import java.util.Objects;

public class TicketActivityImpl implements TicketActivity {
    private String activityName;
    private int activityCode;

    public TicketActivityImpl(String activityName, int activityNo) {
        this.activityName = activityName;
        this.activityCode = activityNo;
    }

    @Override
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public int getActivityCode() {
        return activityCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketActivityImpl that = (TicketActivityImpl) o;
        return activityCode == that.activityCode && Objects.equals(activityName, that.activityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityName, activityCode);
    }

    public void setActivityCode(int activityCode) {
        this.activityCode = activityCode;
    }
}
