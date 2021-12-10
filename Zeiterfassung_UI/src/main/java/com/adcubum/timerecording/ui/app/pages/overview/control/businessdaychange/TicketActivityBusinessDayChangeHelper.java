package com.adcubum.timerecording.ui.app.pages.overview.control.businessdaychange;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangedUiCallbackHandler;

public class TicketActivityBusinessDayChangeHelper extends AbstractBusinessDayChangeHelper<TicketActivity> {

   public TicketActivityBusinessDayChangeHelper(BDChangedUiCallbackHandler uiRefresher) {
      super(uiRefresher);
   }
}
