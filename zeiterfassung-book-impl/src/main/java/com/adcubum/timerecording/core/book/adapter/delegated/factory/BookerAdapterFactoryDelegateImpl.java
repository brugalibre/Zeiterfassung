package com.adcubum.timerecording.core.book.adapter.delegated.factory;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.type.BookerAdapterFactoryDelegate;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

public class BookerAdapterFactoryDelegateImpl implements BookerAdapterFactoryDelegate {

   private static final String BOOKER_NAME = "BookerName";
   private final Function<String, String> bookerNameProvider;

   private BookerAdapterFactoryDelegateImpl(){
      this(keyName -> Settings.INSTANCE.getSettingsValue(ValueKeyFactory.createNew(keyName, String.class)));
   }

   BookerAdapterFactoryDelegateImpl(UnaryOperator<String> bookerNameProvider){
      this.bookerNameProvider = requireNonNull(bookerNameProvider);
   }

   @Override
   public BookerAdapter createBookerAdapter() {
      String bookerName = bookerNameProvider.apply(BOOKER_NAME);
      return BookerAdapterType.getForName(bookerName).createBookerAdapter();
   }
}
