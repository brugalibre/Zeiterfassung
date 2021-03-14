package com.adcubum.timerecording.security.login.auth.usercredentials;

import com.adcubum.timerecording.security.login.auth.usercredentials.impl.UserCredentialsAuthenticatorImpl;

/**
 * Responsible for creating the {@link UserCredentialAuthenticatorFactory} which matches to the given login configuration.
 * Actually this should return always a proper authenticator which uses a microsof-AD API or something else..
 * Anyway because I'm lacking of time and knwoledge, the workarround is to simply fetch a well-known jira ticket if we use the cool-guys
 * turbo-bucher (yes indeed, this is very shaky)
 * or we call make a test-call to the abacus service (basically a login) in order to verify the credentials)
 * Both attemps are horrible but at least we get some feedback
 * 
 * @author Dominic
 *
 */
public class UserCredentialAuthenticatorFactory {

   public static final UserCredentialAuthenticatorFactory INSTANCE = new UserCredentialAuthenticatorFactory(false);
   private boolean usesAbacusApi;

   UserCredentialAuthenticatorFactory(boolean usesAbacusApi) {
      this.usesAbacusApi = usesAbacusApi;
   }

   /**
    * @return the {@link UserCredentialAuthenticator}
    */
   public UserCredentialsAuthenticator getUserCredentialsAuthenticator() {
      return usesAbacusApi ? new UserCredentialsAuthenticatorImpl() : new UserCredentialsAuthenticatorImpl();
   }
}
