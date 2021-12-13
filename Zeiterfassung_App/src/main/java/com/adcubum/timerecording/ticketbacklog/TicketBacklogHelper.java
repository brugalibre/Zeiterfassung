package com.adcubum.timerecording.ticketbacklog;

import static java.util.Objects.nonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

public class TicketBacklogHelper {
   private static final ValueKey<String> JIRA_BASE_URL_VALUE_KEY = ValueKeyFactory.createNew("jiraUrl", String.class);
   private static final ValueKey<String> DEFAULT_TICKET_NAME_KEY = ValueKeyFactory.createNew("defaultticketname", String.class);
   private static final ValueKey<String> TICKET_NAME_PATTERN_KEY = ValueKeyFactory.createNew("ticketnamepattern", String.class);
   private static final ValueKey<String> BOARD_NAME_VALUE_KEY = ValueKeyFactory.createNew("boardName", String.class);
   private static final ValueKey<String> SPRINT_NAMES_VALUE_KEY = ValueKeyFactory.createNew("sprintNames", String.class);
   private static final String SPRINT_NAMES_DELIMITER = ";";

   /**
    * @return a {@link List} with all sprint names
    */
   public List<String> getSprintNames() {
      String concatedSprintNames = Settings.INSTANCE.getSettingsValue(SPRINT_NAMES_VALUE_KEY);
      List<String> sprintNames = Collections.emptyList();
      if (nonNull(concatedSprintNames)) {
         sprintNames = Arrays.asList(concatedSprintNames.split(SPRINT_NAMES_DELIMITER));
      }
      return sprintNames;
   }

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
