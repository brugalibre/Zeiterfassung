package com.adcubum.timerecording.core.book.adapter.delegated.factory;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.type.BookerAdapterFactoryDelegate;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.adcubum.timerecording.settings.common.Const.TICKET_SYSTEM_PROPERTIES;
import static java.util.Objects.requireNonNull;

public class BookerAdapterFactoryDelegateImpl implements BookerAdapterFactoryDelegate {

   private static final String TICKET_SYSTEM_NAME = "TicketSystem";
   private final Function<String, String> bookerNameProvider;

   private BookerAdapterFactoryDelegateImpl(){
      this(keyName -> Settings.INSTANCE.getSettingsValue(ValueKeyFactory.createNew(keyName, TICKET_SYSTEM_PROPERTIES, String.class)));
   }

   BookerAdapterFactoryDelegateImpl(UnaryOperator<String> bookerNameProvider){
      this.bookerNameProvider = requireNonNull(bookerNameProvider);
   }

   @Override
   public BookerAdapter createBookerAdapter() {
      String ticketSystemName = bookerNameProvider.apply(TICKET_SYSTEM_NAME);
      return BookerAdapterType.getForName(ticketSystemName).createBookerAdapter();
   }
}
