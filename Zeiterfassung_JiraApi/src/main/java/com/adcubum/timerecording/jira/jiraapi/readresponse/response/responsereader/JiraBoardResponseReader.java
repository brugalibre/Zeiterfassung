package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraBoardsResponse;

public class JiraBoardResponseReader extends AbstractJiraResponseReader<JiraBoardsResponse> {
    @Override
    protected Class<JiraBoardsResponse> getResponseResultClass() {
        return JiraBoardsResponse.class;
    }

    @Override
    public JiraBoardsResponse createErrorResponse(Exception e, String url) {
        return new JiraBoardsResponse(e, url);
    }
}
