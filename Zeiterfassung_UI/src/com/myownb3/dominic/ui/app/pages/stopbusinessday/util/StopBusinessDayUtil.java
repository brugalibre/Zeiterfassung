package com.myownb3.dominic.ui.app.pages.stopbusinessday.util;

import com.myownb3.dominic.ui.app.pages.stopbusinessday.view.StopBusinessDayIncrementPage;
import com.myownb3.dominic.util.utils.StringUtil;

public class StopBusinessDayUtil {
   /** The delimiter between multiple added Tickets on the {@link StopBusinessDayIncrementPage} */
   public static final String MULTIPLE_TICKETS_DELIMITER = ";";

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
      return StringUtil.isNotEmptyOrNull(ticketsAsString) && ticketsAsString.contains(MULTIPLE_TICKETS_DELIMITER);
   }
}
