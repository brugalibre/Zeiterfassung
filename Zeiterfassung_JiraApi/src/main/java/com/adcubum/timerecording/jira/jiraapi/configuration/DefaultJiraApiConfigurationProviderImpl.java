package com.adcubum.timerecording.jira.jiraapi.configuration;

public class DefaultJiraApiConfigurationProviderImpl implements DefaultJiraApiConfigurationProvider {

    private JiraApiConfiguration defaultJiraApiConfiguration;

    private DefaultJiraApiConfigurationProviderImpl() {
        this.defaultJiraApiConfiguration = JiraApiConfigurationBuilder.of()
                .withNullableFetchBoardsBeginIndex(0)
                .withNullableBoardType(JiraApiConstants.SCRUM)
                .withJiraUrl(JiraApiConstants.JIRA_BASE_URL_KEY)
                .withJiraAgileBasePath(JiraApiConstants.JIRA_AGILE_BASE_PATH)
                .withNullableJiraWorklogBasePath(JiraApiConstants.JIRA_WORKLOG_V2_BASE_PATH)
                .withFetchResultSize(JiraApiConstants.JIRA_MAX_RESULTS_RETURNED)
                .withStartAtPlaceholder(JiraApiConstants.START_AT_PLACE_HOLDER)
                .withBoardIdPlaceholder(JiraApiConstants.BOARD_ID_PLACE_HOLDER)
                .withSprintIdPlaceholder(JiraApiConstants.SPRINT_ID_PLACE_HOLDER)
                .withNullableTicketNamePattern(TicketConst.TICKET_NO_PATTERN)
                .withNullableDefaultTicketName(TicketConst.DEFAULT_TICKET_NAME)
                .build();
    }

    @Override
    public JiraApiConfiguration getDefaultJiraApiConfiguration() {
        return defaultJiraApiConfiguration;
    }
}
