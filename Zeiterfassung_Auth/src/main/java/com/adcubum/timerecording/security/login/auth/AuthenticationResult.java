package com.adcubum.timerecording.security.login.auth;

/**
 * Contains the result of the user authentication
 * 
 * @author dominic
 *
 */
public class AuthenticationResult {
   private boolean isSuccessfull;

   public AuthenticationResult(boolean isSuccessfull) {
      this.isSuccessfull = isSuccessfull;
   }

   public boolean isSuccessfull() {
      return isSuccessfull;
   }
}
