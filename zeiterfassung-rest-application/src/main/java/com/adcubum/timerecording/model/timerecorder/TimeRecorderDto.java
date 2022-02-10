package com.adcubum.timerecording.model.timerecorder;

import com.adcubum.timerecording.app.TimeRecorder;

/**
 * The {@link TimeRecorderDto} represents the current status of the {@link TimeRecorder}
 *
 * @author dstalder
 */
public class TimeRecorderDto {

   private String statusMsg;
   private boolean isBookingPossible;
   private ApplicationStatus applicationStatus;

   public TimeRecorderDto(TimeRecorder timeRecorder) {
      this.statusMsg = timeRecorder.getInfoStringForState();
      this.isBookingPossible = !timeRecorder.isBooking() && timeRecorder.hasNotChargedElements();
      this.applicationStatus = evalApplicationStatus(timeRecorder);
   }

   private static ApplicationStatus evalApplicationStatus(TimeRecorder timeRecorder) {
      if (timeRecorder.isRecording()) {
         return ApplicationStatus.RECORDING;
      } else if (timeRecorder.isBooking()) {
         return ApplicationStatus.BOOKING;
      }
      if (timeRecorder.isComeAndGoActive()) {
         return ApplicationStatus.COME_AND_GO;
      }
      return ApplicationStatus.IDLE;
   }

   public ApplicationStatus getApplicationStatus() {
      return applicationStatus;
   }

   public String getStatusMsg() {
      return statusMsg;
   }

   public boolean getIsBookingPossible() {
      return isBookingPossible;
   }
}
