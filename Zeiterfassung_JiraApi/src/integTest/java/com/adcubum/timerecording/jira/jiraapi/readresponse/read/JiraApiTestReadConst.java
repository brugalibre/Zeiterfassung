package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

public class JiraApiTestReadConst {

   private JiraApiTestReadConst() {
      // private 
   }

   /** The name of our Test Board */
   public static final String BOARD_NAME = "MyLovelyBoardName";
   /** The id of our Test Board */
   public static final String BOARD_ID_FOR_NAME = "100";
   /** The id of our sprint */
   public static final String SPRINT_ID_FOR_BOARD = "1234";
   /** The nr of the received sprint issue */
   public static final String ISSUE_NR = "PROJECT-123";

   /** One of the two active sprint ids of our board */
   public static final String ACTIVE_SPRINT_ID_1_FOR_BOARD = "11120";
   /** The other of the two active sprint ids of our board */
   public static final String ACTIVE_SPRINT_ID_2_FOR_BOARD = "11121";
   /** The nr of the received sprint issue from our active sprint 1 */
   public static final String ISSUE_NR_FROM_ACTIVE_SPRINT_1 = "PROJECT-987";
   /** The nr of the received sprint issue from our active sprint 2 */
   public static final String ISSUE_NR_FROM_ACTIVE_SPRINT_2 = "PROJECT-789";

   public static final String READ_BOARD_SUCCESSFULL_RESPONSE = "{\r\n"
         + "   \"maxResults\": 50,\r\n"
         + "   \"startAt\": 0,\r\n"
         + "   \"total\": 2,\r\n"
         + "   \"isLast\": false,\r\n"
         + "   \"values\": [{\r\n"
         + "         \"id\": 101,\r\n"
         + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/board/101\",\r\n"
         + "         \"name\": \"Board 1\",\r\n"
         + "         \"type\": \"scrum\"\r\n"
         + "      }, {\r\n"
         + "         \"id\": " + BOARD_ID_FOR_NAME + ",\r\n"
         + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/board/100\",\r\n"
         + "         \"name\": \"" + BOARD_NAME + "\",\r\n"
         + "         \"type\": \"scrum\"\r\n"
         + "      }\r\n"
         + "   ]\r\n"
         + "}\r\n"
         + "";
   public static final String READ_KANBAN_BOARD_SUCCESSFULL_RESPONSE = "{\r\n"
         + "   \"maxResults\": 50,\r\n"
         + "   \"startAt\": 0,\r\n"
         + "   \"total\": 2,\r\n"
         + "   \"isLast\": false,\r\n"
         + "   \"values\": [{\r\n"
         + "         \"id\": 101,\r\n"
         + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/board/101\",\r\n"
         + "         \"name\": \"Board 1\",\r\n"
         + "         \"type\": \"kanban\"\r\n"
         + "      }, {\r\n"
         + "         \"id\": " + BOARD_ID_FOR_NAME + ",\r\n"
         + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/board/100\",\r\n"
         + "         \"name\": \"" + BOARD_NAME + "\",\r\n"
         + "         \"type\": \"kanban\"\r\n"
         + "      }\r\n"
         + "   ]\r\n"
         + "}\r\n"
         + "";

   public static final String READ_SPRINT_ID_RESPONSE = "{\r\n"
         + "   \"maxResults\": 50,\r\n"
         + "   \"startAt\": 0,\r\n"
         + "   \"isLast\": true,\r\n"
         + "   \"values\": [{\r\n"
         + "         \"id\": " + SPRINT_ID_FOR_BOARD + ",\r\n"
         + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/sprint/10551\",\r\n"
         + "         \"state\": \"active\",\r\n"
         + "         \"name\": \"Blubb 21-05 / 18.03.\",\r\n"
         + "         \"startDate\": \"2021-03-04T10:33:51.863+01:00\",\r\n"
         + "         \"endDate\": \"2021-03-18T10:33:00.000+01:00\",\r\n"
         + "         \"originBoardId\": 84,\r\n"
         + "         \"goal\": \"\"\r\n"
         + "      }\r\n"
         + "   ]\r\n"
         + "}\r\n"
         + "";

