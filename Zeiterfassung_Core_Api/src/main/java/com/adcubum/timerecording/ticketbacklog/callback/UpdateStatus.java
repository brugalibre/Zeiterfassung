package com.adcubum.timerecording.ticketbacklog.callback;

public enum UpdateStatus {
   /** The update of the tickets was successfully */
   SUCCESS,
   /** The update of the tickets has failed */
   FAIL,
   /** The update of the tickets was not possible due to missing configuration */
   NOT_CONFIGURED
}