package com.adcubum.timerecording.ticketbacklog.factory;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;

import java.util.function.Function;

import static com.adcubum.timerecording.settings.common.Const.TICKET_SYSTEM_PROPERTIES;
import static java.util.Objects.requireNonNull;

public class TicketBacklogFactoryDelegateImpl implements TicketBacklogFactoryDelegate {

   private static final String TICKET_SYSTEM_NAME = "TicketSystem";
   private final Function<String, TicketBacklogType> ticketBacklogNameProvider;

   private TicketBacklogFactoryDelegateImpl(){
      this(keyName -> Settings.INSTANCE.getSettingsValue(ValueKeyFactory.createNew(keyName, TICKET_SYSTEM_PROPERTIES, TicketBacklogType.class)));
   }

   TicketBacklogFactoryDelegateImpl(Function<String, TicketBacklogType> ticketBacklogNameProvider){
      this.ticketBacklogNameProvider = requireNonNull(ticketBacklogNameProvider);
   }

   @Override
   public TicketBacklog createTicketBacklog() {
      TicketBacklogType ticketBacklogType = ticketBacklogNameProvider.apply(TICKET_SYSTEM_NAME);
      return ticketBacklogType.createTicketBacklog();
   }
}
