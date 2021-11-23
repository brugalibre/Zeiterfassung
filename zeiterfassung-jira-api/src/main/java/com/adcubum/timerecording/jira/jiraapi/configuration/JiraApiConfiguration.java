package com.adcubum.timerecording.jira.jiraapi.configuration
        ;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JiraApiConfiguration {

    private String jiraUrl;
    private String jiraAgileBasePath;
    private String jiraBoardBaseUrl;
    private String getIssues4BoardIdUrl;
    private String getActiveSprintIdsForBoardUrl;
    private String getFuturSprintIdsForBoardUrl;
    private String getAllBoardUrls;
    private String getIssueUrl;
    private String jiraBaseUrl;

    // Placeholders
    private String boardIdPlaceholder;
    private String sprintIdPlaceHh;
    private String startAtPlaceholder;

    JiraApiConfiguration(String jiraUrl, String jiraAgileBasePath, String boardIdPlaceholder,
                                String sprintIdPlaceHh, String startAtPlaceholder) {
        this.jiraUrl = jiraUrl;
        this.jiraAgileBasePath = jiraAgileBasePath;
        this.boardIdPlaceholder = boardIdPlaceholder;
        this.sprintIdPlaceHh = sprintIdPlaceHh;
        this.startAtPlaceholder = startAtPlaceholder;
    }

    /**
     * Applays present values from the other {@link JiraApiConfiguration}
     * Note that non-existing values of the new configuration does not override any values of the existing configuration
     *
     * @param jiraApiConfiguration2Apply
     */
    public void applyFromConfiguration(JiraApiConfiguration jiraApiConfiguration2Apply) {
        if (isNull(this.jiraUrl) || nonNull(jiraApiConfiguration2Apply.jiraUrl)) {
            this.jiraUrl = jiraApiConfiguration2Apply.jiraUrl;
        }
        if (isNull(this.jiraAgileBasePath) || nonNull(jiraApiConfiguration2Apply.jiraAgileBasePath)) {
            this.jiraAgileBasePath = jiraApiConfiguration2Apply.jiraAgileBasePath;
        }
        if (isNull(this.sprintIdPlaceHh) || nonNull(jiraApiConfiguration2Apply.sprintIdPlaceHh)) {
            this.sprintIdPlaceHh = jiraApiConfiguration2Apply.sprintIdPlaceHh;
        }
        if (isNull(this.startAtPlaceholder) || nonNull(jiraApiConfiguration2Apply.startAtPlaceholder)) {
            this.startAtPlaceholder = jiraApiConfiguration2Apply.startAtPlaceholder;
        }
        this.jiraBaseUrl = this.jiraUrl + "/" + jiraAgileBasePath;
        this.jiraBoardBaseUrl = this.jiraBaseUrl + "board";
        this.getIssues4BoardIdUrl = this.jiraBoardBaseUrl + "/" + boardIdPlaceholder + "/sprint/"
                + sprintIdPlaceHh + "/issue?startAt=" + startAtPlaceholder;
        this.getActiveSprintIdsForBoardUrl = jiraBoardBaseUrl + "/" + boardIdPlaceholder + "/sprint?state=active";
        this.getFuturSprintIdsForBoardUrl = jiraBoardBaseUrl + "/" + boardIdPlaceholder + "/sprint?state=future";
        this.getAllBoardUrls = jiraBaseUrl + "board?type=scrum&startAt=" + startAtPlaceholder;
        this.getIssueUrl = jiraBaseUrl + "issue/";
    }

    public String getJiraBaseUrl() {
        return jiraUrl + "/" + jiraAgileBasePath;
    }

    /**
     * @return the  plain URL which directs to the jira board itself
     */
    public String getJiraBoardBaseUrl() {
        return jiraBoardBaseUrl;
    }

    /**
     * @return the URL for retrieving all issues for a specific board id
     */
    public String getGetIssues4BoardIdUrl() {
        return getIssues4BoardIdUrl;
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
}
