package com.adcubum.timerecording.core.businessday.repository.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adcubum.timerecording.core.businessday.entity.repository.BusinessDayEntityRepository;

@Component("business-day-entity-repository-factory")
public class BusinessDayEntityRepositoryHolder {

   private static BusinessDayEntityRepository businessDayEntityRepository;

   @Autowired
   public void setBusinessDayEntityRepository(BusinessDayEntityRepository businessDayEntityRepository) {
      BusinessDayEntityRepositoryHolder.businessDayEntityRepository = businessDayEntityRepository;
   }

   public static BusinessDayEntityRepository getBusinessDayEntityRepository() {
      return businessDayEntityRepository;
   }
}
