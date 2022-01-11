package com.adcubum.timerecording.jira.jiraapi.configuration;

import static com.adcubum.util.utils.StringUtil.requireNotEmptyAndNotNull;
import static java.util.Objects.requireNonNull;

public final class JiraApiConfigurationBuilder {

   private boolean useDefaultConfiguration;
   private String jiraUrl;
   private String jiraAgileBasePath;
   private String jiraWorklogBasePath;
   private String boardIdPlaceholder;
   private String sprintIdPlaceHh;
   private String startAtPlaceholder;
   private String defaultTicketName;
   private String ticketNamePattern;
   private String boardType;
   private Integer fetchBoardsBeginIndex;
   private Integer fetchResultSize;

   private JiraApiConfigurationBuilder() {
      this.fetchResultSize = 10;
   }

   /**
    * When calling this, for all properties are default values provided
    * Anyway you can override those defaults by providing any other value using these builders methods
    *
    * @return the {@link JiraApiConfigurationBuilder}
    */
   public JiraApiConfigurationBuilder withDefaultJiraApiConfiguration() {
      this.useDefaultConfiguration = true;
      return this;
   }

   public JiraApiConfigurationBuilder withJiraUrl(String jiraUrl) {
      this.jiraUrl = requireNotEmptyAndNotNull(jiraUrl);
      return this;
   }

   public JiraApiConfigurationBuilder withNullableJiraUrl(String jiraUrl) {
      this.jiraUrl = jiraUrl;
      return this;
   }

   public JiraApiConfigurationBuilder withJiraAgileBasePath(String jiraAgileBasePath) {
      this.jiraAgileBasePath = requireNotEmptyAndNotNull(jiraAgileBasePath);
      return this;
   }

   public JiraApiConfigurationBuilder withFetchResultSize(Integer fetchResultSize) {
      this.fetchResultSize = requireNonNull(fetchResultSize);
      return this;
   }

   public JiraApiConfigurationBuilder withNullableJiraAgileBasePath(String jiraAgileBasePath) {
      this.jiraAgileBasePath = jiraAgileBasePath;
      return this;
   }

   public JiraApiConfigurationBuilder withBoardIdPlaceholder(String boardIdPlaceHolder) {
      this.boardIdPlaceholder = requireNotEmptyAndNotNull(boardIdPlaceHolder);
      return this;
   }

   public JiraApiConfigurationBuilder withNullableJiraWorklogBasePath(String jiraWorklogBasePath) {
      this.jiraWorklogBasePath = jiraWorklogBasePath;
      return this;
   }

   public JiraApiConfigurationBuilder withSprintIdPlaceholder(String springIdPlaceHolder) {
      this.sprintIdPlaceHh = requireNotEmptyAndNotNull(springIdPlaceHolder);
      return this;
   }

   public JiraApiConfigurationBuilder withStartAtPlaceholder(String startAtPlaceholder) {
      this.startAtPlaceholder = requireNotEmptyAndNotNull(startAtPlaceholder);
      return this;
   }

   public JiraApiConfigurationBuilder withNullableTicketNamePattern(String ticketNamePattern) {
      this.ticketNamePattern = ticketNamePattern;
      return this;
   }

   public JiraApiConfigurationBuilder withNullableBoardType(String boardType) {
      this.boardType = boardType;
      return this;
   }

   public JiraApiConfigurationBuilder withNullableFetchBoardsBeginIndex(Integer fetchBoardsBeginIndex) {
      this.fetchBoardsBeginIndex = fetchBoardsBeginIndex;
      return this;
   }

   public JiraApiConfigurationBuilder withNullableDefaultTicketName(String defaultTicketName) {
      this.defaultTicketName = defaultTicketName;
      return this;
   }

   public JiraApiConfiguration build() {
      JiraApiConfiguration jiraApiConfiguration = new JiraApiConfiguration(jiraUrl, jiraAgileBasePath, jiraWorklogBasePath,
              boardIdPlaceholder, sprintIdPlaceHh, startAtPlaceholder, ticketNamePattern, defaultTicketName, boardType, fetchBoardsBeginIndex, fetchResultSize);
      if (useDefaultConfiguration) {
         JiraApiConfiguration defaultJiraApiConfiguration = JiraApiConfigurationFactory.createDefault();
         defaultJiraApiConfiguration.applyFromConfiguration(jiraApiConfiguration);
         return defaultJiraApiConfiguration;
      }
      return jiraApiConfiguration;
   }

   public static JiraApiConfigurationBuilder of() {
      return new JiraApiConfigurationBuilder();
   }
}
