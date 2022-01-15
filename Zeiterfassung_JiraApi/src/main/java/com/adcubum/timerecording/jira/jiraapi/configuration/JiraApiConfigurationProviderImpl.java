package com.adcubum.timerecording.jira.jiraapi.configuration;

public class JiraApiConfigurationProviderImpl implements JiraApiConfigurationProvider {

   private final JiraApiConfigurationHelper jiraApiConfigurationHelper;

   private JiraApiConfigurationProviderImpl() {
      this.jiraApiConfigurationHelper = new JiraApiConfigurationHelper();
   }

   @Override
   public JiraApiConfiguration getJiraApiConfiguration() {
      return JiraApiConfigurationBuilder.of()
              // first set default values
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

              // No override with customized values - if specified
              .withNullableBoardType(jiraApiConfigurationHelper.getBoardType())
              .withNullableFetchBoardsBeginIndex(jiraApiConfigurationHelper.getFetchBoardsBeginIndex())
              .withNullableJiraUrl(jiraApiConfigurationHelper.getJiraBaseUrl())
              .withNullableTicketNamePattern(jiraApiConfigurationHelper.getTicketNamePattern())
              .withNullableDefaultTicketName(jiraApiConfigurationHelper.getDefaultTicketName())
              .build();
   }
}
