package com.adcubum.timerecording.security.login.auth.usercredentials.impl;

import static java.util.Objects.nonNull;

import com.adcubum.timerecording.security.login.auth.configuration.Configuration;
import com.adcubum.timerecording.security.login.auth.configuration.constant.AuthenticationConst;
import com.adcubum.timerecording.security.login.auth.configuration.impl.ConfigurationImpl;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;
import com.azure.core.credential.TokenCredential;
import com.azure.identity.InteractiveBrowserCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

public class UserCredentialsAuthenticatorImpl implements UserCredentialsAuthenticator {

   private Configuration configuration;

   private UserCredentialsAuthenticatorImpl() {
      this.configuration = new ConfigurationImpl();
   }

   @Override
   public boolean doUserAuthentication(String username, char[] userPwd) {
      String vaultUrl = configuration.getValue(AuthenticationConst.AUTHENTICATION_VAULT_URL_KEY);
      String authorityHost = configuration.getValue(AuthenticationConst.AUTHORITY_HOST_KEY);
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
