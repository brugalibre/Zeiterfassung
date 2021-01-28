package com.myownb3.dominic.timerecording.ticketbacklog;

import org.apache.commons.lang3.StringUtils;

import com.myownb3.dominic.timerecording.settings.Settings;

public class TicketBacklogHelper {
   private static final String BOARD_NAME_VALUE_KEY = "boardName";

   /**
    * @return <code>true</code> if there is a board configured or <code>false</code> if not
    */
   public boolean hasBordNameConfigured() {
      String boardName = Settings.INSTANCE.getSettingsValue(BOARD_NAME_VALUE_KEY);
      return !StringUtils.isEmpty(boardName);
   }

   /**
    * @return the configured board name
    */
   public String getBoardName() {
      return Settings.INSTANCE.getSettingsValue(BOARD_NAME_VALUE_KEY);
   }
}
