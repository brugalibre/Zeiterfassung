package com.adcubum.timerecording.ticketbacklog;

import com.adcubum.timerecording.ticketbacklog.callback.TicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class AbstractTicketBacklog implements TicketBacklog {

    protected List<TicketBacklogCallbackHandler> callbackHandlers;

    public AbstractTicketBacklog () {
        this.callbackHandlers = new ArrayList<>();
    }

    @Override
    public void addTicketBacklogCallbackHandler(TicketBacklogCallbackHandler callbackHandler) {
        this.callbackHandlers.add(requireNonNull(callbackHandler));
    }

    protected void notifyCallbackHandlers(UpdateStatus updateStatus) {
        callbackHandlers.forEach(callbackHandler -> callbackHandler.onTicketBacklogUpdated(updateStatus));
    }
}
