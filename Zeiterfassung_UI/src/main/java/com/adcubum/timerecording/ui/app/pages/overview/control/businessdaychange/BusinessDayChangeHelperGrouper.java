package com.adcubum.timerecording.ui.app.pages.overview.control.businessdaychange;

import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangedUiCallbackHandler;

/**
 * Just a helper class in order to combine all the different XYZBusinessDayChangeHelper which works all the same way
 * - just with a different event value
 */
public class BusinessDayChangeHelperGrouper {

    private StringBusinessDayChangeHelper changeListener;
    private TicketBusinessDayChangeHelper ticketChangeListener;
    private TicketActivityBusinessDayChangeHelper ticketActivityChangeListener;

    public BusinessDayChangeHelperGrouper(BDChangedUiCallbackHandler uiRefresher) {
        this.changeListener = new StringBusinessDayChangeHelper(uiRefresher);
        this.ticketChangeListener = new TicketBusinessDayChangeHelper(uiRefresher);
        this.ticketActivityChangeListener = new TicketActivityBusinessDayChangeHelper(uiRefresher);
    }

    public StringBusinessDayChangeHelper getChangeListener() {
        return changeListener;
    }

    public TicketBusinessDayChangeHelper getTicketChangeListener() {
        return ticketChangeListener;
    }

    public TicketActivityBusinessDayChangeHelper getTicketActivityChangeListener() {
        return ticketActivityChangeListener;
    }
}
