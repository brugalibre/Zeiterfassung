package com.adcubum.timerecording.core.book.proles;

import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.ticketbacklog.callback.TicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProlesServiceCodeAdapter implements ServiceCodeAdapter, TicketBacklogCallbackHandler {

    private List<TicketActivity> allActivities;

    ProlesServiceCodeAdapter() {
        this.allActivities = new ArrayList<>();
    }

    @Override
    public void onTicketBacklogUpdated(UpdateStatus updateStatus) {
        if (updateStatus == UpdateStatus.SUCCESS) {
            this.allActivities = TicketBacklogSPI.getTicketBacklog()
                    .getTickets().stream()
                    .map(Ticket::getTicketActivities)
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<String> fetchServiceCodesForProjectNr(long projectNr) {
        // Since an activity doesn't have a mapped project-nr, we can't filter for it
        return getAllActivityNames();
    }

    @Override
    public List<String> getAllServiceCodes() {
        return getAllActivityNames();
    }

    private List<String> getAllActivityNames() {
        return allActivities
                .stream().map(TicketActivity::getActivityName)
                .collect(Collectors.toList());
    }

    @Override
    public int getServiceCode4Description(String serviceCodeDesc) {
        return allActivities
                .stream()
                .filter(prolesActivity -> serviceCodeDesc == prolesActivity.getActivityName())
                .map(TicketActivity::getActivityCode)
                .findFirst()
                .orElse(-1);
    }

    @Override
    public String getServiceCodeDescription4ServiceCode(int serviceCode) {
        return allActivities
                .stream()
                .filter(prolesActivity -> serviceCode == prolesActivity.getActivityCode())
                .map(TicketActivity::getActivityName)
                .findFirst()
                .orElse(null);
    }
}
