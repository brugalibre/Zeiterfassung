package com.adcubum.timerecording.security.login.auth.init;

import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;

/**
 * The {@link UserAuthenticatedObservable} marks objects which cares about, when the user has been authenticated
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface UserAuthenticatedObservable {

   /**
    * Is called by the {@link AuthenticationService} as soon as the user was authenticated
    * 
    * @param authenticationContext
    *        the {@link AuthenticationContext} with the login informations
    */
   void userAuthenticated(AuthenticationContext authenticationContext);

}
