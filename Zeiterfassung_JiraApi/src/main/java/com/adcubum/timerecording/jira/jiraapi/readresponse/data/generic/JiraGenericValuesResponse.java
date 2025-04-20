package com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic;

import java.util.ArrayList;
import java.util.List;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.AbstractJiraResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Generischer Response eines Json elements das eine Liste von 'values' beinhaltet
 * Jedes value-Attribut beinhaltet wiederum ein key- und ein name-Attribut
 * 
 * @author Dominic
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraGenericValuesResponse extends AbstractJiraResponse {
   private List<GenericNameAttrs> values;

   /**
    * Default constructor needed by jackson
    */
   public JiraGenericValuesResponse() {
      this.values = new ArrayList<>();
   }

   public List<GenericNameAttrs> getValues() {
      return values;
   }

   public void setValues(List<GenericNameAttrs> values) {
      this.values = values;
   }

   @JsonIgnoreProperties(ignoreUnknown = true)
   public static class GenericNameAttrs {
      private String id;
      private String name;

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getId() {
         return id;
      }

      public void setId(String id) {
         this.id = id;
      }
   }
}
