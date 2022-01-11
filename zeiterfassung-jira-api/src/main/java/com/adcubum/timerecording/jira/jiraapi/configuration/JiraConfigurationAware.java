package com.adcubum.timerecording.jira.jiraapi.configuration;

/**
 * Interface for all classes which contains a {@link JiraApiConfiguration}
 */
public interface JiraConfigurationAware {

   /**
    * Applies the given {@link JiraApiConfiguration}
    *
    * @param jiraApiConfiguration
    *        the configuration to apply
    */
   void applyJiraApiConfiguration(JiraApiConfiguration jiraApiConfiguration);
}
