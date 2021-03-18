package com.myownb3.dominic.timerecording.ticketbacklog.data.ticket;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssue;

/**
 * The {@link TicketAttrs} contains the attributes of a {@link Ticket}. It's actually more a wrapper for the {@link JiraIssue} and all it's
 * cryptic custom jira attribute names
 * 
 * @author Dominic
 *
 */
public class TicketAttrs {
   private String assignee;
   private String sprintId;
   private String sprintName;
   private String id;
   private String nr;
   private String externalNr;
   private String epicNr;
   private IssueType issueType;
   private String title;
   private String projectNr;
   private String projectCostUnit;
   private String projectDesc;

   private String thema;
   private String subthema;
   private String businessTeamPlaning;
   private String planingId;
   private String syriusExtension;
   private String syriusRelease;
   private String implementationPackage;

   private TicketAttrs() {
      // private 
   }

   public static TicketAttrs of(JiraIssue issue) {
      TicketAttrs ticketAttrs = new TicketAttrs();
      ticketAttrs.assignee = issue.getAssignee();
      ticketAttrs.nr = issue.getKey();
      ticketAttrs.id = issue.getId();
      ticketAttrs.externalNr = issue.getExternalNr();
      ticketAttrs.epicNr = issue.getEpicNr();
      ticketAttrs.issueType = getIssueType(issue);
      ticketAttrs.title = issue.getTitle();
      ticketAttrs.projectCostUnit = issue.getProjectCostUnit();
      ticketAttrs.projectDesc = issue.getProjectDesc();
      ticketAttrs.projectNr = issue.getProjectNr();
      ticketAttrs.thema = issue.getThema();
      ticketAttrs.subthema = issue.getSubthema();
      ticketAttrs.businessTeamPlaning = issue.getBusinessTeamPlaning();
      ticketAttrs.planingId = issue.getPlaningId();
      ticketAttrs.syriusExtension = issue.getSyriusExtension();
      ticketAttrs.syriusRelease = issue.getSyriusRelease();
      ticketAttrs.implementationPackage = issue.getImplementationPackage();
      ticketAttrs.sprintId = issue.getSprintId();
      ticketAttrs.sprintName = issue.getSprintName();
      return ticketAttrs;
   }

   private static IssueType getIssueType(JiraIssue issue) {
      if (nonNull(issue.getIssueType())) {
         return IssueType.of(issue.getIssueType());
      }
      return null;
   }

   public String getNr() {
      return nr;
   }

   public String getExternalNr() {
      return externalNr;
   }

   public String getEpicNr() {
      return epicNr;
   }

   public IssueType getIssueType() {
      return issueType;
   }

   public String getTitle() {
      return title;
   }

   public String getImplementationPackage() {
      return implementationPackage;
   }

   public long getProjectNr() {
      if (isNull(projectNr) || projectNr.isEmpty()) {
         return -1;
      }
      return Long.valueOf(projectNr);
   }

   public int getProjectCostUnit() {
      if (isNull(projectCostUnit) || projectCostUnit.isEmpty()) {
         return -1;
      }
      return Integer.valueOf(projectCostUnit);
   }

   public String getProjectDesc() {
      return projectDesc;
   }

   public String getThema() {
      return thema;
   }

   public String getSubthema() {
      return subthema;
   }

   public String getId() {
      return id;
   }

   public String getBusinessTeamPlaning() {
      return businessTeamPlaning;
   }

   public String getPlaningId() {
      return planingId;
   }

   public String getSyriusExtension() {
      return syriusExtension;
   }

   public String getSyriusRelease() {
      return syriusRelease;
   }

   public String getAssignee() {
      return assignee;
   }

   public String getSprintId() {
      return sprintId;
   }

   public String getSprintName() {
      return sprintName;
   }

   /**
    * @return <code>true</code> if there are all relevant value present or <code>false</code> if not
    */
   public boolean isBookable() {
      return nonNull(projectNr) && !projectNr.isEmpty();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      TicketAttrs other = (TicketAttrs) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      return true;
   }
}
