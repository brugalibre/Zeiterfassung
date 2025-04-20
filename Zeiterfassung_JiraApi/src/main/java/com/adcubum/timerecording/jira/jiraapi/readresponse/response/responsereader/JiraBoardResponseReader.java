package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraBoardsResponse;
import com.brugalibre.common.http.model.response.ResponseWrapper;
import com.brugalibre.common.http.service.response.AbstractHttpResponseReader;

public class JiraBoardResponseReader extends AbstractHttpResponseReader<JiraBoardsResponse> {
    @Override
    protected Class<JiraBoardsResponse> getResponseResultClass() {
        return JiraBoardsResponse.class;
    }

    @Override
    public ResponseWrapper<JiraBoardsResponse> createErrorResponse(Exception e, String url) {
        return new ResponseWrapper<>(new JiraBoardsResponse(), 500, e, url);
    }
}
