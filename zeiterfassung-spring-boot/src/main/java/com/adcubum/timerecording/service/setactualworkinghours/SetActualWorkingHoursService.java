package com.adcubum.timerecording.service.setactualworkinghours;

import org.springframework.stereotype.Service;

import com.adcubum.timerecording.app.setactual.SetActualWorkingHours;
import com.adcubum.timerecording.model.businessday.BusinessDayDto;
import com.adcubum.timerecording.model.setactual.SetActualWorkingHoursDto;
import com.adcubum.timerecording.service.businessday.impl.BusinessDayService;

@Service
public class SetActualWorkingHoursService {

   private BusinessDayService businessDayService;

   public SetActualWorkingHoursService(BusinessDayService businessDayService) {
      this.businessDayService = businessDayService;
   }

   /**
    * @return the {@link SetActualWorkingHoursDto}
    */
   public SetActualWorkingHoursDto getSetActualWorkingHoursDto() {
      BusinessDayDto businessDayDto = businessDayService.getBusinessDayDto();
      float duration = businessDayDto.getTotalDuration();
      SetActualWorkingHours setActualWorkingHours = new SetActualWorkingHours();
      return new SetActualWorkingHoursDto(setActualWorkingHours, duration);
   }
}
