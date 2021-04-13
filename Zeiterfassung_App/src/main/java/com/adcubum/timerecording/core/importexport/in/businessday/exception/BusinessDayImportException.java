package com.adcubum.timerecording.core.importexport.in.businessday.exception;

public class BusinessDayImportException extends RuntimeException {

   public BusinessDayImportException(Exception e) {
      super(e);
   }

   public BusinessDayImportException(String message) {
      super(message);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

}
