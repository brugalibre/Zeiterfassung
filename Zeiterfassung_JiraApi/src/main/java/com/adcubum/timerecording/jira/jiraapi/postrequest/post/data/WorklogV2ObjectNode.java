package com.adcubum.timerecording.jira.jiraapi.postrequest.post.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The {@link WorklogV2ObjectNode} representing a jira-api <code>worklog</code> object in version 2
 * This WorklogV2ObjectNode is a json representation and therefore provides a method in order to convert into a json string
 */
public class WorklogV2ObjectNode {

   /**
    * Returns a json-string representing a jira-api <code>worklog</code> object in version 2
    *
    * @param timeSpentSeconds the time which was spent on an issue
    * @param comment          the comment to describe the work
    * @param started          the date when the work was started
    * @return a {@link ObjectNode}
    */
   public static String buildWorklogJsonV2(int timeSpentSeconds, String comment, String started) {
      ObjectNode worklogObjectNode = buildWorklogPayloadV2Object(timeSpentSeconds, comment, started);
      return buildWorklogJsonV2(worklogObjectNode);
   }

   private static String buildWorklogJsonV2(ObjectNode worklogObjectNode) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
      try {
         return mapper.writeValueAsString(worklogObjectNode);
      } catch (JsonProcessingException e) {
         throw new IllegalStateException(e);
      }
   }

   /**
    * Returns a {@link ObjectNode} representing a jira-api <code>worklog</code> object in version 2
    *
    * @param timeSpentSeconds the time which was spent on an issue
    * @param comment          the comment to describe the work
    * @param started          the date when the work was started
    * @return a {@link ObjectNode}
    */
   private static ObjectNode buildWorklogPayloadV2Object(int timeSpentSeconds, String comment, String started) {
      ObjectNode worklogPayload = JsonNodeFactory.instance.objectNode();
      {
         worklogPayload.put("timeSpentSeconds", timeSpentSeconds);
         worklogPayload.put("comment", comment);
         worklogPayload.put("started", started);
      }
      return worklogPayload;
   }
}
