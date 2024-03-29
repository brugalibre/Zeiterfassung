package com.adcubum.timerecording.jira.jiraapi.configuration;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

import static com.adcubum.timerecording.settings.common.Const.TICKET_SYSTEM_PROPERTIES;
import static java.util.Objects.isNull;

public class JiraApiConfigurationHelper {
   private static final ValueKey<String> JIRA_BASE_URL_VALUE_KEY = ValueKeyFactory.createNew("jiraUrl", TICKET_SYSTEM_PROPERTIES, String.class);
   private static final ValueKey<String> DEFAULT_TICKET_NAME_KEY = ValueKeyFactory.createNew("defaultticketname", TICKET_SYSTEM_PROPERTIES, String.class);
   private static final ValueKey<String> TICKET_NAME_PATTERN_KEY = ValueKeyFactory.createNew("ticketnamepattern", TICKET_SYSTEM_PROPERTIES,String.class);
   private static final ValueKey<String> BOARD_TYPE_VALUE_KEY = ValueKeyFactory.createNew("boardType", TICKET_SYSTEM_PROPERTIES, String.class);
   private static final ValueKey<Integer> FETCH_BOARD_BEGIN_INDEX_KEY = ValueKeyFactory.createNew("fetchBoardsBeginIndex", TICKET_SYSTEM_PROPERTIES, Integer.class, 0);

   /**
    * @return the configured board type
    */
   public String getBoardType() {
      return Settings.INSTANCE.getSettingsValue(BOARD_TYPE_VALUE_KEY);
   }
   /**
    * @return the configured fetchBoardsBeginIndex
    */
   public Integer getFetchBoardsBeginIndex() {
      return Settings.INSTANCE.getSettingsValue(FETCH_BOARD_BEGIN_INDEX_KEY);
   }

   /**
    * @return the configured jira url
    */
   public String getJiraBaseUrl() {
      return Settings.INSTANCE.getSettingsValue(JIRA_BASE_URL_VALUE_KEY);
   }

   /**
    * @return the default name for a Ticket stored in the {@link Settings}
    */
   public String getDefaultTicketName() {
      return Settings.INSTANCE.getSettingsValue(DEFAULT_TICKET_NAME_KEY);
   }

   /**
    * @return the regex-pattern for any ticket name stored in the {@link Settings}
    */
   public String getTicketNamePattern() {
      return Settings.INSTANCE.getSettingsValue(TICKET_NAME_PATTERN_KEY);
   }
}
