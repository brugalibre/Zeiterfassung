package com.adcubum.timerecording.ui.app.pages.overview.control.businessdaychange;

import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangeCallbackHandler;

public class TicketBusinessDayChangeHelper extends AbstractBusinessDayChangeHelper<Ticket> {

   public TicketBusinessDayChangeHelper(BDChangeCallbackHandler uiRefresher) {
      super(uiRefresher);
   }
}
