package com.adcubum.timerecording.model.setactual;

import com.adcubum.timerecording.app.setactual.SetActualWorkingHours;
import com.adcubum.util.parser.NumberFormat;

public class SetActualWorkingHoursDto {

   private String hoursLeft;
   private String actualHours;

   public SetActualWorkingHoursDto(SetActualWorkingHours setActualWorkingHours, float actualHours) {
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
