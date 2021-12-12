package com.adcubum.timerecording.api.timerecorder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.adcubum.timerecording.api.common.BaseControllerExceptionHandler;
import com.adcubum.timerecording.service.timerecorder.StartRecordingNotPossibleException;

@ControllerAdvice
public class TimeRecorderControllerExceptionHandler extends BaseControllerExceptionHandler {

   @ExceptionHandler(StartRecordingNotPossibleException.class)
   protected ResponseEntity<Object> handleStartRecordingNotPossibleException(StartRecordingNotPossibleException ex) {
      return createSingleErrorResponseEntity(ex.getErrorTitle(), ex);
   }
}
