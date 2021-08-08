package com.adcubum.timerecording.api.setactualworkinghours;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcubum.timerecording.model.setactual.SetActualWorkingHoursDto;
import com.adcubum.timerecording.service.setactualworkinghours.SetActualWorkingHoursService;

@RequestMapping("/api/v1/setactualworkinghours")
@RestController
public class SetActualWorkingHoursController {

   private SetActualWorkingHoursService actualSetWorkingHoursService;

   @Autowired
   public SetActualWorkingHoursController(SetActualWorkingHoursService setActualWorkingHoursService) {
      this.actualSetWorkingHoursService = setActualWorkingHoursService;
   }

   @GetMapping
   public SetActualWorkingHoursDto getActualSetWorkingHoursDto() {
      return actualSetWorkingHoursService.getSetActualWorkingHoursDto();
   }
}
