package com.adcubum.timerecording.api.businessday;

import static java.util.Objects.nonNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.adcubum.timerecording.api.common.BaseControllerExceptionHandler;
import com.adcubum.timerecording.core.work.businessday.exception.BusinessDayNotValidException;
import com.adcubum.timerecording.service.exception.BeginOrEndTimeParseException;
import com.adcubum.timerecording.service.exception.NoPersistentBusinessDayIncrementToChangeFoundException;

@ControllerAdvice
public class BusinessDayControllerExceptionHandler extends BaseControllerExceptionHandler {

   @ExceptionHandler(BusinessDayNotValidException.class)
   protected ResponseEntity<Object> handleBusinessDayNotValidException(BusinessDayNotValidException ex) {
      String errorName = nonNull(ex.getAttrName()) ? ex.getAttrName() : "Cause:";
      return createSingleErrorResponseEntity(errorName, ex);
   }

   @ExceptionHandler(BeginOrEndTimeParseException.class)
   protected ResponseEntity<Object> handleBeginOrEndTimeParseException(BeginOrEndTimeParseException ex) {
      return createSingleErrorResponseEntity("begin/end", ex);
   }

   @ExceptionHandler(NoPersistentBusinessDayIncrementToChangeFoundException.class)
   protected ResponseEntity<Object> handleNoPersistentBusinessDayIncrementToChangeFoundException(
         NoPersistentBusinessDayIncrementToChangeFoundException ex) {
      return createSingleErrorResponseEntity("no-business-day-to-change-found", ex);
   }
}
