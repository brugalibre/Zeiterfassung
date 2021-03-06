package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.BOARD_ID_PLACE_HOLDER;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.GET_ACTIVE_SPRINT_ID_FOR_BOARD_URL;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.GET_ALL_BOARDS_URL;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.GET_FUTURE_SPRINT_IDS_FOR_BOARD_URL;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.GET_ISSUES_4_BOARD_URL;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.JIRA_MAX_RESULTS_RETURNED;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.MAX_RESULTS;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.SPRINT_ID_PLACE_HOLDER;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.START_AT_PLACE_HOLDER;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.START_AT_PLACE_LITERAL;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraResponseMapper;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraGenericValuesResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraGenericValuesResponse.GenericNameAttrs;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssueResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssuesResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraGenericValuesResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraIssueResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraIssuesResponseReader;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;


public class JiraApiReaderImpl implements JiraApiReader {

   private static final Logger LOG = Logger.getLogger(JiraApiReaderImpl.class);
   private HttpClient httpClient;

   /**
    * Package private constructor used by spring
    */
   JiraApiReaderImpl() {
      this(new HttpClient());
   }

   /**
    * Constructor for testing purpose only!
    */
   JiraApiReaderImpl(HttpClient httpClient) {
      this.httpClient = httpClient;
      AuthenticationService.INSTANCE.registerUserAuthenticatedObservable(this);
   }

   @Override
   public void userAuthenticated(AuthenticationContext authenticationContext) {
      httpClient.setCredentials(authenticationContext.getUsername(), String.valueOf(authenticationContext.getUserPw()));
   }

   @Override
   public Optional<Ticket> readTicket4Nr(String ticketNr) {
      LOG.info("Try to read ticket for ticket-nr '" + ticketNr + "'");
      String url = JiraApiConstants.GET_ISSUE_URL + ticketNr;
      JiraIssueResponse jiraIssueResponse = httpClient.callRequestAndParse(new JiraIssueResponseReader(), url);
      LOG.info("Read successfully ? " + (jiraIssueResponse.isSuccess() ? "yes" : "no"));
      return JiraResponseMapper.INSTANCE.map2Ticket(jiraIssueResponse);
   }

   @Override
   public JiraApiReadTicketsResult readTicketsFromBoardAndSprints(String boardName, List<String> sprintNames) {
      LOG.info("Try to read the tickets from the current sprint from board '" + boardName + "'");
      SprintInfo sprintInfo = evalActiveSprint4BoardName(boardName);
      if (sprintInfo.isUnknownSprintId()) {
         return failedResult(sprintInfo, boardName);
      }
      JiraIssuesResponse activeSprintIssues = createUrlAndReadIssuesFromJira(sprintInfo);
      readAndApplyFutureSprintTickets(activeSprintIssues, sprintInfo.boardId, sprintNames);
      return JiraResponseMapper.INSTANCE.map2TicketResult(activeSprintIssues);
   }

   private void readAndApplyFutureSprintTickets(JiraIssuesResponse activeSprintIssues, String boardId, List<String> sprintNames) {
      LOG.info("Try to read the tickets for the future sprints '" + (sprintNames.isEmpty() ? "all" : sprintNames.toString() + "'"));
      getFutureSprintInfos(boardId)
            .stream()
            .filter(isRelevantSprint(sprintNames))
            .map(this::createUrlAndReadIssuesFromJira)
            .map(JiraIssuesResponse::filterDoneTasks)
            .forEach(activeSprintIssues::applyFromOther);
   }

   private static Predicate<SprintInfo> isRelevantSprint(List<String> sprintNames) {
      return sprintInfo -> sprintNames.contains(sprintInfo.sprintName);
   }

   private SprintInfo evalActiveSprint4BoardName(String boardName) {
      String boardId = getBoardId(boardName);
      SprintInfo sprintInfo = getActiveSprintInfo(boardId);
      LOG.info("Got sprint id " + sprintInfo.sprintId + " (" + sprintInfo.sprintName + ") for board name '" + boardName + "'");
      return sprintInfo;
   }

   /*
    * Da jira jeweils nur 50 Resultate auf einmal zurück liefert, kann es nötig sein zu fetchen. Der Parameter 'maxResults' wirkt nur einschränkend,
    * kann aber die Grenze von 50 nicht umgehen. Ab dem 8 mal ist aber dann trotzdem schluss
    */
   private String getBoardId(String boardName) {
      String boardId = "";
      int index = 0;
      do {
         LOG.info("Trying to get the board id from the search results within range " + index + " to " + (index + JIRA_MAX_RESULTS_RETURNED));
         boardId = getBoardId4Name(boardName, String.valueOf(index));
         index = index + JIRA_MAX_RESULTS_RETURNED;
      } while (SprintInfo.isUnknown(boardId) && index < MAX_RESULTS);
      LOG.info("Got board id " + boardId + " for board name '" + boardName + "'");
      return boardId;
   }

