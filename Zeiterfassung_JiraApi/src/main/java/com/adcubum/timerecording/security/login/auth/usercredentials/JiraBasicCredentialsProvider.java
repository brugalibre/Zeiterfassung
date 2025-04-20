package com.adcubum.timerecording.security.login.auth.usercredentials;

import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import okhttp3.Credentials;

/**
 * The {@link JiraBasicCredentialsProvider} is a {@link JiraCredentialsProvider} based on {@link Credentials#basic(String, String)}
 */
public class JiraBasicCredentialsProvider implements JiraCredentialsProvider {

   private String credentials;

   public JiraBasicCredentialsProvider() {
      this.credentials = "";
      AuthenticationService.INSTANCE.registerUserAuthenticatedObservable(this);
   }

   @Override
   public void userAuthenticated(AuthenticationContext autCtx) {
      this.credentials = Credentials.basic(autCtx.getUsername(), String.valueOf(autCtx.getUserPw()));
   }

   public String getCredentials() {
      return credentials;
   }
}
