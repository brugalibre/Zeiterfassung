package com.adcubum.timerecording.model.timerecorder;

import com.adcubum.timerecording.app.TimeRecorder;

/**
 * The {@link TimeRecorderDto} represents the current status of the {@link TimeRecorder}
 * 
 * @author dstalder
 *
 */
public class TimeRecorderDto {

   private String statusMsg;
   private boolean isRecording;
   private boolean isBookingPossible;

   public TimeRecorderDto(TimeRecorder timeRecorder) {
      this.isRecording = timeRecorder.isRecording();
      this.statusMsg = timeRecorder.getInfoStringForState();
      this.isBookingPossible = !timeRecorder.isBooking() && timeRecorder.hasNotChargedElements();
   }

   public String getStatusMsg() {
      return statusMsg;
   }

   public boolean getIsRecording() {
      return isRecording;
   }

   public boolean getIsBookingPossible() {
      return isBookingPossible;
   }
}
