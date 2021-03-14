package com.myownb3.dominic.timerecording.core.book.abacus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.adcubum.j2a.zeiterfassung.AbacusBookingConnector;
import com.myownb3.dominic.timerecording.core.book.adapter.ServiceCodeAdapter;

class AbacusBookerAdapterTest {

   @Test
   void testGetServiceCodeAdapter() {
      // Given
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      AbacusBookerAdapter abacusBookerAdapter = new AbacusBookerAdapter(abacusBookingConnector);

      // When
      ServiceCodeAdapter serviceCodeAdapter = abacusBookerAdapter.getServiceCodeAdapter();
      List<String> allServiceCodes = serviceCodeAdapter.getAllServiceCodes();

      // Then
      assertThat(allServiceCodes, is(notNullValue()));
      assertThat(allServiceCodes.size(), is(DefaultServiceCodes.getDefaultServiceCodes().values().size()));
   }
}
