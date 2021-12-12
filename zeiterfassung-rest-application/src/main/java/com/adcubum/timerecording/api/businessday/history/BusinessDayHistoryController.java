package com.adcubum.timerecording.api.businessday.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcubum.timerecording.model.businessday.history.BusinessDayHistoryOverviewDto;
import com.adcubum.timerecording.service.businessday.impl.BusinessDayService;

@RequestMapping("/api/v1/businessday-history")
@RestController
public class BusinessDayHistoryController {

   private BusinessDayService businessDayService;

   @Autowired
   public BusinessDayHistoryController(BusinessDayService businessDayService) {
      this.businessDayService = businessDayService;
   }

   @GetMapping
   public BusinessDayHistoryOverviewDto getBusinessDayHistoryDto() {
      return businessDayService.getBusinessDayHistoryDto();
   }
}
