
package com.adcubum.timerecording.security.login.auth;

import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;

public class TestAuthenticationService extends AuthenticationService {

   public TestAuthenticationService(UserCredentialsAuthenticator userCredentialsAuthenticator) {
      super(userCredentialsAuthenticator);
   }

   public static TestAuthenticationService getNewTestAuthenticationService(UserCredentialsAuthenticator userCredentialsAuthenticator) {
      return new TestAuthenticationService(userCredentialsAuthenticator);
   }
}
