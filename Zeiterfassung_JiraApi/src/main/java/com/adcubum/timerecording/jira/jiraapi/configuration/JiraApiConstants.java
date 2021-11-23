package com.adcubum.timerecording.jira.jiraapi.configuration;

public class JiraApiConstants {

   /** Key which stores the jira base url in the property file */
   public static final String JIRA_BASE_URL_KEY = "https://jira.adcubum.com";

   /** Base path for the jira agile api */
   public static final String JIRA_AGILE_BASE_PATH = "rest/agile/latest/";

   /** Placeholder which is to replace with an actual board id */
   public static final String BOARD_ID_PLACE_HOLDER = "$boardId";

   /** Placeholder which is to replace with an actual sprint id */
   public static final String SPRINT_ID_PLACE_HOLDER = "$sprintId";

   /** The string literal for 'startAt' */
   public static final String START_AT_PLACE_LITERAL = "startAt";

   /** Placeholder which is to replace with an index to start looking for boards */
   public static final String START_AT_PLACE_HOLDER = "$" + START_AT_PLACE_LITERAL;

   /** The maximal amount of results which can be provided by jira at once */
   public static final int JIRA_MAX_RESULTS_RETURNED = 50;

   /** This is the maximal amount of results we get from jira (with fetching) while looking for the board id. After that, we give up.. */
   public static final int MAX_RESULTS = 400;

   private JiraApiConstants() {
      // private
   }
}
