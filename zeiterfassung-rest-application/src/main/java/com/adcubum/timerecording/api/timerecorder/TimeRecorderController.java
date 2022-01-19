package com.adcubum.timerecording.api.timerecorder;

import com.adcubum.timerecording.model.businessday.AddNewBusinessDayIncrementDto;
import com.adcubum.timerecording.model.timerecorder.ApplicationTitle;
import com.adcubum.timerecording.model.timerecorder.TimeRecorderDto;
import com.adcubum.timerecording.service.timerecorder.TimeRecorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

   @GetMapping("/getApplicationTitle")
   @ResponseBody
   public ApplicationTitle getApplicationTitle(){
      return new ApplicationTitle(timeRecorderService.getApplicationTitle());
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
