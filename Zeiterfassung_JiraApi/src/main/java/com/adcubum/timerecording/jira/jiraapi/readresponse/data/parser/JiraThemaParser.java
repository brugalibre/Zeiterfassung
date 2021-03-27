package com.adcubum.timerecording.jira.jiraapi.readresponse.data.parser;

import static java.util.Objects.isNull;

/**
 * Helps parsing the thema name and the project cost unit description from the jira custom field 'customfield_11310'
 * 
 * @author Dominic
 *
 */
public class JiraThemaParser {

   private static final String DELIMITER = " ";

   /**
    * Retrieves the project numer from the String '400000 Syrius Wartung Standard'
    * Always assuming that the value of custom field 'customfield_10060' contains the value with this pattern
    * 
    * @param projectThemaAndCostUnit
    *        the input
    * @return the thema extracted from the input
    */
   public String getThema(String projectThemaAndCostUnit) {
      if (isNull(projectThemaAndCostUnit) || projectThemaAndCostUnit.indexOf(DELIMITER) < 0) {
         return "";
      }
      return projectThemaAndCostUnit.substring(0, projectThemaAndCostUnit.lastIndexOf(DELIMITER));
   }

   /**
    * Retrieves the project description from the String '400000 Syrius Wartung Standard'
    * Always assuming that the value of custom field 'customfield_10060' contains the value with this pattern
    * 
    * @param projectThemaAndCostUnit
    *        the input
    * @return the project description extracted from the input
    */
   public String getProjectCostUnit(String projectThemaAndCostUnit) {
      if (isNull(projectThemaAndCostUnit) || projectThemaAndCostUnit.lastIndexOf(DELIMITER) < 0) {
         return "";
      }
      return projectThemaAndCostUnit.substring(projectThemaAndCostUnit.lastIndexOf(DELIMITER) + 1)
            .replace(")", "")
            .replace("(", "");
   }
}
