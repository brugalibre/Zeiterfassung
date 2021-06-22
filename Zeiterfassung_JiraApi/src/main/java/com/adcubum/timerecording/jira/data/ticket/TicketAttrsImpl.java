package com.adcubum.timerecording.jira.data.ticket;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssue;

/**
 * {@link TicketAttrsImpl} implements the {@link TicketAttrs}. It's actually more a wrapper for the {@link JiraIssue} and all it's
 * cryptic custom jira attribute names
 * @author Dominic
 *
 */
public class TicketAttrsImpl implements TicketAttrs{
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

   private TicketAttrsImpl() {
      // private 
   }

   public static TicketAttrsImpl of(JiraIssue issue) {
      TicketAttrsImpl ticketAttrs = new TicketAttrsImpl();
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

   @Override
   public String getNr() {
      return nr;
   }

   @Override
   public String getExternalNr() {
      return externalNr;
   }
   
   @Override
   public String getEpicNr() {
      return epicNr;
   }

   @Override
   public IssueType getIssueType() {
      return issueType;
   }

   @Override
   public String getTitle() {
      return title;
   }

   @Override
   public String getImplementationPackage() {
      return implementationPackage;
   }

   @Override
   public long getProjectNr() {
      if (isNull(projectNr) || projectNr.isEmpty()) {
         return -1;
      }
      return Long.valueOf(projectNr);
   }

   @Override
   public int getProjectCostUnit() {
      if (isNull(projectCostUnit) || projectCostUnit.isEmpty()) {
         return -1;
      }
      return Integer.valueOf(projectCostUnit);
   }

   @Override
   public String getProjectDesc() {
      return projectDesc;
   }

   @Override
   public String getThema() {
      return thema;
   }

   @Override
   public String getSubthema() {
      return subthema;
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getBusinessTeamPlaning() {
      return businessTeamPlaning;
   }

   @Override
   public String getPlaningId() {
      return planingId;
   }

   @Override
   public String getSyriusExtension() {
      return syriusExtension;
   }

   @Override
   public String getSyriusRelease() {
      return syriusRelease;
   }

   @Override
   public String getAssignee() {
      return assignee;
   }

   @Override
   public String getSprintId() {
      return sprintId;
   }

   @Override
   public String getSprintName() {
      return sprintName;
   }

   @Override
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
      TicketAttrsImpl other = (TicketAttrsImpl) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      return true;
   }
}
