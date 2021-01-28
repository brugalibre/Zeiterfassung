package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The {@link JiraIssueGenericValueObject} is a generic object which contains always an attribute called 'value'
 * 
 * @author Dominic
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueGenericValueObject {
   private String value;

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
