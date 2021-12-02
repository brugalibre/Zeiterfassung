/**
 * 
 */
package com.adcubum.timerecording.ticketbacklog.callback;

import com.adcubum.timerecording.ticketbacklog.TicketBacklog;

/**
 * The {@link TicketBacklogCallbackHandler} acts as a callbackhandler for the {@link TicketBacklog}
 * @author Dominic
 *
 */
public interface TicketBacklogCallbackHandler {

   /**
    * Is called whenever the {@link TicketBacklog} has been updated
    * 
    * @param updateStatus
    *        defines the actual status of the update
    * @see UpdateStatus
    */
   public void onTicketBacklogUpdated(UpdateStatus updateStatus);
}
