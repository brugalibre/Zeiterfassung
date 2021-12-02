package com.adcubum.timerecording.core.book.adapter.delegated.factory;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.proles.ProlesBookerHelper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class BookerAdapterFactoryDelegateImplTest {

    @Test
    void testGetProlesBookerServiceAdapter (){

        // Given
        BookerAdapterFactoryDelegateImpl bookerAdapterFactoryDelegate = new BookerAdapterFactoryDelegateImpl(key -> "proles-web");

        // When
        BookerAdapter bookerAdapter = bookerAdapterFactoryDelegate.createBookerAdapter();

        // Then
        assertThat(bookerAdapter.getClass().isAssignableFrom(ProlesBookerHelper.class), is(true));
    }

}