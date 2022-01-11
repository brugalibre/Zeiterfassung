package com.adcubum.timerecording.core.book.jira;

import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponse;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.JiraApiWorklogCreator;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.data.Worklog;

import java.util.List;

public class TestJiraApiWorklogCreatorImpl implements JiraApiWorklogCreator {

   private List<String> successfullTicketNrs;

   public TestJiraApiWorklogCreatorImpl(List<String> successfullTicketNrs) {
      this.successfullTicketNrs = successfullTicketNrs;
   }

   @Override
   public JiraPostResponse createWorklog(Worklog worklog) {
      return new JiraPostResponse() {
         @Override
         public boolean isSuccessful() {
            return successfullTicketNrs.contains(worklog.getIssueNr());
         }

         @Override
         public void setIsSuccessful(boolean successful) {

         }
      };
   }
}
