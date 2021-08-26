package com.adcubum.timerecording.app.businessday;

import static java.util.Objects.nonNull;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;

public class BusinessDayHelperImpl implements BusinessDayHelper {

   private BusinessDayRepository businessDayRepository;
   private UUID businessDayId;

   public BusinessDayHelperImpl(BusinessDayRepository businessDayRepository) {
      this.businessDayRepository = businessDayRepository;
   }

   @Override
   public BusinessDay getBusinessDay() {
      if (nonNull(businessDayId)) {
         return businessDayRepository.findById(businessDayId);
      }
      return null;
   }

   @Override
   public BusinessDay save(BusinessDay newBusinessDay) {
      return saveAndUpdate(newBusinessDay);
   }

   private BusinessDay saveAndUpdate(BusinessDay newBusinessDay) {
      BusinessDay businessDay = businessDayRepository.save(newBusinessDay);
      this.businessDayId = businessDay.getId();
      return businessDay;
   }

   @Override
   public void createNew() {
      loadExistingOrCreateNew();
   }

   @Override
   public BusinessDay loadExistingOrCreateNew() {
      BusinessDay businessDay = businessDayRepository.findFirstOrCreateNew();
      this.businessDayId = businessDay.getId();
      return businessDay;
   }
}
