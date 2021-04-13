/**
 * 
 */
package com.adcubum.timerecording.ticketbacklog.callback;

import com.adcubum.timerecording.ticketbacklog.TicketBacklog;

/**
 * @author Dominic
 *
 */
public interface UiTicketBacklogCallbackHandler {

   /**
    * Is called whenever the {@link TicketBacklog} has been updated
    * 
    * @param updateStatus
    *        defines the actual status of the update
    * @see UpdateStatus
    */
   public void onTicketBacklogUpdated(UpdateStatus updateStatus);

   public enum UpdateStatus {
      /** The update of the tickets was successfully */
      SUCCESS,
      /** The update of the tickets has failed */
      FAIL,
      /** The update of the tickets was not possible due to missing configuration */
      NOT_CONFIGURED
   }
}
