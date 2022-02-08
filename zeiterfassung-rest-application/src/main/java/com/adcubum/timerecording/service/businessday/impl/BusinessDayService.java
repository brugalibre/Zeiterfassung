package com.adcubum.timerecording.service.businessday.impl;

import com.adcubum.timerecording.model.businessday.BusinessDayDto;
import com.adcubum.timerecording.model.businessday.BusinessDayIncrementDto;
import com.adcubum.timerecording.model.businessday.history.BusinessDayHistoryOverviewDto;
import com.adcubum.timerecording.service.businessday.BusinessDayServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BusinessDayService {

   private BusinessDayServiceHelper businessDayServiceHelper;

   @Autowired
   public BusinessDayService(@Qualifier("BusinessDayServiceHelper") BusinessDayServiceHelper businessDayServiceHelper) {
      this.businessDayServiceHelper = businessDayServiceHelper;
   }

   public BusinessDayDto getBusinessDayDto() {
      return businessDayServiceHelper.getBusinessDayDto();
   }

   public BusinessDayHistoryOverviewDto getBusinessDayHistoryDto() {
      return businessDayServiceHelper.getBusinessDayHistoryDto();
   }

   public BusinessDayIncrementDto addBusinessDayIncrementDto(BusinessDayIncrementDto businessDayIncrementDto) {
      return businessDayServiceHelper.addBusinessDayIncrement(businessDayIncrementDto);
   }

   public BusinessDayIncrementDto changeBusinessDayIncrementDto(BusinessDayIncrementDto businessDayIncrementDto) {
      return businessDayServiceHelper.changeBusinessDayIncrementDto(businessDayIncrementDto);
   }

   public int deleteBusinessDayIncrementDto(UUID id) {
      return businessDayServiceHelper.deleteBusinessDayIncrementDto(id);
   }

   public int deleteAll() {
      return businessDayServiceHelper.deleteAllBusinessDayIncrements();
   }
}
