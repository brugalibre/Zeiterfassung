package com.adcubum.timerecording.jira.data.ticket;

/**
 * The {@link TicketAttrs} contains the attributes of a {@link Ticket}.
 * 
 * @author Dominic
 *
 */
public interface TicketAttrs {

   String getNr();

   String getExternalNr();

   String getEpicNr();

   IssueType getIssueType();

   String getTitle();

   String getImplementationPackage();

   long getProjectNr();

   int getProjectCostUnit();

   String getProjectDesc();

   String getThema();

   String getSubthema();

   String getId();

   String getBusinessTeamPlaning();

   String getPlaningId();

   String getSyriusExtension();

   String getSyriusRelease();

   String getAssignee();

   String getSprintId();

   String getSprintName();

}
