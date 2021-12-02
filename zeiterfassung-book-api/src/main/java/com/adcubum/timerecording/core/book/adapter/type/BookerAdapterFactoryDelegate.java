package com.adcubum.timerecording.core.book.adapter.type;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;

/**
 * 
 * The {@link BookerAdapterFactoryDelegate} serves as a delegate to the {@link BookerAdapterFactory} which has,
 * in contrast to the {@link BookerAdapterFactory} access to the configuration in order to determine the actual
 * implementation of a {@link BookerAdapter}
 * 
 * @author dstalder
 *
 */
public interface BookerAdapterFactoryDelegate {

   /**
    * Creates a new or uses an already created {@link BookerAdapter}
    * 
    * @return a new or uses an already created {@link BookerAdapter}
    */
   BookerAdapter createBookerAdapter();

}
