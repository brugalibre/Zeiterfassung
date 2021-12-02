package com.adcubum.timerecording.proles.security.login.auth.usercredentials;

import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;
import com.zeiterfassung.web.proles.login.ProlesLogin;

public class ProesUserCredentialsAuthenticatorImpl implements UserCredentialsAuthenticator {

   @Override
   public boolean doUserAuthentication(String username, char[] userPwd) {
      ProlesLogin prolesLogin = ProlesLogin.createProlesTicketExtractor(username, String.valueOf(userPwd));
      return prolesLogin.doLogin();
   }
}
