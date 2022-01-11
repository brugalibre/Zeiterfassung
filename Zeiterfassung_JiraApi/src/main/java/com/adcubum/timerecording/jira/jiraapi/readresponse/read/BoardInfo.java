package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraBoardsResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic.JiraGenericValuesResponse;

import java.util.Collections;
import java.util.List;

public class BoardInfo {
    public static final String UNKNOWN = "'unknown'";
    private String boardId;
    private String boardName;
    private String boardType;
    private List<SprintInfo> sprintInfos;

    public BoardInfo(String boardId, String boardType, String boardName) {
        this.boardId = boardId;
        this.boardType = boardType;
        this.boardName = boardName;
        this.sprintInfos = Collections.emptyList();
    }

    public static BoardInfo ofUnknown(String boardName) {
        return new BoardInfo(UNKNOWN, UNKNOWN, boardName);
    }

    public static BoardInfo of(JiraBoardsResponse.JiraBoardResponse jiraBoardResponse) {
        return new BoardInfo(jiraBoardResponse.getId(), jiraBoardResponse.getType(), jiraBoardResponse.getName());
    }

    public String getBoardId() {
        return boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public static boolean isUnknown(String boardId) {
        return UNKNOWN.equals(boardId);
    }

    public void setSprintInfos(List<SprintInfo> sprintInfos) {
        this.sprintInfos = sprintInfos;
    }

    public List<SprintInfo> getSprintInfos() {
        return this.sprintInfos;
    }

    public boolean isKanbanBoard() {
        return JiraApiConstants.KANBAN.equals(this.boardType);
    }

    public boolean isScrumBoard() {
        return JiraApiConstants.SCRUM.equals(this.boardType);
    }

    public static class SprintInfo {
        public String boardId;
        public String sprintId;
        public String sprintName;

        public SprintInfo(String boardId, JiraGenericValuesResponse.GenericNameAttrs genericNameAttrs) {
            this(boardId, genericNameAttrs.getId(), genericNameAttrs.getName());
        }

        private SprintInfo(String boardId, String sprintId, String sprintName) {
            this.boardId = boardId;
            this.sprintId = sprintId;
            this.sprintName = sprintName;
        }
    }
}
