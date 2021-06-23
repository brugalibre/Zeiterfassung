package com.adcubum.timerecording.security.login.auth.constant;

import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

public class AuthenticationConst {

   private AuthenticationConst() {
      // private 
   }

   public static final ValueKey<String> AUTHENTICATION_VAULT_URL_KEY = ValueKeyFactory.createNew("authenticationVaultUrl", String.class);
   public static final ValueKey<String> AUTHORITY_HOST_KEY = ValueKeyFactory.createNew("authorityHostKey", String.class);
   public static final String CLIENT_ID = "51403290-193e-4cd8-afd7-714429b9007f";
   public static final String CLIENT_KEY = "clientKey";
   public static final String TENANT_ID = "193ae71e-af07-44d6-b7f4-85fcf14ba82b";

}
