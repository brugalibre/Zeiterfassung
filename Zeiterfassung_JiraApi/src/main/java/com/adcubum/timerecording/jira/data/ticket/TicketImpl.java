package com.adcubum.timerecording.jira.data.ticket;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDto;
import com.adcubum.timerecording.data.ticket.ticketactivity.TicketActivityImpl;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssue;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * {@link TicketImpl} implements the {@link Ticket}
 * @author Dominic
 *
 */
public class TicketImpl implements Ticket{

   private Optional<BookerAdapter> bookerAdapterOptional;
   private TicketAttrs ticketAttrs;
   private boolean isCurrentUserAssigned;
   private boolean isSprintTicket;
   private boolean isDummyTicket;

   private TicketImpl(TicketAttrsImpl ticketAttrs, boolean isSprintTicket, boolean isDummyTicket, BookerAdapter bookerAdapter) {
      this.ticketAttrs = ticketAttrs;
      this.isCurrentUserAssigned = isCurrentUserAssigned(ticketAttrs.getAssignee());
      this.isSprintTicket = isSprintTicket;
      this.isDummyTicket = isDummyTicket;
      this.bookerAdapterOptional = Optional.ofNullable(bookerAdapter);
   }

   /**
    * Creates an empty Ticket. Only the Ticket-Nr is set
    * 
    * @param ticketNr
    *        the ticket-nr
    * @return an empty Ticket. Only the Ticket-Nr is set
    */
   public static TicketImpl dummy(String ticketNr) {
      JiraIssue jiraIssue = new JiraIssue();
      jiraIssue.setKey(ticketNr);
      return TicketImpl.of(jiraIssue, false, true);
   }

   public static TicketImpl of(JiraIssue issue, boolean isSprintTicket, boolean isDummyTicket) {
      return new TicketImpl(TicketAttrsImpl.of(issue), isSprintTicket, isDummyTicket, isDummyTicket ? null : BookerAdapterFactory.getAdapter());
   }

   public static TicketImpl of(JiraIssue issue) {
      return new TicketImpl(TicketAttrsImpl.of(issue), true, false, BookerAdapterFactory.getAdapter());
   }

   private boolean isCurrentUserAssigned(String assignee) {
      String currentUserName = AuthenticationService.INSTANCE.getUsername();
      return nonNull(currentUserName) && currentUserName.equalsIgnoreCase(assignee);
   }

   @Override
   public List<TicketActivity> getTicketActivities() {
      return bookerAdapterOptional.map(BookerAdapter::getServiceCodeAdapter)
              .map(serviceCodeAdapter -> fetchServiceCodesForProjectNr(serviceCodeAdapter)
                      .stream()
                      .map(toTicketActivity())
                      .collect(Collectors.toList()))
              .orElse(Collections.emptyList());
   }

   private List<ServiceCodeDto> fetchServiceCodesForProjectNr(ServiceCodeAdapter serviceCodeAdapter) {
      return serviceCodeAdapter.fetchServiceCodesForProjectNr(this.ticketAttrs.getProjectNr());
   }

   @NotNull
   private static Function<ServiceCodeDto, TicketActivity> toTicketActivity() {
      return serviceCode -> new TicketActivityImpl(serviceCode.getServiceCodeName(), serviceCode.getServiceCode());
   }

   @Override
   public TicketAttrs getTicketAttrs() {
      return ticketAttrs;
   }

   @Override
   public String getNr() {
      return ticketAttrs.getNr();
   }

   @Override
   public boolean isBookable() {
      return bookerAdapterOptional.map(bookerAdapter -> bookerAdapter.isTicketBookable(this))
              .orElse(false);
   }

   @Override
   public boolean isDummyTicket() {
      return isDummyTicket;
   }

   @Override
   public boolean isSprintTicket() {
      return isSprintTicket;
   }

   @Override
   public boolean isCurrentUserAssigned() {
      return isCurrentUserAssigned;
   }

   @Override
   public String getTicketRep() {
      String title = this.ticketAttrs.getTitle();
      if (isNull(title)) {
         return this.getNr();
      }
      return this.getNr() + " (" + title + ")";
   }

   @Override
   public String toString() {
      return "Ticket-Nr = " + ticketAttrs.getNr() + " (" + ticketAttrs.getTitle() + "), projekt-nr = " + ticketAttrs.getProjectNr() + " ("
            + ticketAttrs.getProjectDesc() + ")";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((ticketAttrs == null) ? 0 : ticketAttrs.hashCode());
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
      TicketImpl other = (TicketImpl) obj;
      if (ticketAttrs == null) {
         if (other.ticketAttrs != null)
            return false;
      } else if (!ticketAttrs.equals(other.ticketAttrs))
         return false;
      return true;
   }
}
