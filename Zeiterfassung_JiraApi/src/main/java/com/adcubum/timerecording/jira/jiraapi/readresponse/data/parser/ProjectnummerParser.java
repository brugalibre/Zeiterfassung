package com.adcubum.timerecording.jira.jiraapi.readresponse.data.parser;

import static java.util.Objects.isNull;

/**
 * Helps pasring the project numer and project description from the jira custom field 'customfield_10060'
 * 
 * @author Dominic
 *
 */
public class ProjectnummerParser {

   private static final String DELIMITER = " ";

   /**
    * Retrieves the project numer from the String '400000 Syrius Wartung Standard'
    * Always assuming that the value of custom field 'customfield_10060' contains the value with this pattern
    * 
    * @param projektNrAndBez
    *        the input
    * @return the project number extracted from the input
    */
   public String getProjectNr(String projektNrAndBez) {
      if (isNull(projektNrAndBez) || projektNrAndBez.indexOf(DELIMITER) < 0) {
         return "";
      }
      return projektNrAndBez.substring(0, projektNrAndBez.indexOf(DELIMITER));
   }

   /**
    * Retrieves the project description from the String '400000 Syrius Wartung Standard'
    * Always assuming that the value of custom field 'customfield_10060' contains the value with this pattern
    * 
    * @param projektNrAndBez
    *        the input
    * @return the project description extracted from the input
    */
   public String getProjectDesc(String projektNrAndBez) {
      if (isNull(projektNrAndBez) || projektNrAndBez.indexOf(DELIMITER) < 0) {
         return "";
      }
      return projektNrAndBez.substring(projektNrAndBez.indexOf(DELIMITER) + 1);
   }

}