   public static final String READ_SPRINT_ISSUES_RESPONSE = "{\r\n"
         + "   \"expand\": \"schema,names\",\r\n"
         + "   \"startAt\": 0,\r\n"
         + "   \"maxResults\": 50,\r\n"
         + "   \"total\": 1,\r\n"
         + "   \"issues\": [{\r\n"
         + "         \"expand\": \"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\r\n"
         + "         \"id\": \"" + SPRINT_ID_FOR_BOARD + "\",\r\n"
         + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/issue/1234\",\r\n"
         + "         \"key\": \"" + ISSUE_NR + "\",\r\n"
         + "         \"fields\": {\r\n"
         + "         \r\n"
         + "            \r\n"
         + "         }\r\n"
         + "      }\r\n"
         + "   ]\r\n"
         + "}";
   public static final String READ_KANBAN_ISSUES_RESPONSE = "{\r\n"
           + "   \"expand\": \"schema,names\",\r\n"
           + "   \"startAt\": 0,\r\n"
           + "   \"maxResults\": 50,\r\n"
           + "   \"total\": 1,\r\n"
           + "   \"issues\": [{\r\n"
           + "         \"expand\": \"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\r\n"
           + "         \"id\": \"" + ACTIVE_SPRINT_ID_1_FOR_BOARD + "\",\r\n"
           + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/issue/1234\",\r\n"
           + "         \"key\": \"" + ISSUE_NR_FROM_ACTIVE_SPRINT_1 + "\",\r\n"
           + "         \"fields\": {\r\n"
           + "         \r\n"
           + "            \r\n"
           + "         }\r\n"
           + "      }\r\n"
           + "   ]\r\n"
           + "}";

   public static final String READ_SPRINT_ID_RESPONSE_TWO_ACTIVE_SPRINTS = "{\r\n"
         + "   \"maxResults\": 50,\r\n"
         + "   \"startAt\": 0,\r\n"
         + "   \"isLast\": true,\r\n"
         + "   \"values\": [\r\n"
         + "      {\r\n"
         + "         \"id\": " + ACTIVE_SPRINT_ID_1_FOR_BOARD + ",\r\n"
         + "         \"self\": \"https://jira.adcubum.com/rest/agile/1.0/sprint/11120\",\r\n"
         + "         \"state\": \"active\",\r\n"
         + "         \"name\": \"COH Gin 21-15+16 / 18.08.\",\r\n"
         + "         \"startDate\": \"2021-07-21T11:31:48.317+02:00\",\r\n"
         + "         \"endDate\": \"2021-08-18T11:31:00.000+02:00\",\r\n"
         + "         \"originBoardId\": 84,\r\n"
         + "         \"goal\": \"HeyHo-Gin\"\r\n"
         + "      },\r\n"
         + "      {\r\n"
         + "         \"id\": " + ACTIVE_SPRINT_ID_2_FOR_BOARD + ",\r\n"
         + "         \"self\": \"https://jira.adcubum.com/rest/agile/1.0/sprint/11120\",\r\n"
         + "         \"state\": \"active\",\r\n"
         + "         \"name\": \"COH Tonic 21-15+16 / 18.08.\",\r\n"
         + "         \"startDate\": \"2021-07-21T11:31:48.317+02:00\",\r\n"
         + "         \"endDate\": \"2021-08-18T11:31:00.000+02:00\",\r\n"
         + "         \"originBoardId\": 84,\r\n"
         + "         \"goal\": \"HeyHo-Tonic\"\r\n"
         + "      }\r\n"
         + "   ]\r\n"
         + "}";

   public static final String READ_SPRINT_ISSUES_FOR_ACTIVE_SPRINT_1_RESPONSE = "{\r\n"
         + "   \"expand\": \"schema,names\",\r\n"
         + "   \"startAt\": 0,\r\n"
         + "   \"maxResults\": 50,\r\n"
         + "   \"total\": 1,\r\n"
         + "   \"issues\": [{\r\n"
         + "         \"expand\": \"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\r\n"
         + "         \"id\": \"" + ACTIVE_SPRINT_ID_1_FOR_BOARD + "\",\r\n"
         + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/issue/1234\",\r\n"
         + "         \"key\": \"" + ISSUE_NR_FROM_ACTIVE_SPRINT_1 + "\",\r\n"
         + "         \"fields\": {\r\n"
         + "         \r\n"
         + "            \r\n"
         + "         }\r\n"
         + "      }\r\n"
         + "   ]\r\n"
         + "}";

   public static final String READ_SPRINT_ISSUES_FOR_ACTIVE_SPRINT_2_RESPONSE = "{\r\n"
         + "   \"expand\": \"schema,names\",\r\n"
         + "   \"startAt\": 0,\r\n"
         + "   \"maxResults\": 50,\r\n"
         + "   \"total\": 1,\r\n"
         + "   \"issues\": [{\r\n"
         + "         \"expand\": \"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\r\n"
         + "         \"id\": \"" + ACTIVE_SPRINT_ID_2_FOR_BOARD + "\",\r\n"
         + "         \"self\": \"https://jira.company.com/restApi/agile/1.5/issue/1234\",\r\n"
         + "         \"key\": \"" + ISSUE_NR_FROM_ACTIVE_SPRINT_2 + "\",\r\n"
         + "         \"fields\": {\r\n"
         + "         \r\n"
         + "            \r\n"
         + "         }\r\n"
         + "      }\r\n"
         + "   ]\r\n"
         + "}";
}
