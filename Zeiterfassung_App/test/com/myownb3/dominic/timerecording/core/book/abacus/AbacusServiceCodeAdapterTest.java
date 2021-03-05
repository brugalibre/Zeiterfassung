package com.myownb3.dominic.timerecording.core.book.abacus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.adcubum.j2a.zeiterfassung.AbacusBookingConnector;

class AbacusServiceCodeAdapterTest {

   private static final String EXPECTED_SERVICE_CODE_DESC = "expectedServiceCodeDesc";

   @Test
   void testFetchServiceCodesForProject() {
      // Given
      long projectNr = 1234;
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      AbacusServiceCodeAdapter abacusServiceCodeAdapter = new AbacusServiceCodeAdapter(abacusBookingConnector);
      // When
      abacusServiceCodeAdapter.fetchServiceCodesForProjectNr(projectNr);

      // Then
      verify(abacusBookingConnector).fetchServiceCodesForProject(eq(projectNr));
   }

   @Test
   void testGetServiceCode4Description() {
      // Given
      String expectedServiceCodeDesc = EXPECTED_SERVICE_CODE_DESC;
      int serviceCode = 1234;
      AbacusBookingConnector abacusBookingConnector = mockAbacusBookingConnector(expectedServiceCodeDesc, serviceCode, "anotherDescription", 135);
      AbacusServiceCodeAdapter abacusServiceCodeAdapter = new AbacusServiceCodeAdapter(abacusBookingConnector);
      abacusServiceCodeAdapter.init();

      // When
      int actualServiceCode = abacusServiceCodeAdapter.getServiceCode4Description(expectedServiceCodeDesc);

      // Then
      assertThat(actualServiceCode, is(serviceCode));
   }

   @Test
   void testGetServiceCode4Description_NoMatch() {
      // Given
      int expectedServiceCode = -1;
      String givenServiceCodeDesc = "anotherDescription";
      AbacusBookingConnector abacusBookingConnector = mockAbacusBookingConnector(EXPECTED_SERVICE_CODE_DESC, 135);
      AbacusServiceCodeAdapter abacusServiceCodeAdapter = new AbacusServiceCodeAdapter(abacusBookingConnector);
      abacusServiceCodeAdapter.init();

      // When
      int actualServiceCode = abacusServiceCodeAdapter.getServiceCode4Description(givenServiceCodeDesc);

      // Then
      assertThat(actualServiceCode, is(expectedServiceCode));
   }

   @Test
   void testGetServiceCode4DescriptionWithExceptionDuringInit() {
      // Given
      String expectedServiceCodeDesc = EXPECTED_SERVICE_CODE_DESC;
      int serviceCode = 1234;
      int expectedServiceCode = -1;
      AbacusBookingConnector abacusBookingConnector = mockAbacusBookingConnector(expectedServiceCodeDesc, serviceCode);
      doThrow(new RuntimeException()).when(abacusBookingConnector).fetchAllServiceCodes();
      AbacusServiceCodeAdapter abacusServiceCodeAdapter = new AbacusServiceCodeAdapter(abacusBookingConnector);
      abacusServiceCodeAdapter.init();

      // When
      int actualServiceCode = abacusServiceCodeAdapter.getServiceCode4Description(expectedServiceCodeDesc);

      // Then
      assertThat(actualServiceCode, is(expectedServiceCode));
   }

   @Test
   void testGetAllServiceCodesDesc() {
      // Given
      String expectedServiceCodeDesc = EXPECTED_SERVICE_CODE_DESC;
      int serviceCode = 1234;
      AbacusBookingConnector abacusBookingConnector = mockAbacusBookingConnector(expectedServiceCodeDesc, serviceCode);
      AbacusServiceCodeAdapter abacusServiceCodeAdapter = new AbacusServiceCodeAdapter(abacusBookingConnector);
      abacusServiceCodeAdapter.init();

      // When
      List<String> allServiceCodes = abacusServiceCodeAdapter.getAllServiceCodes();

      // Then
      assertThat(allServiceCodes, is(Collections.singletonList(expectedServiceCodeDesc)));
   }

   @Test
   void testGetServiceCodeDescription4Code() {
      // Given
      String expectedServiceCodeDesc = EXPECTED_SERVICE_CODE_DESC;
      int serviceCode = 1234;
      AbacusBookingConnector abacusBookingConnector = mockAbacusBookingConnector(expectedServiceCodeDesc, serviceCode);
      AbacusServiceCodeAdapter abacusServiceCodeAdapter = new AbacusServiceCodeAdapter(abacusBookingConnector);
      abacusServiceCodeAdapter.init();

      // When
      String actualServiceCodeDescription4Code = abacusServiceCodeAdapter.getServiceCodeDescription4ServiceCode(serviceCode);

      // Then
      assertThat(actualServiceCodeDescription4Code, is(expectedServiceCodeDesc));
   }

   private AbacusBookingConnector mockAbacusBookingConnector(String expectedServiceCodeDesc1, int serviceCode1, String expectedServiceCodeDesc2,
         int serviceCode2) {
      AbacusBookingConnector abacusBookingConnector = mock(AbacusBookingConnector.class);
      Map<Integer, String> allServiceCodes = new HashMap<>();
      allServiceCodes.put(serviceCode1, expectedServiceCodeDesc1);
      allServiceCodes.put(serviceCode2, expectedServiceCodeDesc2);
      when(abacusBookingConnector.fetchAllServiceCodes()).thenReturn(allServiceCodes);
      return abacusBookingConnector;
   }

   private AbacusBookingConnector mockAbacusBookingConnector(String expectedServiceCodeDesc, int serviceCode) {
      return mockAbacusBookingConnector(expectedServiceCodeDesc, serviceCode, expectedServiceCodeDesc, serviceCode);
   }
}
