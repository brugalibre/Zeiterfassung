package com.adcubum.timerecording.model.businessdayconfig;

import com.adcubum.timerecording.app.businessday.businessdayconfig.BusinessDayConfig;
import com.adcubum.util.parser.NumberFormat;

public class BusinessDayConfigDto {

   private String hoursLeft;
   private String actualHours;

   public BusinessDayConfigDto(BusinessDayConfig setActualWorkingHours, float actualHours) {
      this.hoursLeft = NumberFormat.format(setActualWorkingHours.getHoursLeft(actualHours));
      this.actualHours = NumberFormat.format(actualHours);
   }

   public String getHoursLeft() {
      return hoursLeft;
   }

   public String getActualHours() {
      return actualHours;
   }
}
