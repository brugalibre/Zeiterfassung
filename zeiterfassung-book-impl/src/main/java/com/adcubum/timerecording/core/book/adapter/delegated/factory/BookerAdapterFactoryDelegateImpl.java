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

   private static final String BOOKER_ADAPTER_NAME = "TicketSystem";
   private final Function<String, BookerAdapterType> bookerNameProvider;

   private BookerAdapterFactoryDelegateImpl(){
      this(keyName -> Settings.INSTANCE.getSettingsValue(ValueKeyFactory.createNew(keyName, TICKET_SYSTEM_PROPERTIES, BookerAdapterType.class)));
   }

   BookerAdapterFactoryDelegateImpl(Function<String, BookerAdapterType> bookerNameProvider){
      this.bookerNameProvider = requireNonNull(bookerNameProvider);
   }

   @Override
   public BookerAdapter createBookerAdapter() {
      BookerAdapterType bookerAdapterType = bookerNameProvider.apply(BOOKER_ADAPTER_NAME);
      return bookerAdapterType.createBookerAdapter();
   }
}
