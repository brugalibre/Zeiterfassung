package com.adcubum.timerecording.jira.jiraapi.configuration
        ;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JiraApiConfiguration {

    private static final String MULTI_TICKET_DELIMITER = ";";
    // Jira-api / url stuff
    private String jiraUrl;
    private String jiraAgileBasePath;
    private String jiraBoardBaseUrl;
    private String getActiveSprintIdsForBoardUrl;
    private String getFuturSprintIdsForBoardUrl;
    private String getAllBoardUrls;
    private String getIssueUrl;
    private String boardType;
    private Integer fetchBoardsBeginIndex;
    private Integer fetchResultSize;

    // Jira log-time api
    private String jiraWorklogBasePath;
    private String jiraCreateWorklogUrl;

    // Ticket stuff
    private String defaultTicketName;
    private String ticketNamePattern;
    private String multiTicketNoPattern;

    // Placeholders
    private String boardIdPlaceholder;
    private String sprintIdPlaceholder;
    private String startAtPlaceholder;
    private String getIssues4SprintBoardIdUrl;
    private String getIssues4KanbanBoardIdUrl;

    JiraApiConfiguration(String jiraUrl, String jiraAgileBasePath, String jiraWorklogBasePath, String boardIdPlaceholder, String sprintIdPlaceholder,
                         String startAtPlaceholder, String ticketNamePattern, String defaultTicketName, String boardType, Integer fetchBoardsBeginIndex, int fetchResultSize) {
        this.jiraUrl = jiraUrl;
        this.boardType = boardType;
        this.jiraAgileBasePath = jiraAgileBasePath;
        this.jiraWorklogBasePath = jiraWorklogBasePath;
        this.boardIdPlaceholder = boardIdPlaceholder;
        this.sprintIdPlaceholder = sprintIdPlaceholder;
        this.startAtPlaceholder = startAtPlaceholder;
        this.defaultTicketName = defaultTicketName;
        this.ticketNamePattern = ticketNamePattern;
        this.fetchResultSize = fetchResultSize;
        this.fetchBoardsBeginIndex = fetchBoardsBeginIndex;
        this.multiTicketNoPattern = "(" + ticketNamePattern + "(" + MULTI_TICKET_DELIMITER + "?)){1,}";
        applyFromConfiguration(this);
    }

    /**
     * Applays present values from the other {@link JiraApiConfiguration}
     * Note that non-existing values of the new configuration does not override any values of the existing configuration
     *
     * @param jiraApiConfiguration2Apply the config to apply from
     */
    public void applyFromConfiguration(JiraApiConfiguration jiraApiConfiguration2Apply) {
        if (isNull(this.jiraUrl) || nonNull(jiraApiConfiguration2Apply.jiraUrl)) {
            this.jiraUrl = jiraApiConfiguration2Apply.jiraUrl;
        }
        if (isNull(this.jiraAgileBasePath) || nonNull(jiraApiConfiguration2Apply.jiraAgileBasePath)) {
            this.jiraAgileBasePath = jiraApiConfiguration2Apply.jiraAgileBasePath;
        }
        if (isNull(this.sprintIdPlaceholder) || nonNull(jiraApiConfiguration2Apply.sprintIdPlaceholder)) {
            this.sprintIdPlaceholder = jiraApiConfiguration2Apply.sprintIdPlaceholder;
        }
        if (isNull(this.startAtPlaceholder) || nonNull(jiraApiConfiguration2Apply.startAtPlaceholder)) {
            this.startAtPlaceholder = jiraApiConfiguration2Apply.startAtPlaceholder;
        }
        if (isNull(this.defaultTicketName) || nonNull(jiraApiConfiguration2Apply.defaultTicketName)) {
            this.defaultTicketName = jiraApiConfiguration2Apply.defaultTicketName;
        }
        if (isNull(this.ticketNamePattern) || nonNull(jiraApiConfiguration2Apply.ticketNamePattern)) {
            this.ticketNamePattern = jiraApiConfiguration2Apply.ticketNamePattern;
        }
        if (isNull(this.boardType) || nonNull(jiraApiConfiguration2Apply.boardType)) {
            this.boardType = jiraApiConfiguration2Apply.boardType;
        }
        if (isNull(this.fetchBoardsBeginIndex) || nonNull(jiraApiConfiguration2Apply.fetchBoardsBeginIndex)) {
            this.fetchBoardsBeginIndex = jiraApiConfiguration2Apply.fetchBoardsBeginIndex;
        }
        if (isNull(this.jiraWorklogBasePath) || nonNull(jiraApiConfiguration2Apply.jiraWorklogBasePath)) {
            this.jiraWorklogBasePath = jiraApiConfiguration2Apply.jiraWorklogBasePath;
        }
        this.jiraCreateWorklogUrl = this.jiraUrl  + "/" + this.jiraWorklogBasePath + "?notifyUsers=false";
        this.multiTicketNoPattern = "(" + ticketNamePattern + "(" + MULTI_TICKET_DELIMITER + "?)){1,}";
        String jiraBaseUrl = this.jiraUrl + "/" + jiraAgileBasePath;
        this.jiraBoardBaseUrl = jiraBaseUrl + "board";
        this.getIssues4SprintBoardIdUrl = this.jiraBoardBaseUrl + "/" + boardIdPlaceholder + "/sprint/"
                + sprintIdPlaceholder + "/issue?startAt=" + startAtPlaceholder + "&maxResults=" + fetchResultSize;
        this.getIssues4KanbanBoardIdUrl = getIssues4SprintBoardIdUrl.replace("/sprint/" + sprintIdPlaceholder, "");
        this.getActiveSprintIdsForBoardUrl = jiraBoardBaseUrl + "/" + boardIdPlaceholder + "/sprint?state=active";
        this.getFuturSprintIdsForBoardUrl = jiraBoardBaseUrl + "/" + boardIdPlaceholder + "/sprint?state=future";
        this.getAllBoardUrls = jiraBaseUrl + "board?type=" + boardType + "&startAt=" + startAtPlaceholder;
        this.getIssueUrl = jiraBaseUrl + "issue/";
    }

    /**
     * @return the  plain URL which directs to the jira board itself
     */
    public String getJiraBoardBaseUrl() {
        return jiraBoardBaseUrl;
    }

    /**
     * @return the  plain URL which is used to create a jira-api {@link com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.data.Worklog}
     */
    public String getJiraCreateWorkUrl() {
        return jiraCreateWorklogUrl;
    }

    /**
     * @return the URL for retrieving all issues for a specific board id
     */
    public String getGetIssues4SprintBoardIdUrl() {
        return getIssues4SprintBoardIdUrl;
    }

    /**
     * @return the URL for retrieving all issues for a specific (kanban) board id
     */
    public String getGetIssues4KanbanBoardIdUrl() {
        return getIssues4KanbanBoardIdUrl;
    }

    public int getFetchBoardsBeginIndex() {
        return fetchBoardsBeginIndex;
    }

    /**
     * @return the URL for retrieving the sprint id for an active sprint and a given board id
     */
    public String getGetActiveSprintIdsForBoardUrl() {
        return getActiveSprintIdsForBoardUrl;
    }

    /**
     * @return the URL for retrieving all issues for a specific board id
     */
    public String getGetFuturSprintIdsForBoardUrl() {
        return getFuturSprintIdsForBoardUrl;
    }

    /**
     * @return the URL for retrieving all boards
     */
    public String getGetAllBoardUrls() {
        return getAllBoardUrls;
    }

    /**
     * @return the URL for retrieving details for an issue
     */
    public String getGetIssueUrl() {
        return getIssueUrl;
    }

    public String getJiraUrl() {
        return jiraUrl;
    }

    /**
     * @return the regex for a ticket-name
     */
    public String getTicketNamePattern() {
        return ticketNamePattern;
    }

    /**
     * @return the default ticket name
     */
    public String getDefaultTicketName() {
        return defaultTicketName;
    }

    /**
     * @return the regex for multiple ticket-names
     */
    public String getMultiTicketNoPattern() {
        return multiTicketNoPattern;
    }
}
