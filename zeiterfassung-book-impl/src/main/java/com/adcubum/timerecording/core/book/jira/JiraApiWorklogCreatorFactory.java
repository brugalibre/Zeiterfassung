package com.adcubum.timerecording.core.book.jira;

import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.JiraApiWorklogCreator;

import java.util.function.Supplier;

/**
 * Interface in order to capsulate the static factory {@link com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.JiraApiWorklogCreatorFactory}
 */
@FunctionalInterface
public interface JiraApiWorklogCreatorFactory {
   /**
    * Creates a new {@link JiraApiWorklogCreatorFactory}
    *
    * @param jiraApiConfiguration the {@link JiraApiConfiguration}
    * @param username             the username
    * @param userPw               the password
    * @return always a new {@link JiraApiWorklogCreatorFactory}
    */
   JiraApiWorklogCreator createNew(JiraApiConfiguration jiraApiConfiguration, String username, Supplier<char[]> userPw);

}

