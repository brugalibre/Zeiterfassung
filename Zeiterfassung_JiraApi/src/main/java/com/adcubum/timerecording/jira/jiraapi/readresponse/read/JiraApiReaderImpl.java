package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraResponseMapper;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraBoardsResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssueResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssuesResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic.JiraGenericValuesResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic.JiraGenericValuesResponse.GenericNameAttrs;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.BoardInfo.SprintInfo;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraBoardResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraGenericValuesResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraIssueResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraIssuesResponseReader;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.*;
import static java.util.Objects.requireNonNull;

public class JiraApiReaderImpl implements JiraApiReader {

   private static final Logger LOG = LoggerFactory.getLogger(JiraApiReaderImpl.class);
   private HttpClient httpClient;
   private JiraApiConfiguration jiraApiConfiguration;

   /**
    * Private constructor called by the {@link JiraApiReaderFactory}
    * 
    * @param jiraApiConfiguration
    *        the {@link JiraApiConfiguration}
    */
   private JiraApiReaderImpl(JiraApiConfiguration jiraApiConfiguration) {
      this(new HttpClient(), jiraApiConfiguration);
   }

   /**
    * Package private constructor for testing purpose only!
    * 
    * @param httpClient
    *        the {@link HttpClient}
    * @param jiraApiConfiguration
    *        the {@link JiraApiConfiguration}
    */
   JiraApiReaderImpl(HttpClient httpClient, JiraApiConfiguration jiraApiConfiguration) {
      this.httpClient = httpClient;
      this.jiraApiConfiguration = requireNonNull(jiraApiConfiguration);
      AuthenticationService.INSTANCE.registerUserAuthenticatedObservable(this);
   }

   @Override
   public void applyJiraApiConfiguration(JiraApiConfiguration jiraApiConfiguration) {
      requireNonNull(jiraApiConfiguration);
      this.jiraApiConfiguration.applyFromConfiguration(jiraApiConfiguration);
   }

   @Override
   public void userAuthenticated(AuthenticationContext authenticationContext) {
      httpClient.setCredentials(authenticationContext.getUsername(), String.valueOf(authenticationContext.getUserPw()));
   }

   @Override
   public Optional<Ticket> readTicket4Nr(String ticketNr) {
      LOG.info("Try to read ticket for ticket-nr '{}'", ticketNr);
      String url = jiraApiConfiguration.getGetIssueUrl() + ticketNr;
      JiraIssueResponse jiraIssueResponse = httpClient.callRequestAndParse(new JiraIssueResponseReader(), url);
      LOG.info("Read was successfully: {}", (jiraIssueResponse.isSuccess() ? "yes" : "no"));
      return JiraResponseMapper.INSTANCE.map2Ticket(jiraIssueResponse);
   }

   @Override
   public JiraApiReadTicketsResult readTicketsFromBoardAndSprints(String boardName, List<String> sprintNames) {
      LOG.info("Try to read the tickets from the current sprint from board '{}'", boardName);
      BoardInfo boardInfo = evalActiveSprints4BoardName(boardName);
      if (boardInfo.getSprintInfos().isEmpty() && boardInfo.isScrumBoard()) {
         return failedResult(boardInfo); // A scrum-board without any active nor futur sprints -> error
      }
      JiraIssuesResponse activeSprintIssues = createUrlAndReadScrumIssuesFromJira(boardInfo);
      readAndApplyFutureSprintTickets(activeSprintIssues, boardInfo, sprintNames);
      return JiraResponseMapper.INSTANCE.map2TicketResult(activeSprintIssues);
   }

   private void readAndApplyFutureSprintTickets(JiraIssuesResponse activeSprintIssues, BoardInfo boardInfo, List<String> sprintNames) {
      if (boardInfo.isKanbanBoard()) {
         return;
      }
      LOG.info("Try to read the tickets for the future sprints '{}'", (sprintNames.isEmpty() ? "all" : sprintNames));
      getFutureSprintInfos(boardInfo.getBoardId())
            .stream()
            .filter(isRelevantSprint(sprintNames))
            .map(this::createUrlAndReadIssuesFromJira)
            .forEach(activeSprintIssues::applyFromOther);
   }

