package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.error.JiraErrorResponse;

/**
 * Generischer Response eines Json elements das eine Liste von 'values' beinhaltet
 * Jedes value-Attribut beinhaltet wiederum ein key- und ein name-Attribut
 * 
 * @author Dominic
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraGenericValuesResponse extends JiraErrorResponse {
   private List<GenericNameAttrs> values;

   /**
    * Default constructor needed by jackson
    */
   public JiraGenericValuesResponse() {
      this(null, null);
   }

   /**
    * Constructor needed when something went south
    */
   public JiraGenericValuesResponse(Exception e, String url) {
      super(e, url);
      this.values = new ArrayList<>();
   }

   public List<GenericNameAttrs> getValues() {
      return values;
   }

   void setValues(List<GenericNameAttrs> values) {
      this.values = values;
   }

   @JsonIgnoreProperties(ignoreUnknown = true)
   public static class GenericNameAttrs {
      private String id;
      private String name;

      public String getName() {
         return name;
      }

      void setName(String name) {
         this.name = name;
      }

      public String getId() {
         return id;
      }

      void setId(String id) {
         this.id = id;
      }
   }
}