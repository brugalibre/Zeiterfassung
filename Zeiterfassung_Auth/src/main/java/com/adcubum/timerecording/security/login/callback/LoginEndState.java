package com.adcubum.timerecording.security.login.callback;

/**
 * Defines the different end states of the login
 * 
 * @author Dominic
 *
 */
public enum LoginEndState {

   /** The login was finished properly */
   SUCCESSFULLY,

   /** The login was finished with a failure */
   FAILED,

   /** The login was aborted */
   ABORTED
}
