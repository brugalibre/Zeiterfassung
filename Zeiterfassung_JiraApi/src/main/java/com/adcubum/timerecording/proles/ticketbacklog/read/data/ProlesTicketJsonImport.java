package com.adcubum.timerecording.proles.ticketbacklog.read.data;

import com.adcubum.timerecording.data.ticket.ticketactivity.TicketActivityJsonImport;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProlesTicketJsonImport {
    private String customer;
    private String ticketNr;
    private String description;
    private String project;
    private TicketActivityJsonImport[] ticketActivities;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicketNr() {
        return ticketNr;
    }

    public void setTicketNr(String ticketNr) {
        this.ticketNr = ticketNr;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public TicketActivityJsonImport[] getTicketActivities() {
        return ticketActivities;
    }

    public List<TicketActivity> getProlesActivity() {
        return Arrays.stream(ticketActivities)
                .map(TicketActivityJsonImport::mapToProlesActivity)
                .collect(Collectors.toList());
    }

    public void setTicketActivities(TicketActivityJsonImport[] ticketActivities) {
        this.ticketActivities = ticketActivities;
    }
}