   private List<SprintInfo> getFutureSprintInfos(String boardId) {
      String getFutureSprintIdUrl = GET_FUTURE_SPRINT_IDS_FOR_BOARD_URL.replace(BOARD_ID_PLACE_HOLDER, boardId);
      JiraGenericValuesResponse jiraGetSprintIdResponse = httpClient.callRequestAndParse(new JiraGenericValuesResponseReader(), getFutureSprintIdUrl);
      return jiraGetSprintIdResponse.getValues()
            .stream()
            .map(buildSprintInfo(boardId))
            .collect(Collectors.toList());
   }

   private SprintInfo getActiveSprintInfo(String boardId) {
      String getActiveSprintIdUrl = GET_ACTIVE_SPRINT_ID_FOR_BOARD_URL.replace(BOARD_ID_PLACE_HOLDER, boardId);
      JiraGenericValuesResponse jiraGetSprintIdResponse = httpClient.callRequestAndParse(new JiraGenericValuesResponseReader(), getActiveSprintIdUrl);
      return jiraGetSprintIdResponse.getValues()
            .stream()
            .map(buildSprintInfo(boardId))
            .findFirst()
            .orElseGet(SprintInfo::unknown);
   }

   private String getBoardId4Name(String boardName, String startAt) {
      String getAllBoardsUrl = GET_ALL_BOARDS_URL.replace(START_AT_PLACE_HOLDER, startAt);
      JiraGenericValuesResponse jiraGetBoardsResponse =
            httpClient.callRequestAndParse(new JiraGenericValuesResponseReader(), getAllBoardsUrl);
      return jiraGetBoardsResponse.getValues()
            .stream()
            .filter(jiraBoard -> boardName.equals(jiraBoard.getName()))
            .map(GenericNameAttrs::getId)
            .findFirst()
            .orElse(SprintInfo.UNKNOWN);
   }

   /*
    * Reads all the issues from the given board and sprint. Because we have to filter all subtask it's possible that we recieve more than 50 issues.
    * So we have to fetch a second time to get the other issues
    */
   private JiraIssuesResponse createUrlAndReadIssuesFromJira(SprintInfo sprintInfo) {
      String url = createGetIssues4BoardUrl(sprintInfo);
      LOG.info("Trying to get issues for sprint '" + sprintInfo.sprintId + "' (" + sprintInfo.sprintName + ")");
      return readIssuesFromJira(url);
   }

   private static String createGetIssues4BoardUrl(SprintInfo sprintInfo) {
      return GET_ISSUES_4_BOARD_URL
            .replace(BOARD_ID_PLACE_HOLDER, sprintInfo.boardId)
            .replace(SPRINT_ID_PLACE_HOLDER, sprintInfo.sprintId)
            .replace(START_AT_PLACE_HOLDER, "0"); // lets start at the begining
   }

   private JiraIssuesResponse readIssuesFromJira(String url) {
      JiraIssuesResponse parsedIssuesFromJira = httpClient.callRequestAndParse(new JiraIssuesResponseReader(), url);
      if (parsedIssuesFromJira.hasMaxResults()) {
         url = url.replace(START_AT_PLACE_LITERAL + "=0", START_AT_PLACE_LITERAL + "=" + JIRA_MAX_RESULTS_RETURNED);// Das isch wie nöd eso schöö...
         JiraIssuesResponse otherParsedIssuesFromJira = httpClient.callRequestAndParse(new JiraIssuesResponseReader(), url);
         parsedIssuesFromJira.applyFromOther(otherParsedIssuesFromJira);
      }
      logResult(parsedIssuesFromJira);
      return parsedIssuesFromJira;
   }

   private static void logResult(JiraIssuesResponse parsedIssuesFromJira) {
      int amountOfNotSubtasks = parsedIssuesFromJira.getIssuesNotSubtask().size();
      int totalAmount = parsedIssuesFromJira.getIssues().size();
      LOG.info("Read " + totalAmount + " jira issues (" + amountOfNotSubtasks + " issues and " + (totalAmount - amountOfNotSubtasks)
            + " subtasks)");
   }

   private JiraApiReadTicketsResult failedResult(SprintInfo sprintInfo, String boardName) {
      LOG.warn("Unable to read tickets from board '" + boardName +
            "' (id=" + sprintInfo.boardId + ", sprint-id=" + sprintInfo.sprintId + ")");
      return JiraApiReadTicketsResult.failed();
   }

   private Function<GenericNameAttrs, SprintInfo> buildSprintInfo(String boardId) {
      return genericNameAttrs -> new SprintInfo(boardId, genericNameAttrs);
   }

   static class SprintInfo {
      static final String UNKNOWN = "'unknown'";
      private String boardId;
      private String sprintId;
      private String sprintName;

      private SprintInfo(String boardId, GenericNameAttrs genericNameAttrs) {
         this(boardId, genericNameAttrs.getId(), genericNameAttrs.getName());
      }

      private static SprintInfo unknown() {
         return new SprintInfo(UNKNOWN, UNKNOWN, UNKNOWN);
      }

      private SprintInfo(String boardId, String sprintId, String sprintName) {
         this.boardId = boardId;
         this.sprintId = sprintId;
         this.sprintName = sprintName;
      }

      private boolean isUnknownSprintId() {
         return isUnknown(sprintId);
      }

      private static boolean isUnknown(String boardId) {
         return UNKNOWN.equals(boardId);
      }
   }
}
