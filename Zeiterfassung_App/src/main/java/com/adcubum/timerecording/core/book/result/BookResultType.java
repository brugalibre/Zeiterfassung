package com.adcubum.timerecording.core.book.result;

public enum BookResultType {

   /** Everything went smoothly */
   SUCCESS,
   /** Almost went smoothly, we don't now where the issue is */
   PARTIAL_SUCCESS_WITH_ERROR,
   /** Almost went smoothly, we know there are some tickets which can't be booked. Probalby misconfigured */
   PARTIAL_SUCCESS_WITH_NON_BOOKABLE,
   /** Nothing went smoothly, it was a total desaster */
   FAILURE,
}
