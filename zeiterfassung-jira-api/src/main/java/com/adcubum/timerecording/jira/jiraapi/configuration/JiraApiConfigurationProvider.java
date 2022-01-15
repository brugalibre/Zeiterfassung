package com.adcubum.timerecording.jira.jiraapi.configuration;

public interface JiraApiConfigurationProvider {

   /**
    * The singleton instance of the {@link JiraApiConfigurationProvider}
    */
   JiraApiConfigurationProvider INSTANCE = JiraApiConfigurationProviderFactory.createNew();

   /**
    * Returns reads the configuration from an optional config file and applies this configuration to
    * the systems default configuration for the jira api
    *
    * @return the default {@link JiraApiConfiguration}
    */
   JiraApiConfiguration getJiraApiConfiguration();
}
