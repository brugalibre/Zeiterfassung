package com.adcubum.timerecording.jira.jiraapi.configuration;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;

/**
 * The {@link JiraApiConfigurationFactory} is used to create and instantiate new {@link JiraApiReader} instances
 *
 * @author DStalder
 */
public class JiraApiConfigurationFactory extends AbstractFactory {
    private static final String BEAN_NAME = "jiraapiconfiguration";
    private static final JiraApiConfigurationFactory INSTANCE = new JiraApiConfigurationFactory();

    private JiraApiConfigurationFactory() {
        super("jira-module-configuration.xml");
    }

    /**
     * Creates a new Instance or reuse an existing default JiraApiConfiguration
     *
     * @return a new Instance or an already existing default JiraApiConfiguration
     */
    public static JiraApiConfiguration createDefault() {
        DefaultJiraApiConfigurationProvider defaultJiraApiConfigurationProvider = INSTANCE.createNewWithAgruments(BEAN_NAME);
        return defaultJiraApiConfigurationProvider.getDefaultJiraApiConfiguration();
    }
}
