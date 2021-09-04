package com.adcubum.timerecording.core.businessday.impl.repository;

import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.businessday.comeandgo.dao.ComeAndGoesDao;
import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoesEntity;
import com.adcubum.timerecording.core.businessday.dao.BusinessDayDao;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;


class BusinessDayEntityRepositoryImplTest {

   @Test
   void testFfindFirstOrCreateNew_FindExisting() {

      // Given
      boolean isBooked = false;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withExistingUuid(UUID.randomUUID(), isBooked)
            .build();

      // When
      BusinessDayEntity actualBusinessDayEntity = tcb.businessDayEntityRepository.findFirstOrCreateNew();

      // Then
      assertThat(actualBusinessDayEntity, is(tcb.existingBusinessDayEntity));
   }

   @Test
   void testFfindFirstOrCreateNew_CreateNew() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .build();

      // When
      BusinessDayEntity actualBusinessDayEntity = tcb.businessDayEntityRepository.findFirstOrCreateNew();

      // Then
      assertThat(actualBusinessDayEntity, is(tcb.newCreatedBusinessDayEntity));
   }

   private static class TestCaseBuilder {
      private ComeAndGoesDao comeAndGoesDao;
      private BusinessDayDao businessDayDao;
      private BusinessDayEntityRepositoryImpl businessDayEntityRepository;
      private BusinessDayEntity existingBusinessDayEntity; // the first business-day, which is returned if we found more than one
      private List<UUID> businessDayEntityIds; // all persistent businessday-ids
      private BusinessDayEntity newCreatedBusinessDayEntity;

      private TestCaseBuilder() {
         this.comeAndGoesDao = mock(ComeAndGoesDao.class);
         this.businessDayDao = mock(BusinessDayDao.class);
         this.businessDayEntityIds = new ArrayList<>();
         this.businessDayEntityRepository = new BusinessDayEntityRepositoryImpl(businessDayDao, comeAndGoesDao);
      }

      private TestCaseBuilder withExistingUuid(UUID existingId, boolean isBooked) {
         this.existingBusinessDayEntity = new BusinessDayEntity(existingId, isBooked);
         businessDayEntityIds.add(0, existingId);
         return this;
      }

      private TestCaseBuilder build() {
         mockBusinessDayDao();
         mockComeAndGoesDao();
         return this;
      }

      private void mockComeAndGoesDao() {
         if (nonNull(existingBusinessDayEntity)) {
            when(comeAndGoesDao.save(existingBusinessDayEntity.getComeAndGoesEntity())).thenReturn(existingBusinessDayEntity.getComeAndGoesEntity());
         }
      }

      private void mockBusinessDayDao() {
         if (nonNull(existingBusinessDayEntity)) {
            when(businessDayDao.findById(eq(existingBusinessDayEntity.getId()))).thenReturn(Optional.of(existingBusinessDayEntity));
            when(businessDayDao.save(eq(existingBusinessDayEntity))).thenReturn(existingBusinessDayEntity);
         } else {
            this.newCreatedBusinessDayEntity = new BusinessDayEntity();
            when(businessDayDao.save(any())).thenReturn(newCreatedBusinessDayEntity);
            when(comeAndGoesDao.save(any())).thenReturn(new ComeAndGoesEntity());
         }
         when(businessDayDao.findAllBusinessDayIds4BookingStatus(eq(false))).thenReturn(businessDayEntityIds);
      }
   }

}
