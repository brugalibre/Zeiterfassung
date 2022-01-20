package com.adcubum.timerecording.test;

import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

import java.util.Objects;

public class TestTicketActivity implements TicketActivity {

   private final String activityName;
   private final int activityCode;

   public TestTicketActivity(String activityName, int activityCode) {
      this.activityName = activityName;
      this.activityCode = activityCode;
   }

   @Override
   public String toString() {
      return "activityName='" + activityName + "'"
              + "activityCode='" + activityCode + "'";
   }

   @Override
   public String getActivityName() {
      return activityName;
   }

   @Override
   public int getActivityCode() {
      return activityCode;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TicketActivity that = (TicketActivity) o;
      return Objects.equals(activityCode, that.getActivityCode()) && Objects.equals(activityName, that.getActivityName());
   }

   @Override
   public int hashCode() {
      return Objects.hash(activityCode, activityName);
   }

   @Override
   public boolean isDummy() {
      return false;
   }
}
