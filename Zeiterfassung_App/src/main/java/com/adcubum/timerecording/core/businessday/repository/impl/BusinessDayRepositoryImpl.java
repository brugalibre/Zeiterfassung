package com.adcubum.timerecording.core.businessday.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.businessday.entity.repository.BusinessDayEntityRepository;
import com.adcubum.timerecording.core.businessday.repository.factory.BusinessDayEntityRepositoryHolder;
import com.adcubum.timerecording.core.repository.ObjectNotFoundException;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.mapper.BusinessDayEntityMapper;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeUtil;

/**
 * The {@link BusinessDayRepository} serves as a dao on the "business" side of the application, since the actual persistence-repository
 * cannot known the core-api, hence it cannot map into a {@link BusinessDay}
 * 
 * @author DStalder
 *
 */
public class BusinessDayRepositoryImpl implements BusinessDayRepository {

   private BusinessDayEntityRepository businessDayEntityRepository;

   public BusinessDayRepositoryImpl() {
      businessDayEntityRepository = BusinessDayEntityRepositoryHolder.getBusinessDayEntityRepository();
   }

   @Override
   public BusinessDay findFirstOrCreateNew() {
      BusinessDayEntity businessDayEntity = businessDayEntityRepository.findFirstOrCreateNew();
      return BusinessDayEntityMapper.INSTANCE.map2BusinessDay(businessDayEntity);
   }

   @Override
   public BusinessDay findBookedBusinessDayByDate(DateTime time) {
      DateTime lowerBounds = DateTimeUtil.getBeginOfDay(time);
      DateTime upperBounds = DateTimeUtil.getEndOfDay(time);
      Optional<BusinessDayEntity> businessDayEntityOpt = businessDayEntityRepository.findBookedBusinessDayEntityWithinRange(lowerBounds, upperBounds);
      return businessDayEntityOpt
            .map(BusinessDayEntityMapper.INSTANCE::map2BusinessDay)
            .orElse(null);
   }

   @Override
   public List<BusinessDay> findBookedBussinessDaysWithinRange(DateTime lowerBounds, DateTime upperBounds) {
      return businessDayEntityRepository.findAllBookedBusinessDayEntitiesWithinRange(lowerBounds, upperBounds)
            .stream()
            .map(BusinessDayEntityMapper.INSTANCE::map2BusinessDay)
            .collect(Collectors.toList());
   }

   @Override
   public BusinessDay createNew(boolean isBooked) {
      BusinessDayEntity businessDayEntity = businessDayEntityRepository.createNew(isBooked);
      return BusinessDayEntityMapper.INSTANCE.map2BusinessDay(businessDayEntity);
   }

   @Override
   public BusinessDay findById(UUID businessDayId) throws ObjectNotFoundException {
      BusinessDayEntity businessDayEntity = businessDayEntityRepository.findById(businessDayId);
      return BusinessDayEntityMapper.INSTANCE.map2BusinessDay(businessDayEntity);
   }

   @Override
   public BusinessDay save(BusinessDay businessDay) {
      BusinessDayEntity businessDayEntity = BusinessDayEntityMapper.INSTANCE.map2BusinessDayEntity(businessDay);
      BusinessDayEntity changedBusinessDayEntity = businessDayEntityRepository.save(businessDayEntity);
      return BusinessDayEntityMapper.INSTANCE.map2BusinessDay(changedBusinessDayEntity);
   }

   @Override
   public void deleteAll(boolean isBooked) {
      businessDayEntityRepository.deleteAll(isBooked);
   }

   @Override
   public void deleteBookedBusinessDaysWithinRange(DateTime lowerBounds, DateTime upperBounds) {
      businessDayEntityRepository.deleteBookedBusinessDaysWithinRange(lowerBounds, upperBounds);
   }
}
