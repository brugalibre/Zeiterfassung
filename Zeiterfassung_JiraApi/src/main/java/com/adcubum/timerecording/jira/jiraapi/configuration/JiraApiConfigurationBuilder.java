package com.adcubum.timerecording.jira.jiraapi.configuration;

import static com.adcubum.util.utils.StringUtil.requireNotEmptyAndNotNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public final class JiraApiConfigurationBuilder {

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

   public JiraApiConfigurationBuilder withJiraUrl(String jiraUrl) {
      this.jiraUrl = requireNotEmptyAndNotNull(jiraUrl);
      return this;
   }

   public JiraApiConfigurationBuilder withNullableJiraUrl(String jiraUrl) {
      if (nonNull(jiraUrl)) {
         this.jiraUrl = jiraUrl;
      }
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

   /**
    * Sets the given value only if it is not null
    *
    * @param jiraAgileBasePath the base url for the jira agil api
    * @return this {@link JiraApiConfigurationBuilder}
    */
   public JiraApiConfigurationBuilder withNullableJiraAgileBasePath(String jiraAgileBasePath) {
      if (nonNull(jiraAgileBasePath)) {
         this.jiraAgileBasePath = jiraAgileBasePath;
      }
      return this;
   }

   public JiraApiConfigurationBuilder withBoardIdPlaceholder(String boardIdPlaceHolder) {
      this.boardIdPlaceholder = requireNotEmptyAndNotNull(boardIdPlaceHolder);
      return this;
   }

   /**
    * Sets the given value only if it is not null
    *
    * @param jiraWorklogBasePath the base path for the jira worklog api
    * @return this {@link JiraApiConfigurationBuilder}
    */
   public JiraApiConfigurationBuilder withNullableJiraWorklogBasePath(String jiraWorklogBasePath) {
      if (nonNull(jiraWorklogBasePath)) {
         this.jiraWorklogBasePath = jiraWorklogBasePath;
      }
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

   /**
    * Sets the given value only if it is not null
    *
    * @param ticketNamePattern the ticket-name-pattern to set
    * @return this {@link JiraApiConfigurationBuilder}
    */
   public JiraApiConfigurationBuilder withNullableTicketNamePattern(String ticketNamePattern) {
      if (nonNull(ticketNamePattern)) {
         this.ticketNamePattern = ticketNamePattern;
      }
      return this;
   }

   /**
    * Sets the given value only if it is not null
    *
    * @param boardType the board-type to set
    * @return this {@link JiraApiConfigurationBuilder}
    */
   public JiraApiConfigurationBuilder withNullableBoardType(String boardType) {
      if (nonNull(boardType)) {
         this.boardType = boardType;
      }
      return this;
   }

   /**
    * Sets the given value only if it is not null
    *
    * @param fetchBoardsBeginIndex the index to begin to fetch boards
    * @return this {@link JiraApiConfigurationBuilder}
    */
   public JiraApiConfigurationBuilder withNullableFetchBoardsBeginIndex(Integer fetchBoardsBeginIndex) {
      if (nonNull(fetchBoardsBeginIndex)) {
         this.fetchBoardsBeginIndex = fetchBoardsBeginIndex;
      }
      return this;
   }

   /**
    * Sets the given value only if it is not null
    *
    * @param defaultTicketName the default name pattern for tickets
    * @return this {@link JiraApiConfigurationBuilder}
    */
   public JiraApiConfigurationBuilder withNullableDefaultTicketName(String defaultTicketName) {
      if (nonNull(defaultTicketName)) {
         this.defaultTicketName = defaultTicketName;
      }
      return this;
   }

   public JiraApiConfiguration build() {
      return new JiraApiConfiguration(jiraUrl, jiraAgileBasePath, jiraWorklogBasePath,
              boardIdPlaceholder, sprintIdPlaceHh, startAtPlaceholder, ticketNamePattern, defaultTicketName, boardType, fetchBoardsBeginIndex, fetchResultSize);
   }

   public static JiraApiConfigurationBuilder of() {
      return new JiraApiConfigurationBuilder();
   }
}
