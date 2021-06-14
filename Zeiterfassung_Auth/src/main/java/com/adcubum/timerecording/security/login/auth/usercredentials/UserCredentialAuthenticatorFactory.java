package com.adcubum.timerecording.security.login.auth.usercredentials;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * Responsible for creating the {@link UserCredentialAuthenticatorFactory} which matches to the given login configuration.
 * 
 * @author Dominic
 *
 */
public class UserCredentialAuthenticatorFactory extends AbstractFactory {

   private static final String BEAN_NAME = "usercredentials";
   private static final UserCredentialAuthenticatorFactory INSTANCE = new UserCredentialAuthenticatorFactory();

   private UserCredentialAuthenticatorFactory() {
      super("modul-configration.xml");
   }

   /**
    * @return the {@link UserCredentialAuthenticator}
    */
   public static UserCredentialsAuthenticator getUserCredentialsAuthenticator() {
      return INSTANCE.createNew(BEAN_NAME);
   }
}
