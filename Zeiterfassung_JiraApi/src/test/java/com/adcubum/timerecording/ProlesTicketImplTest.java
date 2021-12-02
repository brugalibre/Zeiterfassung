package com.adcubum.timerecording;

import com.adcubum.timerecording.data.ticket.ticketactivity.TicketActivityJsonImport;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.proles.ticketbacklog.ProlesTicketImpl;
import com.adcubum.timerecording.proles.ticketbacklog.read.data.ProlesTicketJsonImport;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ProlesTicketImplTest {

   @Test
   void createProlesTicket_TicketBookable() {
      // Given
      String project = "project";
      String ticketNr = "ticketNr";
      String customer = "customer";
      TicketActivityJsonImport ticketActivityJsonImport = buildTicketActivityJsonImport();
      ProlesTicketJsonImport prolesTicketJsonImport = buildProlesTicketJsonImport(project, ticketNr, customer, ticketActivityJsonImport);

      // When
      Ticket prolesTicket = ProlesTicketImpl.of(prolesTicketJsonImport);

      // Then
      assertThat(prolesTicket.isBookable(), is(true));
   }

   @Test
   void createProlesTicket_NoCustomer_TicketNotBookable() {
      // Given
      String project = "project";
      String ticketNr = "nag-1";
      String customer = "";
      ProlesTicketJsonImport prolesTicketJsonImport = buildProlesTicketJsonImport(project, ticketNr, customer);

      // When
      Ticket prolesTicket = ProlesTicketImpl.of(prolesTicketJsonImport);

      // Then
      assertThat(prolesTicket.isBookable(), is(false));
   }

   @Test
   void createProlesTicket_NoTicketNr_TicketNotBookable() {
      // Given
      String project = "project";
      String ticketNr = "";
      String customer = "customer";
      ProlesTicketJsonImport prolesTicketJsonImport = buildProlesTicketJsonImport(project, ticketNr, customer);

      // When
      Ticket prolesTicket = ProlesTicketImpl.of(prolesTicketJsonImport);

      // Then
      assertThat(prolesTicket.isBookable(), is(false));
   }

   @Test
   void createProlesTicket_NoProject_TicketNotBookable() {
      // Given
      String project = "";
      String ticketNr = "ticketNr";
      String customer = "customer";
      ProlesTicketJsonImport prolesTicketJsonImport = buildProlesTicketJsonImport(project, ticketNr, customer);

      // When
      Ticket prolesTicket = ProlesTicketImpl.of(prolesTicketJsonImport);

      // Then
      assertThat(prolesTicket.isBookable(), is(false));
   }

   @Test
   void createProlesTicket_NoTicketActivities_TicketNotBookable() {
      // Given
      String project = "project";
      String ticketNr = "ticketNr";
      String customer = "customer";
      ProlesTicketJsonImport prolesTicketJsonImport = buildProlesTicketJsonImport(project, ticketNr, customer);

      // When
      Ticket prolesTicket = ProlesTicketImpl.of(prolesTicketJsonImport);

      // Then
      assertThat(prolesTicket.isBookable(), is(false));
   }

   @NotNull
   private static ProlesTicketJsonImport buildProlesTicketJsonImport(String project, String ticketNr, String customer, TicketActivityJsonImport... ticketActivityJsonImports) {
      ProlesTicketJsonImport prolesTicketJsonImport = new ProlesTicketJsonImport();
      prolesTicketJsonImport.setProject(project);
      prolesTicketJsonImport.setTicketNr(ticketNr);
      prolesTicketJsonImport.setCustomer(customer);
      prolesTicketJsonImport.setDescription("");
      prolesTicketJsonImport.setTicketActivities(ticketActivityJsonImports);
      return prolesTicketJsonImport;
   }

   private static TicketActivityJsonImport buildTicketActivityJsonImport() {
      TicketActivityJsonImport ticketActivityJsonImport = new TicketActivityJsonImport();
      ticketActivityJsonImport.setActivityCode(1);
      ticketActivityJsonImport.setActivityName("test");
      return ticketActivityJsonImport;
   }
}