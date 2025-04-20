package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic.JiraGenericValuesResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraBoardsResponse extends AbstractJiraResponse {
    private List<JiraBoardResponse> values;

    public JiraBoardsResponse() {
        // default constructor for json
        this.values = Collections.emptyList();
    }

    public List<JiraBoardResponse> getValues() {
        return values;
    }

    public void setValues(List<JiraBoardResponse> values) {
        this.values = values;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class JiraBoardResponse extends JiraGenericValuesResponse.GenericNameAttrs {
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
