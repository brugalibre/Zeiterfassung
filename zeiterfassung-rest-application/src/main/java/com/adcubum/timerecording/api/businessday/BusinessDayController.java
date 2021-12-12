package com.adcubum.timerecording.api.businessday;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcubum.timerecording.model.businessday.BusinessDayDto;
import com.adcubum.timerecording.model.businessday.BusinessDayIncrementDto;
import com.adcubum.timerecording.service.businessday.impl.BusinessDayService;

@RequestMapping("/api/v1/businessday")
@RestController
public class BusinessDayController {

   private BusinessDayService businessDayService;

   @Autowired
   public BusinessDayController(BusinessDayService businessDayService) {
      this.businessDayService = businessDayService;
   }

   @PostMapping
   public BusinessDayIncrementDto addBusinessDayIncrement(@Valid @NonNull @RequestBody BusinessDayIncrementDto businessDayIncrementDto) {
      return businessDayService.addBusinessDayIncrementDto(businessDayIncrementDto);
   }

   @PutMapping
   public BusinessDayIncrementDto changeBusinessDayIncrement(@Valid @NonNull @RequestBody BusinessDayIncrementDto businessDayIncrementDto) {
      return businessDayService.changeBusinessDayIncrementDto(businessDayIncrementDto);
   }

   @GetMapping
   public BusinessDayDto getBusinessDayDto() {
      return businessDayService.getBusinessDayDto();
   }

   @DeleteMapping(path = "{id}")
   public int deleteBusinessDay(@PathVariable("id") UUID id) {
      return businessDayService.deleteBusinessDayIncrementDto(id);
   }

   @DeleteMapping("/deleteAll")
   public int deleteAll() {
      return businessDayService.deleteAll();
   }
}
