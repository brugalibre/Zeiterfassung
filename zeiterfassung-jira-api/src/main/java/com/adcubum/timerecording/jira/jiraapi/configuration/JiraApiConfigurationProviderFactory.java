package com.adcubum.timerecording.jira.jiraapi.configuration;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * The {@link JiraApiConfigurationProviderFactory} is used to create and instantiate new {@link JiraApiConfigurationProvider} instances
 *
 * @author DStalder
 */
public class JiraApiConfigurationProviderFactory extends AbstractFactory {
    private static final String BEAN_NAME = "jiraapiconfigurationprovider";
    private static final JiraApiConfigurationProviderFactory INSTANCE = new JiraApiConfigurationProviderFactory();

    private JiraApiConfigurationProviderFactory() {
        super("jira-module-configuration.xml");
    }

    /**
     * Creates a new Instance or reuse an existing default JiraApiConfiguration
     *
     * @return a new Instance or an already existing default JiraApiConfiguration
     */
    public static JiraApiConfigurationProvider createNew() {
        return INSTANCE.createNewWithAgruments(BEAN_NAME);
    }
}
