package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import static java.util.Objects.requireNonNull;

import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;

public class JiraApiReaderBuilder {

   private JiraApiConfiguration jiraApiConfiguration;

   private JiraApiReaderBuilder() {
      // private 
   }

   public JiraApiReaderBuilder withJiraApiConfiguration(JiraApiConfiguration jiraApiConfiguration) {
      this.jiraApiConfiguration = jiraApiConfiguration;
      return this;
   }

   public JiraApiReader build() {
      requireNonNull(jiraApiConfiguration, "Call withJiraApiConfiguration first!");
      return JiraApiReaderFactory.createNew(jiraApiConfiguration);
   }

   public static JiraApiReaderBuilder of() {
      return new JiraApiReaderBuilder();
   }
}
