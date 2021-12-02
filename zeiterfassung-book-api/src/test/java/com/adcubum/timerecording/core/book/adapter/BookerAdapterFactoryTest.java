package com.adcubum.timerecording.core.book.adapter;

import com.adcubum.timerecording.core.book.coolguys.ChargeType;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class BookerAdapterFactoryTest {

   @Test
   void testGetServiceCodeAdapter() {

      // Given

      // When
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();

      // Then
      assertThat(serviceCodeAdapter.getClass().isAssignableFrom(ChargeType.class), is(true));
   }
}
