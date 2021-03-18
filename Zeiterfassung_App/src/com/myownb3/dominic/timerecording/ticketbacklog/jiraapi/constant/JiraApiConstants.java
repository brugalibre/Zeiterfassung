package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant;

public class JiraApiConstants {

   /** ADC jira host */
   public static final String ADC_JIRA_HOST = "https://jira.adcubum.com";

   /** Base path for the jira agile api */
   public static final String ADC_JIRA_AGILE_BASE_PATH = "rest/agile/latest/";

   /** Base url to adc jira */
   public static final String ADC_JIRA_BASE_URL = ADC_JIRA_HOST + "/" + ADC_JIRA_AGILE_BASE_PATH;

   /** Url for the jira boards */
   public static final String ADC_JIRA_BOARD_BASE_URL = ADC_JIRA_BASE_URL + "board";

   /** Place holder which is to replace with an actual board id */
   public static final String BOARD_ID_PLACE_HOLDER = "$boardId";

   /** Place holder which is to replace with an actual sprint id */
   public static final String SPRINT_ID_PLACE_HOLDER = "$sprintId";

   /** The string literal for 'startAt' */
   public static final String START_AT_PLACE_LITERAL = "startAt";

   /** Place holder which is to replace with an index to start looking for boards */
   public static final String START_AT_PLACE_HOLDER = "$" + START_AT_PLACE_LITERAL;

   /** URL for retrieving all issues for a specific board id */
   public static final String GET_ISSUES_4_BOARD_URL = ADC_JIRA_BOARD_BASE_URL + "/" + BOARD_ID_PLACE_HOLDER + "/sprint/"
         + SPRINT_ID_PLACE_HOLDER + "/issue?startAt=" + START_AT_PLACE_HOLDER;

   /** URL for retrieving the sprint id for an active sprint and a given board id */
   public static final String GET_ACTIVE_SPRINT_ID_FOR_BOARD_URL = ADC_JIRA_BOARD_BASE_URL + "/" + BOARD_ID_PLACE_HOLDER + "/sprint?state=active";

   /** URL for retrieving all issues for a specific board id */
   public static final String GET_FUTURE_SPRINT_IDS_FOR_BOARD_URL = ADC_JIRA_BOARD_BASE_URL + "/" + BOARD_ID_PLACE_HOLDER + "/sprint?state=future";

   /** URL for retrieving all boards */
   public static final String GET_ALL_BOARDS_URL = ADC_JIRA_BASE_URL + "board?type=scrum&startAt=" + START_AT_PLACE_HOLDER;

   /** URL for retrieving details for an issue */
   public static final String GET_ISSUE_URL = ADC_JIRA_BASE_URL + "issue/";

   /** The maximal amount of results which can be provided by jira at once */
   public static final int JIRA_MAX_RESULTS_RETURNED = 50;

   /** This is the maximal amount of results we get from jiar (with fetching) while looking for the board id. After that, we give up.. */
   public static final int MAX_RESULTS = 400;

   private JiraApiConstants() {
      // private
   }

}
