package com.adcubum.timerecording.api.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class BaseControllerExceptionHandler {

   @ExceptionHandler(MethodArgumentNotValidException.class)
   protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult()
            .getAllErrors()
            .forEach(putError(errors));
      return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
   }

   @ExceptionHandler(IllegalStateException.class)
   protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
      return createSingleErrorResponseEntity("internal-server-error", ex);
   }

   @ExceptionHandler(NullPointerException.class)
   protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
      return createSingleErrorResponseEntity("internal-server-error", ex);
   }

   private static Consumer<ObjectError> putError(Map<String, String> errors) {
      return error -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      };
   }

   /**
    * Creates a {@link ResponseEntity} with exactly one message.
    * The {@link ResponseEntity} has a HTTP-Status {@link HttpStatus#UNPROCESSABLE_ENTITY}
    * 
    * @param errorName
    *        the name of the error
    * @param ex
    *        the exception
    * @return a new {@link ResponseEntity}
    */
   protected ResponseEntity<Object> createSingleErrorResponseEntity(String errorName, Exception ex) {
      Map<String, String> errors = new HashMap<>();
      errors.put(errorName, ex.getLocalizedMessage());
      return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
   }
}
