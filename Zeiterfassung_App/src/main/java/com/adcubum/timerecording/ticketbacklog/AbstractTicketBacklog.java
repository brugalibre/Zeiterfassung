package com.adcubum.timerecording.ticketbacklog;

import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfigurationBuilder;
import com.adcubum.timerecording.ticketbacklog.callback.TicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;
import com.adcubum.timerecording.ticketbacklog.config.TicketConfiguration;
import com.adcubum.timerecording.ticketbacklog.config.TicketConfigurationImpl;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class AbstractTicketBacklog implements TicketBacklog {

    protected List<TicketBacklogCallbackHandler> callbackHandlers;
    protected TicketBacklogHelper backlogHelper;

    public AbstractTicketBacklog () {
        this.callbackHandlers = new ArrayList<>();
        this.backlogHelper = new TicketBacklogHelper();
    }

    @Override
    public void addTicketBacklogCallbackHandler(TicketBacklogCallbackHandler callbackHandler) {
        this.callbackHandlers.add(requireNonNull(callbackHandler));
    }

    @Override
    public TicketConfiguration getTicketConfiguration() {
        JiraApiConfiguration jiraApiConfiguration = buildJiraApiConfiguration();
        return new TicketConfigurationImpl(jiraApiConfiguration.getTicketNamePattern(),
                jiraApiConfiguration.getDefaultTicketName(), jiraApiConfiguration.getMultiTicketNoPattern());
    }

    protected void notifyCallbackHandlers(UpdateStatus updateStatus) {
        callbackHandlers.forEach(callbackHandler -> callbackHandler.onTicketBacklogUpdated(updateStatus));
    }

    protected JiraApiConfiguration buildJiraApiConfiguration() {
        return JiraApiConfigurationBuilder.of()
                .withDefaultJiraApiConfiguration()
                .withNullableJiraAgileBasePath(backlogHelper.getJiraBaseUrl())
                .withNullableTicketNamePattern(backlogHelper.getTicketNamePattern())
                .withNullableDefaultTicketName(backlogHelper.getDefaultTicketName())
                .build();
    }
}
