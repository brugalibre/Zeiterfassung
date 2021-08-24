package com.adcubum.timerecording.api.businessdayconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcubum.timerecording.model.businessdayconfig.BusinessDayConfigDto;
import com.adcubum.timerecording.service.businessdayconfig.BusinessDayConfigService;

@RequestMapping("/api/v1/businessdayconfig")
@RestController
public class BusinessDayConfigController {

   private BusinessDayConfigService businessDayConfigService;

   @Autowired
   public BusinessDayConfigController(BusinessDayConfigService businessDayConfigService) {
      this.businessDayConfigService = businessDayConfigService;
   }

   @GetMapping
   public BusinessDayConfigDto getBusinessDayConfigDto() {
      return businessDayConfigService.getBusinessDayConfigDto();
   }
}
