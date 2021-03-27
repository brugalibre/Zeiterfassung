package com.adcubum.timerecording.jira.data.ticket;

public enum IssueType {

   BUG("Bug"),
   FEATURE("Feature"),
   SERVICE("Service"),
   TEST("Test"),
   TEST_AUTOMAT("Testautomat"),
   TECH_TASK("Technische Aufgabe"),
   UNKNOWN("Unbekannt");

   private String name;

   IssueType(String name) {
      this.name = name;
   }

   /**
    * 
    * Evaluates and returns an {@link IssueType} for the given name of {@link IssueType#UNKNOWN} if none is found
    * 
    * @param issueTypeAsString
    *        the name of the issue type
    * @return an {@link IssueType} for the given name of {@link IssueType#UNKNOWN} if none is found
    */
   public static IssueType of(String issueTypeAsString) {
      for (IssueType issueType : IssueType.values()) {
         if (issueType.name.equals(issueTypeAsString)) {
            return issueType;
         }
      }
      return UNKNOWN;
   }
}
