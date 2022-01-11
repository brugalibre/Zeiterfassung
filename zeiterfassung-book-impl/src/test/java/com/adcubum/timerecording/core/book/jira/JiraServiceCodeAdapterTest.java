package com.adcubum.timerecording.core.book.jira;

import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDto;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class JiraServiceCodeAdapterTest {

   @Test
   void testPostFinanceReadServiceCodes() {
      // Given
      JiraServiceCodeAdapter jiraServiceCodeAdapter = new JiraServiceCodeAdapter();

      // When
      List<ServiceCodeDto> allServiceCodeDescriptions = jiraServiceCodeAdapter.getAllServiceCodeDescriptions();

      // Then
      assertThat(allServiceCodeDescriptions.size(), is(3));
      assertThat(allServiceCodeDescriptions.get(0).getServiceCodeName(), is("Projektarbeit in Basel"));
      assertThat(allServiceCodeDescriptions.get(1).getServiceCodeName(), is("Projektarbeit in Zofingen"));
      assertThat(allServiceCodeDescriptions.get(2).getServiceCodeName(), is("Reisezeit nicht verrechenbar"));
   }

   @Test
   void testGetUnknownServiceCode() {
      // Given
      JiraServiceCodeAdapter jiraServiceCodeAdapter = new JiraServiceCodeAdapter();
      int serviceCode = 45645645;

      // When
      ServiceCodeDto serviceCodeDto = jiraServiceCodeAdapter.getServiceCode4Code(serviceCode);

      // Then
      MatcherAssert.assertThat(serviceCodeDto.getServiceCode(), is(serviceCode));
      MatcherAssert.assertThat(serviceCodeDto.getServiceCodeName(), is("unknown"));
   }
}