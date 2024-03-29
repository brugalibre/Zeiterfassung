package com.adcubum.timerecording.security.login.auth.usercredentials.factory;

import com.adcubum.timerecording.security.login.auth.configuration.impl.ConfigurationImpl;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;

import static com.adcubum.timerecording.security.login.auth.configuration.constant.AuthenticationConst.TICKET_SYSTEM_PROPERTIES;

/**
 * Responsible for creating the {@link UserCredentialAuthenticatorFactory} which matches to the given login configuration.
 *
 * @author Dominic
 */
public class UserCredentialAuthenticatorFactory {

   private static final String TICKET_SYSTEM_NAME = "TicketSystem";

   private UserCredentialAuthenticatorFactory() {
      // private
   }

   /**
    * @return the {@link UserCredentialsAuthenticator}
    */
   public static UserCredentialsAuthenticator getUserCredentialsAuthenticator() {
      return createUserCredentialsAuthenticator();
   }

   private static UserCredentialsAuthenticator createUserCredentialsAuthenticator() {
      // the authentication is depending on the configured ticket-system.
      String ticketSystemName = new ConfigurationImpl(TICKET_SYSTEM_PROPERTIES).getValue(TICKET_SYSTEM_NAME);
      return UserCredentialsAuthenticatorType.valueOf(ticketSystemName.toUpperCase())
              .createUserCredentialAuthenticator();
   }

}
