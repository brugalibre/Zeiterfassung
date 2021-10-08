package com.adcubum.timerecording.model.businessdayconfig;

import com.adcubum.timerecording.app.businessday.businessdayconfig.BusinessDayConfig;
import com.adcubum.util.parser.NumberFormat;

public class BusinessDayConfigDto {

   private String hoursLeft;
   private String actualHours;
   private String setHours;

   public BusinessDayConfigDto(BusinessDayConfig businessDayConfig, float actualHours) {
      this.hoursLeft = NumberFormat.format(businessDayConfig.getHoursLeft(actualHours));
      this.actualHours = NumberFormat.format(actualHours);
      this.setHours = NumberFormat.format(businessDayConfig.getSetHours());
   }

   public String getHoursLeft() {
      return hoursLeft;
   }

   public String getSetHours() {
      return setHours;
   }

   public String getActualHours() {
      return actualHours;
   }
}
