package com.adcubum.timerecording.security.login.callback;

/**
 * The goal of this {@link LoginCallbackHandler} is to inform any interested about a login which is either done or aborted.
 * They do not care about the result.
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface LoginCallbackHandler {

   /**
    * Is called as soon as the login process was finish or aborted by the user
    * 
    * @param loginEndState
    *        the {@link LoginEndState}
    */
   void onLoginEnd(LoginEndState loginEndState);
}
