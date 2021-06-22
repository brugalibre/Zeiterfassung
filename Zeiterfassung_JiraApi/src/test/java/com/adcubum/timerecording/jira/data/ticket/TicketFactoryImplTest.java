package com.adcubum.timerecording.jira.data.ticket;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;

class TicketFactoryImplTest {

	@Test
	void testDummySuccessfullyCreate() {
		// Given
		TicketFactory ticketFactory = TicketFactory.INSTANCE;

		// When
		Ticket dummy1 = ticketFactory.dummy("1324");

		// Then
		assertThat(dummy1.isDummyTicket(), is(true));
		assertThat(dummy1.getTicketAttrs().isBookable(), is(false));
	}

}
