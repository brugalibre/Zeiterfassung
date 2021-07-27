package com.adcubum.timerecording.core.work.businessday.repository;

import java.util.UUID;

import com.adcubum.timerecording.core.repository.ObjectNotFoundException;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;

public class BusinessDayRepositoryIntegMockUtil {

   private BusinessDayRepositoryIntegMockUtil() {
      // private 
   }

   /**
    * Creates a test implementation of a {@link BusinessDayRepository} for the given {@link BusinessDay}
    * 
    * @param businessDay
    *        the {@link BusinessDay} which is managed by the mocked {@link BusinessDayRepository}
    * @return a mocked {@link BusinessDayRepository}
    */
   public static BusinessDayRepository mockBusinessDayRepository(BusinessDay businessDay) {
      return new TestBusinessDayRepositoryImpl(businessDay);
   }

   /**
    * The {@link TestBusinessDayRepositoryImpl} uses it's {@link BusinessDay} member instance as a 'repository'
    * This means it has an initial {@link BusinessDay} which is returned when invoking {@link BusinessDayRepository#findFirstOrCreateNew()}
    * or {@link BusinessDayRepository#findById(UUID)} - if we don't have another one.
    * If you call {@link BusinessDayRepository#save(BusinessDay)}, the given {@link BusinessDay} is returned by this very method and, from
    * this point forth, also when calling {@link BusinessDayRepository#findById(UUID)}
    * 
    * @author DStalder
    *
    */
   private static class TestBusinessDayRepositoryImpl implements BusinessDayRepository {

      private BusinessDay businessDday;

      private TestBusinessDayRepositoryImpl(BusinessDay initialBusinessDday) {
         this.businessDday = initialBusinessDday;
      }

      @Override
      public BusinessDay findById(UUID objectId) throws ObjectNotFoundException {
         return businessDday;
      }

      @Override
      public BusinessDay findFirstOrCreateNew() {
         return businessDday;
      }

      @Override
      public BusinessDay save(BusinessDay businessDay) {
         this.businessDday = businessDay;
         return businessDay;
      }

      @Override
      public void deleteAll() {
         // nothing to do
      }
   }
}
