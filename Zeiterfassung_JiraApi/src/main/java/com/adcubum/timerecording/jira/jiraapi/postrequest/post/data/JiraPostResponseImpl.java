package com.adcubum.timerecording.jira.jiraapi.postrequest.post.data;

import com.adcubum.timerecording.jira.jiraapi.readresponse.response.error.JiraErrorResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraPostResponseImpl extends JiraErrorResponse implements JiraPostResponse {

   public JiraPostResponseImpl() {
      // empty for jackson-fasterxml
      super(null, null);
   }

   public JiraPostResponseImpl(Exception e, String url) {
      super(e, url);
   }

}