   private static Predicate<SprintInfo> isRelevantSprint(List<String> sprintNames) {
      return sprintInfo -> sprintNames.contains(sprintInfo.sprintName);
   }

   private BoardInfo evalActiveSprints4BoardName(String boardName) {
      BoardInfo boardInfo = getBoardInfo(boardName);
      if (boardInfo.isKanbanBoard()) {
         return boardInfo;
      }
      List<SprintInfo> sprintInfos = getActiveSprintInfos(boardInfo.getBoardId());
      logResult(sprintInfos, boardName);
      boardInfo.setSprintInfos(sprintInfos);
      return boardInfo;
   }

   /*
    * Da jira jeweils nur 50 Resultate auf einmal zurückliefert, kann es nötig sein zu fetchen. Der Parameter 'maxResults' wirkt nur einschränkend,
    * kann aber die Grenze von 50 nicht umgehen. Ab dem 8-mal ist aber dann trotzdem schluss
    */
   private BoardInfo getBoardInfo(String boardName) {
      BoardInfo boardInfo;
      int index = jiraApiConfiguration.getFetchBoardsBeginIndex();
      do {
         LOG.info("Trying to get the board id from the search results from start index {} to end index {}", index, (index + JIRA_MAX_RESULTS_RETURNED));
         boardInfo = getBoardInfo4Name(boardName, String.valueOf(index));
         index = index + JIRA_MAX_RESULTS_RETURNED;
      } while (BoardInfo.isUnknown(boardInfo.getBoardId()) && index < MAX_RESULTS);
      LOG.info("Got board id {} for board name '{}'",  boardInfo.getBoardId(), boardName);
      return boardInfo;
   }

   private List<SprintInfo> getFutureSprintInfos(String boardId) {
      String getFutureSprintIdUrl = jiraApiConfiguration.getGetFuturSprintIdsForBoardUrl().replace(BOARD_ID_PLACE_HOLDER, boardId);
      JiraGenericValuesResponse jiraGetSprintIdResponse = httpClient.callRequestAndParse(new JiraGenericValuesResponseReader(), getFutureSprintIdUrl);
      return jiraGetSprintIdResponse.getValues()
            .stream()
            .map(buildSprintInfo(boardId))
            .collect(Collectors.toList());
   }

   private List<SprintInfo> getActiveSprintInfos(String boardId) {
      String getActiveSprintIdUrl = jiraApiConfiguration.getGetActiveSprintIdsForBoardUrl().replace(BOARD_ID_PLACE_HOLDER, boardId);
      JiraGenericValuesResponse jiraGetSprintIdResponse = httpClient.callRequestAndParse(new JiraGenericValuesResponseReader(), getActiveSprintIdUrl);
      return jiraGetSprintIdResponse.getValues()
            .stream()
            .map(buildSprintInfo(boardId))
            .collect(Collectors.toList());
   }

   private BoardInfo getBoardInfo4Name(String boardName, String startAt) {
      String allBoardsUrl = jiraApiConfiguration.getGetAllBoardUrls().replace(START_AT_PLACE_HOLDER, startAt);
      JiraBoardsResponse jiraGetBoardsResponse =
            httpClient.callRequestAndParse(new JiraBoardResponseReader(), allBoardsUrl);
      return jiraGetBoardsResponse.getValues()
            .stream()
            .filter(jiraBoard ->  boardName.equals(jiraBoard.getName()))
            .findFirst()
            .map(BoardInfo::of)
            .orElse(BoardInfo.ofUnknown(boardName));
   }

   private JiraIssuesResponse createUrlAndReadKanbanIssuesFromJira(BoardInfo boardInfo) {
      String url = createGetKanbanIssues4BoardUrl(boardInfo.getBoardId());
      return readIssuesFromJira(url, 0, null);
   }

   /*
    * Reads all the issues from the given board and sprint, if it is a scrum-board.
    * So we have to fetch multiple times to get all issues
    */
   private JiraIssuesResponse createUrlAndReadScrumIssuesFromJira(BoardInfo boardInfo) {
      if (boardInfo.isKanbanBoard()) {
         return createUrlAndReadKanbanIssuesFromJira(boardInfo);
      }
      List<SprintInfo> sprintInfos = boardInfo.getSprintInfos();
      return sprintInfos.stream()
            .map(this::createUrlAndReadIssuesFromJira)
            .collect(Collectors.collectingAndThen(Collectors.toList(), apply2InitialResponse()));
   }

