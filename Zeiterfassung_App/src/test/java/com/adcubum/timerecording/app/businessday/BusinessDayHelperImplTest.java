package com.adcubum.timerecording.app.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;

class BusinessDayHelperImplTest {

   @Test
   void testGetBusinessDay_GetNull() {

      // Given
      BusinessDayHelperImpl businessDayHelperImpl = new BusinessDayHelperImpl(mock(BusinessDayRepository.class));

      // When
      BusinessDay actualBusinessDay = businessDayHelperImpl.getBusinessDay();

      // Then
      assertThat(actualBusinessDay, is(nullValue()));
   }

}
