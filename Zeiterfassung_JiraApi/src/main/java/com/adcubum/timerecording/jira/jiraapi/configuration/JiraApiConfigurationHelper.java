package com.adcubum.timerecording.jira.jiraapi.configuration;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

import static java.util.Objects.isNull;

public class JiraApiConfigurationHelper {
   private static final ValueKey<String> JIRA_BASE_URL_VALUE_KEY = ValueKeyFactory.createNew("jiraUrl", String.class);
   private static final ValueKey<String> DEFAULT_TICKET_NAME_KEY = ValueKeyFactory.createNew("defaultticketname", String.class);
   private static final ValueKey<String> TICKET_NAME_PATTERN_KEY = ValueKeyFactory.createNew("ticketnamepattern", String.class);
   private static final ValueKey<String> BOARD_TYPE_VALUE_KEY = ValueKeyFactory.createNew("boardType", String.class);
   private static final ValueKey<String> FETCH_BOARD_BEGIN_INDEX_KEY = ValueKeyFactory.createNew("fetchBoardsBeginIndex", String.class);

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
      String fetchBoardsBeginIndexAsString = Settings.INSTANCE.getSettingsValue(FETCH_BOARD_BEGIN_INDEX_KEY);
      return isNull(fetchBoardsBeginIndexAsString) ? 0 : Integer.valueOf(fetchBoardsBeginIndexAsString);
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