   private Function<List<JiraIssuesResponse>, JiraIssuesResponse> apply2InitialResponse() {
      return jiraIssuesResponses -> {
         JiraIssuesResponse initResponse = new JiraIssuesResponse();
         jiraIssuesResponses.forEach(initResponse::applyFromOther);
         return initResponse;
      };
   }

   /*
    * Reads all the issues from the given board and sprint. Because we have to filter all subtask it's possible that we recieve more than 50 issues.
    * So we have to fetch a second time to get the other issues
    */
   private JiraIssuesResponse createUrlAndReadIssuesFromJira(SprintInfo sprintInfo) {
      String url = createGetScrumIssues4BoardUrl(sprintInfo.boardId, sprintInfo.sprintId);
      LOG.info("Trying to get issues for sprint '{}' ('{}')" , sprintInfo.sprintId, sprintInfo.sprintName);
      return readIssuesFromJira(url, 0, null);
   }

   private String createGetScrumIssues4BoardUrl(String boardId, String sprintId) {
      return jiraApiConfiguration.getGetIssues4SprintBoardIdUrl()
              .replace(BOARD_ID_PLACE_HOLDER, boardId)
              .replace(SPRINT_ID_PLACE_HOLDER, sprintId);
   }

   private String createGetKanbanIssues4BoardUrl(String boardId) {
      return jiraApiConfiguration.getGetIssues4KanbanBoardIdUrl()
              .replace(BOARD_ID_PLACE_HOLDER, boardId);
   }

   private JiraIssuesResponse readIssuesFromJira(String urlWithPlaceholder, int startIndex, JiraIssuesResponse otherParsedIssuesFromJira) {
      String url = urlWithPlaceholder.replace(START_AT_PLACE_HOLDER, String.valueOf(startIndex));
      JiraIssuesResponse parsedIssuesFromJira = httpClient.callRequestAndParse(new JiraIssuesResponseReader(), url);
      int endIndex = startIndex + JIRA_MAX_RESULTS_RETURNED;
      LOG.info("Read {} jira issues from start index {} to end index {}", parsedIssuesFromJira.getIssues().size(), startIndex, endIndex);
      if (parsedIssuesFromJira.getTotal() > endIndex) {
         parsedIssuesFromJira.applyFromOther(otherParsedIssuesFromJira);
         return readIssuesFromJira(urlWithPlaceholder, endIndex + 1, parsedIssuesFromJira);
      }
      parsedIssuesFromJira.applyFromOther(otherParsedIssuesFromJira);
      logResult(parsedIssuesFromJira);
      return parsedIssuesFromJira;
   }

   private static void logResult(JiraIssuesResponse parsedIssuesFromJira) {
      int amountOfNotSubtasks = parsedIssuesFromJira.getIssuesNotSubtask().size();
      int totalAmount = parsedIssuesFromJira.getIssues().size();
      LOG.info("Read {} jira issues ({} issues and {} subtasks)", totalAmount, amountOfNotSubtasks, (totalAmount - amountOfNotSubtasks));
   }

   private void logResult(List<SprintInfo> sprintInfos, String boardName) {
      if (sprintInfos.isEmpty()) {
         LOG.warn("No active sprints found for board name '{}'", boardName);
      }
      for (SprintInfo sprintInfo : sprintInfos) {
         LOG.info("Got sprint id '{}' ('{}') for board name '{}'", sprintInfo.sprintId, sprintInfo.sprintName, boardName);
      }
   }

   private JiraApiReadTicketsResult failedResult(BoardInfo boardInfo) {
      LOG.warn("Unable to read tickets from board '{}'" + " (id='{}', sprint-ids={})", boardInfo.getBoardName(), boardInfo.getBoardId(), BoardInfo.UNKNOWN);
      return JiraApiReadTicketsResult.failed();
   }

   private Function<GenericNameAttrs, SprintInfo> buildSprintInfo(String boardId) {
      return genericNameAttrs -> new SprintInfo(boardId, genericNameAttrs);
   }
}
