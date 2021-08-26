package com.adcubum.timerecording.service.businessdayconfig;

import org.springframework.stereotype.Service;

import com.adcubum.timerecording.app.businessday.businessdayconfig.BusinessDayConfig;
import com.adcubum.timerecording.model.businessday.BusinessDayDto;
import com.adcubum.timerecording.model.businessdayconfig.BusinessDayConfigDto;
import com.adcubum.timerecording.service.businessday.impl.BusinessDayService;

@Service
public class BusinessDayConfigService {

   private BusinessDayService businessDayService;

   public BusinessDayConfigService(BusinessDayService businessDayService) {
      this.businessDayService = businessDayService;
   }

   /**
    * @return the {@link BusinessDayConfigDto}
    */
   public BusinessDayConfigDto getBusinessDayConfigDto() {
      BusinessDayDto businessDayDto = businessDayService.getBusinessDayDto();
      float duration = businessDayDto.getTotalDuration();
      BusinessDayConfig businessDayConfig = new BusinessDayConfig();
      return new BusinessDayConfigDto(businessDayConfig, duration);
   }
}
