package com.adcubum.timerecording.core.work.businessday.repository;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;

public class BusinessDayRepositoryMockUtil {

   private BusinessDayRepositoryMockUtil() {
      // private 
   }

   /**
    * Mocks a {@link BusinessDayRepository} for the given {@link BusinessDayRepository}
    * 
    * @param businessDay
    *        the {@link BusinessDay} which is managed by the mocked {@link BusinessDayRepository}
    * @return a mocked {@link BusinessDayRepository}
    */
   public static BusinessDayRepository mockBusinessDayRepository(BusinessDay businessDay) {
      BusinessDayRepository businessDayRepository = mock(BusinessDayRepository.class);
      doReturn(businessDay).when(businessDayRepository).findFirstOrCreateNew();
      doReturn(businessDay).when(businessDayRepository).findById(any());
      doReturn(businessDay).when(businessDayRepository).save(any());
      return businessDayRepository;
   }
}
