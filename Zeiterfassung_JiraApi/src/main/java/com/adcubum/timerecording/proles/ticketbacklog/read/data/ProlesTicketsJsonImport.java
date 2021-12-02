package com.adcubum.timerecording.proles.ticketbacklog.read.data;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.proles.ticketbacklog.ProlesTicketImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProlesTicketsJsonImport {
    private ProlesTicketJsonImport[] prolesTicketJsonImports;

    public void setProlesTicketJsonImports(ProlesTicketJsonImport[] prolesTicketJsonImports) {
        this.prolesTicketJsonImports = prolesTicketJsonImports;
    }

    /**
     * Maps all its {@link ProlesTicketJsonImport} into a ProlesTicketImpl
     *
     * @return a list of {@link Ticket}s
     */
    public List<Ticket> mapToProlesTickets() {
        return Arrays.stream(prolesTicketJsonImports)
                .map(ProlesTicketImpl::of)
                .collect(Collectors.toList());
    }
}