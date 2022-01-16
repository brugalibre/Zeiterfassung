package com.adcubum.timerecording.ticketbacklog.factory;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

public class TicketBacklogFactoryDelegateImpl implements TicketBacklogFactoryDelegate {

   private static final String TICKET_SYSTEM_NAME = "TicketSystem";
   private final Function<String, String> ticketBacklogNameProvider;

   private TicketBacklogFactoryDelegateImpl(){
      this(keyName -> Settings.INSTANCE.getSettingsValue(ValueKeyFactory.createNew(keyName, String.class)));
   }

   TicketBacklogFactoryDelegateImpl(UnaryOperator<String> ticketBacklogNameProvider){
      this.ticketBacklogNameProvider = requireNonNull(ticketBacklogNameProvider);
   }

   @Override
   public TicketBacklog createTicketBacklog() {
      String ticketSystemName = ticketBacklogNameProvider.apply(TICKET_SYSTEM_NAME);
      return TicketBacklogType.getForName(ticketSystemName)
              .createTicketBacklog();
   }
}
