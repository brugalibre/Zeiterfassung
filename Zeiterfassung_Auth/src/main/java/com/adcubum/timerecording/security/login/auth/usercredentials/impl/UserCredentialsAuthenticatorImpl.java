package com.adcubum.timerecording.security.login.auth.usercredentials.impl;

import static java.util.Objects.nonNull;

import com.adcubum.timerecording.security.login.auth.constant.AuthenticationConst;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;
import com.adcubum.timerecording.settings.Settings;
import com.azure.core.credential.TokenCredential;
import com.azure.identity.InteractiveBrowserCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

public class UserCredentialsAuthenticatorImpl implements UserCredentialsAuthenticator {

   @Override
   public boolean doUserAuthentication(String username, char[] userPwd) {
      String vaultUrl = Settings.INSTANCE.getSettingsValue(AuthenticationConst.AUTHENTICATION_VAULT_URL_KEY);
      String authorityHost = Settings.INSTANCE.getSettingsValue(AuthenticationConst.AUTHORITY_HOST_KEY);
      KeyVaultSecret keyVaultSecret = null;
      try {
         TokenCredential credential = buildTokenCredential(authorityHost);
         SecretClient client = buildSecretClient(vaultUrl, credential);
         keyVaultSecret = client.getSecret("ZeiterfassungSecret");
      } catch (Exception e) {
         e.printStackTrace();
      }
      return nonNull(keyVaultSecret);
   }

   private static SecretClient buildSecretClient(String vaultUrl, TokenCredential credential) {
      return new SecretClientBuilder()
            .vaultUrl(vaultUrl)
            .credential(credential)
            .buildClient();
   }

   private static TokenCredential buildTokenCredential(String authorityHost) {
      return new InteractiveBrowserCredentialBuilder()
            .authorityHost(authorityHost)
            .tenantId(AuthenticationConst.TENANT_ID)
            .clientId(AuthenticationConst.CLIENT_ID)
            .redirectUrl("http://localhost")
            .build();
   }
}
