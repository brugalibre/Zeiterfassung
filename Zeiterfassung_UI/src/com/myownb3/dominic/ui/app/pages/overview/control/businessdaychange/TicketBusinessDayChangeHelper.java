package com.myownb3.dominic.ui.app.pages.overview.control.businessdaychange;

import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.ui.app.pages.overview.control.callback.BDChangeCallbackHandler;

public class TicketBusinessDayChangeHelper extends AbstractBusinessDayChangeHelper<Ticket> {

   public TicketBusinessDayChangeHelper(BDChangeCallbackHandler uiRefresher) {
      super(uiRefresher);
   }
}
