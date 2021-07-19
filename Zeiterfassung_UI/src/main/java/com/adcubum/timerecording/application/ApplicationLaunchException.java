package com.adcubum.timerecording.application;

public class ApplicationLaunchException extends RuntimeException {

   public ApplicationLaunchException(Exception e) {
      super(e);
   }

   public ApplicationLaunchException(String message) {
      super(message);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

}
