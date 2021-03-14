package com.adcubum.timerecording.security.login.auth.usercredentials;

/**
 * The {@link UserCredentialsAuthenticator} is a wrapper for the actual authentication of the provided username and password
 * 
 * @author DStalder
 *
 */
@FunctionalInterface
public interface UserCredentialsAuthenticator {

   /**
    * Does the actual authentication
    * 
    * @param username
    *        the username
    * @param userPwd
    *        the user password
    * @return <code>true</code> if the user was successfully authenticated or <code>false</code> if not
    */
   public boolean doUserAuthentication(String username, char[] userPwd);
}
