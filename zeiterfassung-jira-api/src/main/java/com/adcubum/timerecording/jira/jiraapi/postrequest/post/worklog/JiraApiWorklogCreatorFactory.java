package com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;

import java.util.function.Supplier;

/**
 * The {@link JiraApiWorklogCreatorFactory} is used to create and instantiate new {@link JiraApiWorklogCreator} instances
 *
 * @author DStalder
 */
public class JiraApiWorklogCreatorFactory extends AbstractFactory {
   private static final String BEAN_NAME = "jiraapiworklogcreator";
   private static final JiraApiWorklogCreatorFactory INSTANCE = new JiraApiWorklogCreatorFactory();

   private JiraApiWorklogCreatorFactory() {
      super("jira-module-configuration.xml");
   }

   /**
    * Creates a new Instance of the JiraApiReader or returns an already created instance
    *
    * @param jiraApiConfiguration the {@link JiraApiConfiguration}
    * @param username             the username
    * @param userPw               the password supplier
    * @return a new Instance of the JiraApiReader or returns an already created instance
    */
   public static JiraApiWorklogCreator createNew(JiraApiConfiguration jiraApiConfiguration, String username, Supplier<char[]> userPw) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, jiraApiConfiguration, username, userPw);
   }
}
