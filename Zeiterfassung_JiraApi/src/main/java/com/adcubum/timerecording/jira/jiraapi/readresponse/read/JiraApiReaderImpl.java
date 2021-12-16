package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.BOARD_ID_PLACE_HOLDER;
import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.JIRA_MAX_RESULTS_RETURNED;
import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.MAX_RESULTS;
import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.SPRINT_ID_PLACE_HOLDER;
import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.START_AT_PLACE_HOLDER;
import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.START_AT_PLACE_LITERAL;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
      LOG.info("Try to read ticket for ticket-nr '" + ticketNr + "'");
      String url = jiraApiConfiguration.getGetIssueUrl() + ticketNr;
      JiraIssueResponse jiraIssueResponse = httpClient.callRequestAndParse(new JiraIssueResponseReader(), url);
      LOG.info("Read successfully ? " + (jiraIssueResponse.isSuccess() ? "yes" : "no"));
      return JiraResponseMapper.INSTANCE.map2Ticket(jiraIssueResponse);
   }

   @Override
   public JiraApiReadTicketsResult readTicketsFromBoardAndSprints(String boardName, List<String> sprintNames) {
      LOG.info("Try to read the tickets from the current sprint from board '" + boardName + "'");
      SprintInfos sprintInfos = evalActiveSprints4BoardName(boardName);
      if (sprintInfos.isEmpty()) {
         return failedResult(sprintInfos);
      }
      JiraIssuesResponse activeSprintIssues = createUrlAndReadIssuesFromJira(sprintInfos);
      readAndApplyFutureSprintTickets(activeSprintIssues, sprintInfos.getBoardId(), sprintNames);
      return JiraResponseMapper.INSTANCE.map2TicketResult(activeSprintIssues);
   }

   private void readAndApplyFutureSprintTickets(JiraIssuesResponse activeSprintIssues, String boardId, List<String> sprintNames) {
      LOG.info("Try to read the tickets for the future sprints '" + (sprintNames.isEmpty() ? "all" : sprintNames + "'"));
      getFutureSprintInfos(boardId)
            .stream()
            .filter(isRelevantSprint(sprintNames))
            .map(this::createUrlAndReadIssuesFromJira)
            .forEach(activeSprintIssues::applyFromOther);
   }

   private static Predicate<SprintInfo> isRelevantSprint(List<String> sprintNames) {
      return sprintInfo -> sprintNames.contains(sprintInfo.sprintName);
   }

   private SprintInfos evalActiveSprints4BoardName(String boardName) {
      String boardId = getBoardId(boardName);
      SprintInfos sprintInfos = getActiveSprintInfos(boardId);
      logResult(sprintInfos, boardName);
      return sprintInfos;
   }

   /*
    * Da jira jeweils nur 50 Resultate auf einmal zurückliefert, kann es nötig sein zu fetchen. Der Parameter 'maxResults' wirkt nur einschränkend,
    * kann aber die Grenze von 50 nicht umgehen. Ab dem 8-mal ist aber dann trotzdem schluss
    */
   private String getBoardId(String boardName) {
      String boardId;
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
      String getFutureSprintIdUrl = jiraApiConfiguration.getGetFuturSprintIdsForBoardUrl().replace(BOARD_ID_PLACE_HOLDER, boardId);
      JiraGenericValuesResponse jiraGetSprintIdResponse = httpClient.callRequestAndParse(new JiraGenericValuesResponseReader(), getFutureSprintIdUrl);
      return jiraGetSprintIdResponse.getValues()
            .stream()
            .map(buildSprintInfo(boardId))
            .collect(Collectors.toList());
   }

   private SprintInfos getActiveSprintInfos(String boardId) {
      String getActiveSprintIdUrl = jiraApiConfiguration.getGetActiveSprintIdsForBoardUrl().replace(BOARD_ID_PLACE_HOLDER, boardId);
      JiraGenericValuesResponse jiraGetSprintIdResponse = httpClient.callRequestAndParse(new JiraGenericValuesResponseReader(), getActiveSprintIdUrl);
      return jiraGetSprintIdResponse.getValues()
            .stream()
            .map(buildSprintInfo(boardId))
            .collect(Collectors.collectingAndThen(Collectors.toList(), buildSprintInfos(boardId)));
   }

   private Function<List<SprintInfo>, SprintInfos> buildSprintInfos(String boardId) {
      return sprintInfoEntries -> SprintInfos.of(sprintInfoEntries, boardId, boardId);
   }

   private String getBoardId4Name(String boardName, String startAt) {
      String allBoardsUrl = jiraApiConfiguration.getGetAllBoardUrls().replace(START_AT_PLACE_HOLDER, startAt);
      JiraGenericValuesResponse jiraGetBoardsResponse =
            httpClient.callRequestAndParse(new JiraGenericValuesResponseReader(), allBoardsUrl);
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
   private JiraIssuesResponse createUrlAndReadIssuesFromJira(SprintInfos sprintInfos) {
      return sprintInfos.getSprintInfoEntries()
            .stream()
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
      String url = createGetIssues4BoardUrl(sprintInfo);
      LOG.info("Trying to get issues for sprint '" + sprintInfo.sprintId + "' (" + sprintInfo.sprintName + ")");
      return readIssuesFromJira(url);
   }

   private String createGetIssues4BoardUrl(SprintInfo sprintInfo) {
      return jiraApiConfiguration.getGetIssues4BoardIdUrl()
            .replace(BOARD_ID_PLACE_HOLDER, sprintInfo.boardId)
            .replace(SPRINT_ID_PLACE_HOLDER, sprintInfo.sprintId)
            .replace(START_AT_PLACE_HOLDER, "0"); // let's start at the beginning
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

   private void logResult(SprintInfos sprintInfos, String boardName) {
      if (sprintInfos.isEmpty()) {
         LOG.warn("No active sprints found for board name '" + boardName + "'");
      }
      for (SprintInfo sprintInfo : sprintInfos.getSprintInfoEntries()) {
         LOG.info("Got sprint id " + sprintInfo.sprintId + " (" + sprintInfo.sprintName + ") for board name '" + boardName + "'");
      }
   }

   private JiraApiReadTicketsResult failedResult(SprintInfos sprintInfos) {
      LOG.warn("Unable to read tickets from board '" + sprintInfos.getBoardName() +
            "' (id=" + sprintInfos.getBoardId() + ", sprint-ids=" + SprintInfo.UNKNOWN + ")");
      return JiraApiReadTicketsResult.failed();
   }

   private Function<GenericNameAttrs, SprintInfo> buildSprintInfo(String boardId) {
      return genericNameAttrs -> new SprintInfo(boardId, genericNameAttrs);
   }

   private static class SprintInfos {

      private List<SprintInfo> sprintInfoEntries;
      private String boardName;
      private String boardId;

      private SprintInfos(List<SprintInfo> sprintInfoEntries, String boardName, String boardId) {
         this.sprintInfoEntries = requireNonNull(sprintInfoEntries);
         this.boardName = requireNonNull(boardName);
         this.boardId = requireNonNull(boardId);
      }

      public String getBoardId() {
         return boardId;
      }

      public String getBoardName() {
         return boardName;
      }

      public List<SprintInfo> getSprintInfoEntries() {
         return sprintInfoEntries;
      }

      public boolean isEmpty() {
         return sprintInfoEntries.isEmpty();
      }

      public static SprintInfos of(List<SprintInfo> sprintInfoEntries, String boardName, String boardId) {
         return new SprintInfos(sprintInfoEntries, boardName, boardId);
      }
   }

   static class SprintInfo {
      static final String UNKNOWN = "'unknown'";
      private String boardId;
      private String sprintId;
      private String sprintName;

      private SprintInfo(String boardId, GenericNameAttrs genericNameAttrs) {
         this(boardId, genericNameAttrs.getId(), genericNameAttrs.getName());
      }

      private static boolean isUnknown(String boardId) {
         return UNKNOWN.equals(boardId);
      }

      private SprintInfo(String boardId, String sprintId, String sprintName) {
         this.boardId = boardId;
         this.sprintId = sprintId;
         this.sprintName = sprintName;
      }
   }
}
