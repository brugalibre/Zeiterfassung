package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;

/**
 * The {@link JiraApiReaderFactory} is used to create and instantiate new {@link JiraApiReader} instances
 * 
 * @author DStalder
 *
 */
public class JiraApiReaderFactory extends AbstractFactory {
   private static final String BEAN_NAME = "jiraapireader";
   private static final JiraApiReaderFactory INSTANCE = new JiraApiReaderFactory();

   private JiraApiReaderFactory() {
      super("jira-module-configuration.xml");
   }

   /**
    * Creates a new Instance of the JiraApiReader or returns an already created instance
    * 
    * @param jiraApiConfiguration
    *        the {@link JiraApiConfiguration}
    * 
    * @return a new Instance of the JiraApiReader or returns an already created instance
    */
   public static JiraApiReader createNew(JiraApiConfiguration jiraApiConfiguration) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, jiraApiConfiguration);
   }
}
