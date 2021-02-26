package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read;

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
}
