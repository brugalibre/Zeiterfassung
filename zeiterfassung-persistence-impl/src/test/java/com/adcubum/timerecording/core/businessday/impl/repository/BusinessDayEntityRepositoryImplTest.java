package com.adcubum.timerecording.core.businessday.impl.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.businessday.comeandgo.dao.ComeAndGoesDao;
import com.adcubum.timerecording.core.businessday.dao.BusinessDayDao;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;


class BusinessDayEntityRepositoryImplTest {

   @Test
   void testFindFirstOrCreateNew() {

      // Given
      ComeAndGoesDao comeAndGoesDao = mock(ComeAndGoesDao.class);
      BusinessDayDao businessDayDao = mock(BusinessDayDao.class);
      BusinessDayEntityRepositoryImpl businessDayEntityRepository = new BusinessDayEntityRepositoryImpl(businessDayDao, comeAndGoesDao);

      BusinessDayEntity first = new BusinessDayEntity();
      List<BusinessDayEntity> businessDayEntities = mockBusinessDayEntity(first, new BusinessDayEntity(), new BusinessDayEntity());
      when(businessDayDao.findAll()).thenReturn(businessDayEntities);

      // When
      BusinessDayEntity actualBusinessDayEntity = businessDayEntityRepository.findFirstOrCreateNew();

      // Then
      assertThat(actualBusinessDayEntity, is(first));

   }

   private static List<BusinessDayEntity> mockBusinessDayEntity(BusinessDayEntity... businessDayEntities) {
      List<BusinessDayEntity> mockedBusinessDayEntities = new LinkedList<>();
      mockedBusinessDayEntities.addAll(Arrays.asList(businessDayEntities));
      return mockedBusinessDayEntities;
   }

}
