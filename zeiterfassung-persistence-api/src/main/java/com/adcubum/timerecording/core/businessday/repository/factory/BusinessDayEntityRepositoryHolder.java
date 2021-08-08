package com.adcubum.timerecording.core.businessday.repository.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adcubum.timerecording.core.businessday.entity.repository.BusinessDayEntityRepository;

@Component(BusinessDayEntityRepositoryHolder.BUSINESS_DAY_ENTITY_REPOSITORY_FACTORY)
public class BusinessDayEntityRepositoryHolder {

   /** Name of the bean */
   public static final String BUSINESS_DAY_ENTITY_REPOSITORY_FACTORY = "business-day-entity-repository-factory";
   private static BusinessDayEntityRepository businessDayEntityRepository;

   @Autowired
   public void setBusinessDayEntityRepository(BusinessDayEntityRepository businessDayEntityRepository) {
      BusinessDayEntityRepositoryHolder.businessDayEntityRepository = businessDayEntityRepository;
   }

   public static BusinessDayEntityRepository getBusinessDayEntityRepository() {
      return businessDayEntityRepository;
   }
}
