package com.adcubum.timerecording.service.timerecorder;

import com.adcubum.timerecording.message.Message;

public class StartRecordingNotPossibleException extends RuntimeException {

   private static final long serialVersionUID = 1L;
   private final String errorTitle;

   public StartRecordingNotPossibleException(Message message) {
      super(message.getMessage());
      this.errorTitle = message.getMessageTitle();
   }

   public String getErrorTitle() {
      return errorTitle;
   }
}
