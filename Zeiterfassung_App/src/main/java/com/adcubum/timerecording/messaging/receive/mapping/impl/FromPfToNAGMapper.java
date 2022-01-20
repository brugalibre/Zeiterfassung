package com.adcubum.timerecording.messaging.receive.mapping.impl;

import com.adcubum.timerecording.messaging.receive.mapping.TicketMapper;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.common.Const;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

import static com.adcubum.timerecording.settings.common.Const.PROLES_TICKET_ACTIVITIES_PROPERTIES;

public class FromPfToNAGMapper implements TicketMapper {

   private static final ValueKey<String> PROLES_PF_TICKET_NAME_KEY = ValueKeyFactory.createNew(Const.PROLES_PF_TICKET_NAME, PROLES_TICKET_ACTIVITIES_PROPERTIES, String.class);
   private Settings settings;

   public FromPfToNAGMapper(Settings settings) {
      this.settings = settings;
   }

   @Override
   public String mapTicketNr(String ticketNr) {
      return settings.getSettingsValue(PROLES_PF_TICKET_NAME_KEY);
   }

   @Override
   public String mapTicketActivityCode(String ticketActivityCode) {
      // since jira-work-log plugin doesn't require a ticket-activity at all,
      // we you already nag-ticket-activities on the pf-side. That's why we can map 1:1 here
      return ticketActivityCode;
   }
}
