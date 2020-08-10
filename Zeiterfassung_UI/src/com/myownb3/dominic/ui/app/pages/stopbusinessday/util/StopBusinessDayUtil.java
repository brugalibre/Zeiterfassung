package com.myownb3.dominic.ui.app.pages.stopbusinessday.util;

import com.myownb3.dominic.util.utils.StringUtil;

public class StopBusinessDayUtil {

   private StopBusinessDayUtil() {
      // private 
   }

   /**
    * Verifies if the given String contains multiple Tickets
    * 
    * @param ticketsAsString
    *        the given String
    * @return <code>true</code> if the given String contains multiple Tickets
    *         otherwise <code>false</code>
    */
   public static boolean areMultipleTicketsEntered(String ticketsAsString) {
      return StringUtil.isNotEmptyOrNull(ticketsAsString) && ticketsAsString.contains(";");
   }
}
