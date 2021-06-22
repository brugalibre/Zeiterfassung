package com.adcubum.timerecording.ui.app.pages.overview.control.businessdaychange;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangedUiCallbackHandler;

public class TicketBusinessDayChangeHelper extends AbstractBusinessDayChangeHelper<Ticket> {

   public TicketBusinessDayChangeHelper(BDChangedUiCallbackHandler uiRefresher) {
      super(uiRefresher);
   }
}
