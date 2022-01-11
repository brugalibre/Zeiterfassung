package com.adcubum.timerecording.core.book.jira;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.data.Worklog;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.util.parser.DateParser;

import static java.util.Objects.requireNonNull;

public class JiraWorklogImpl implements Worklog {

   private String comment;
   private int timeSpentSeconds;
   private String started;
   private String issueNr;

   private JiraWorklogImpl(String comment, int timeSpentSeconds, String started, String issueNr) {
      this.timeSpentSeconds = timeSpentSeconds;
      this.comment = requireNonNull(comment);
      this.started = requireNonNull(started);
      this.issueNr = requireNonNull(issueNr);
   }

   @Override
   public String toString() {
      return "JiraWorklogImpl{" +
              "comment='" + comment + '\'' +
              ", timeSpentSeconds=" + timeSpentSeconds +
              ", started='" + started + '\'' +
              ", issueNr='" + issueNr + '\'' +
              '}';
   }

   /**
    * Creates a new {@link JiraWorklogImpl} for the given {@link BusinessDayIncrement}
    *
    * @param businessDayIncrement the {@link BusinessDayIncrement}
    * @param bookingDateTime      the date when the work to place
    * @return a new {@link JiraWorklogImpl}
    */
   public static JiraWorklogImpl of(BusinessDayIncrement businessDayIncrement, DateTime bookingDateTime) {
      String started = DateParser.parse2String(bookingDateTime, DateParser.YYYY_MM_DD_T_HH_MINMIN_SS_SSSZ);
      int timeSpentSeconds = (int) (businessDayIncrement.getTotalDuration() * 3600);
      return new JiraWorklogImpl(businessDayIncrement.getDescription(), timeSpentSeconds, started, businessDayIncrement.getTicket().getNr());
   }

   @Override
   public String getComment() {
      return comment;
   }

   @Override
   public int getTimeSpentSeconds() {
      return timeSpentSeconds;
   }

   @Override
   public String getStarted() {
      return started;
   }

   @Override
   public String getIssueNr() {
      return issueNr;
   }
}
