package com.myownb3.dominic.launch.exception;

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
