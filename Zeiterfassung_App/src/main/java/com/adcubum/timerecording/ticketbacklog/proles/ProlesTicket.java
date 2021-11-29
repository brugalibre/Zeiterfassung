package com.adcubum.timerecording.ticketbacklog.proles;

import com.adcubum.timerecording.jira.data.ticket.IssueType;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;

public class ProlesTicket implements Ticket {

   private TicketAttrs ticketAttrs;
   private String ticketRepresentation;

   @Override
   public String getTicketRep() {
      return ticketRepresentation;
   }

   @Override
   public boolean isCurrentUserAssigned() {
      return false;
   }

   @Override
   public boolean isSprintTicket() {
      return false;
   }

   @Override
   public boolean isDummyTicket() {
      return false;
   }

   @Override
   public boolean isBookable() {
      return true;
   }

   @Override
   public String getNr() {
      return ticketAttrs.getNr();
   }

   @Override
   public TicketAttrs getTicketAttrs() {
      return ticketAttrs;
   }

   public static final class ProlesTicketAttr implements TicketAttrs {

      private String ticketNr;
      private String title;
      private String projectDesc;
      private String customer;

      @Override
      public String getNr() {
         return ticketNr;
      }

      @Override
      public String getExternalNr() {
         return null;
      }

      @Override
      public String getEpicNr() {
         return null;
      }

      @Override
      public IssueType getIssueType() {
         return IssueType.BUG;
      }

      @Override
      public String getTitle() {
         return title;
      }

      @Override
      public String getImplementationPackage() {
         return null;
      }

      @Override
      public long getProjectNr() {
         return -1;
      }

      @Override
      public int getProjectCostUnit() {
         return -1;
      }

      @Override
      public String getProjectDesc() {
         return projectDesc;
      }

      @Override
      public String getThema() {
         return customer;
      }

      @Override
      public String getSubthema() {
         return null;
      }

      @Override
      public String getId() {
         return null;
      }

      @Override
      public String getBusinessTeamPlaning() {
         return null;
      }

      @Override
      public String getPlaningId() {
         return null;
      }

      @Override
      public String getSyriusExtension() {
         return null;
      }

      @Override
      public String getSyriusRelease() {
         return null;
      }

      @Override
      public String getAssignee() {
         return null;
      }

      @Override
      public String getSprintId() {
         return null;
      }

      @Override
      public String getSprintName() {
         return null;
      }

      @Override
      public boolean isBookable() {
         return true;
      }
   }
}
