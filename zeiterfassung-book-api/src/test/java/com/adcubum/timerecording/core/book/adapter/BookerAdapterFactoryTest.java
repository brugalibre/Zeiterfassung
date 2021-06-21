package com.adcubum.timerecording.core.book.adapter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class BookerAdapterFactoryTest {

   @Test
   void testGetServiceCodeAdapter() {

      // Given

      // When
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();

      // Then
      assertThat(serviceCodeAdapter, is(notNullValue()));
   }

}
