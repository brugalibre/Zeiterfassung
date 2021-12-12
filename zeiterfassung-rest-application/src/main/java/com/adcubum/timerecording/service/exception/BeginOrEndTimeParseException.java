package com.adcubum.timerecording.service.exception;

import java.text.ParseException;

public class BeginOrEndTimeParseException extends RuntimeException {

   public BeginOrEndTimeParseException(ParseException e) {
      super(e);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

}
