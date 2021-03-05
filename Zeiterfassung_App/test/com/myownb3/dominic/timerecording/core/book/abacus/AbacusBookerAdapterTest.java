package com.myownb3.dominic.timerecording.core.book.abacus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.adcubum.j2a.zeiterfassung.AbacusBookingConnector;
import com.myownb3.dominic.timerecording.core.book.adapter.ServiceCodeAdapter;

class AbacusBookerAdapterTest {

   @Test
   void testGetServiceCodeAdapter() {
      // Given
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      AbacusBookerAdapter abacusBookerAdapter = new AbacusBookerAdapter(abacusBookingConnector, "test");

      // When
      ServiceCodeAdapter serviceCodeAdapter = abacusBookerAdapter.getServiceCodeAdapter();
      serviceCodeAdapter.getAllServiceCodes();

      // Then
      assertThat(serviceCodeAdapter, is(notNullValue()));
      verify(abacusBookingConnector).fetchAllServiceCodes();
   }
}
