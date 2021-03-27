package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The {@link JiraIssueThemaInfos} contains all about the thema, subthema and project cost-unit
 * 
 * @author Dominic
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueThemaInfos {
   private String value; // contains e.g. the string literal 'Vertragsverwaltung (Health) 41003'
   private JiraIssueGenericValueObject child;

   public JiraIssueThemaInfos() {
      this.child = new JiraIssueGenericValueObject();
   }

   public JiraIssueGenericValueObject getChild() {
      return child;
   }

   public void setChild(JiraIssueGenericValueObject child) {
      this.child = requireNonNull(child);
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
