package com.adcubum.timerecording.api.timerecorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcubum.timerecording.model.businessday.AddNewBusinessDayIncrementDto;
import com.adcubum.timerecording.model.timerecorder.TimeRecorderDto;
import com.adcubum.timerecording.service.timerecorder.TimeRecorderService;

@RequestMapping("/api/v1/timerecorder")
@RestController
public class TimeRecorderController {

   private TimeRecorderService timeRecorderService;

   @Autowired
   public TimeRecorderController(TimeRecorderService timeRecorderService) {
      this.timeRecorderService = timeRecorderService;
   }

   @GetMapping
   public TimeRecorderDto getTimeRecorderDto() {
      return timeRecorderService.getTimeRecorderDto();
   }

   @PostMapping("/startStopRecording")
   public AddNewBusinessDayIncrementDto startStopRecording() {
      return timeRecorderService.startStopRecording();
   }

   @PostMapping("/resume")
   public int resume() {
      return timeRecorderService.resume();
   }

   @PostMapping("/book")
   public int book() {
      return timeRecorderService.book();
   }
}
