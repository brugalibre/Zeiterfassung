package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.parser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueFields.JiraFixVersionsType;

/**
 * Helps parsing the syr-release from the jira custom field 'fixVersions'.
 * 
 * @author Dominic
 *
 */
public class JiraSyrReleaseParser {

   private static final String PRAEFIX = "[";
   private static final String SUFFIX = "]";
   private static final String SYR_REL_DELIMITER = ", ";

   public String getSyriusRelease(JiraFixVersionsType[] jiraFixVersionsTypes) {
      StringBuilder stringBuilder = new StringBuilder(PRAEFIX);
      List<JiraFixVersionsType> fixVersionsAsList = Arrays.asList(jiraFixVersionsTypes);
      for (Iterator<JiraFixVersionsType> jiraFixVersionsTypeIt = fixVersionsAsList.iterator(); jiraFixVersionsTypeIt
            .hasNext();) {
         JiraFixVersionsType jiraFixVersionsType = jiraFixVersionsTypeIt.next();
         stringBuilder.append(jiraFixVersionsType.getName());
         if (jiraFixVersionsTypeIt.hasNext()) {
            stringBuilder.append(SYR_REL_DELIMITER);
         }
      }
      return stringBuilder
            .append(SUFFIX)
            .toString();
   }
}
