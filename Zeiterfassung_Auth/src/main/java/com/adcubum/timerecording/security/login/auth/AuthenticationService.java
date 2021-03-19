package com.adcubum.timerecording.security.login.auth;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

import com.adcubum.timerecording.security.login.auth.init.UserAuthenticatedObservable;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialAuthenticatorFactory;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;

public class AuthenticationService {

   /** The singleton instance of the {@link AuthenticationService} */
   public static final AuthenticationService INSTANCE = new AuthenticationService();

   private Set<UserAuthenticatedObservable> userAuthenticatedObservables;
   private UserCredentialsAuthenticator userCredentialsAuthenticator;
   private boolean isAuthenticated;
   private String username;

   private AuthenticationService() {
      this(UserCredentialAuthenticatorFactory.INSTANCE.getUserCredentialsAuthenticator());
   }

   /**
    * Constructor only for testing purpose!
    * 
    * @param userCredentialsAuthenticator
    *        the {@link UserCredentialsAuthenticator}
    */
   AuthenticationService(UserCredentialsAuthenticator userCredentialsAuthenticator) {
      this.username = "";
      this.userCredentialsAuthenticator = userCredentialsAuthenticator;
      this.userAuthenticatedObservables = new HashSet<>();
   }

   /**
    * Registers this {@link UserAuthenticatedObservable} to this {@link AuthenticationService}
    * 
    * @param userAuthenticatedObservable
    */
   public void registerUserAuthenticatedObservable(UserAuthenticatedObservable userAuthenticatedObservable) {
      this.userAuthenticatedObservables.add(userAuthenticatedObservable);
   }

   /**
    * Does the authentication of the given username and password. Additionally the
    * password is stored within the {@link SecureStorage}
    * 
    * @param username
    *        the username
    * @param pw
    *        the password
    * @return an {@link AuthenticationResult}
    */
   public AuthenticationResult doUserAuthentication(String username, char[] pw) {
      this.username = requireNonNull(username);
      isAuthenticated = userCredentialsAuthenticator.doUserAuthentication(username, pw);
      if (isAuthenticated) {
         broadcastUserAuthenticated(pw);
      }
      return new AuthenticationResult(isAuthenticated);
   }

   private void broadcastUserAuthenticated(char[] userPwd) {
      AuthenticationContext authenticationContext = new AuthenticationContext(username, () -> userPwd);
      userAuthenticatedObservables.stream()
            .forEach(userAuthenticatedSensible -> userAuthenticatedSensible.userAuthenticated(authenticationContext));
   }

   /**
    * @return the username of the authenticated user
    */
   public String getUsername() {
      return username;
   }

   /**
    * @return <code>true</code> if the current user is authenticated or <code>false</code> if not
    */
   public boolean isUserAuthenticated() {
      return isAuthenticated;
   }

   public void setUserCredentialsAuthenticator(UserCredentialsAuthenticator userCredentialsAuthenticator) {
      this.userCredentialsAuthenticator = userCredentialsAuthenticator;
   }
}
