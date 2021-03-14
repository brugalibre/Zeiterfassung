package com.adcubum.timerecording.security.login.auth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;

class AuthenticationServiceTest {

   @Test
   void testDoUserAuthenticationSuccessfull() {
      // Given
      char[] pw = "".toCharArray();
      String username = "1234";
      UserCredentialsAuthenticator userCredentialsAuthenticator = mock(UserCredentialsAuthenticator.class);
      when(userCredentialsAuthenticator.doUserAuthentication(eq(username), eq(pw))).thenReturn(true);
      AuthenticationService authenticationService = new AuthenticationService(userCredentialsAuthenticator);

      // When
      authenticationService.doUserAuthentication(username, pw);

      // Then
      verify(userCredentialsAuthenticator).doUserAuthentication(eq(username), eq(pw));
      assertThat(authenticationService.isUserAuthenticated(), is(true));
   }

   @Test
   void testDoUserAuthenticationNotSuccessfull() {
      // Given
      char[] pw = "".toCharArray();
      String username = "1234";
      UserCredentialsAuthenticator userCredentialsAuthenticator = mock(UserCredentialsAuthenticator.class);
      when(userCredentialsAuthenticator.doUserAuthentication(eq(username), eq(pw))).thenReturn(false);
      AuthenticationService authenticationService = new AuthenticationService(userCredentialsAuthenticator);

      // When
      AuthenticationResult authenticationResult = authenticationService.doUserAuthentication(username, pw);

      // Then
      assertThat(authenticationResult.isSuccessfull(), is(false));
   }

   @Test
   void testGetUsername() {
      // Given
      String username = "hampi";
      char[] userPwd = "13242".toCharArray();
      boolean value = true;
      UserCredentialsAuthenticator userCredentialsAuthenticator = mockUserCredentialsAuthenticator(username, userPwd, value);
      AuthenticationService authenticationService = new AuthenticationService(userCredentialsAuthenticator);

      // When
      authenticationService.doUserAuthentication(username, userPwd);
      String actualUsername = authenticationService.getUsername();

      // Then
      assertThat(actualUsername, is(username));
   }

   private UserCredentialsAuthenticator mockUserCredentialsAuthenticator(String username, char[] userPwd, boolean isAuthSuccessfull) {
      UserCredentialsAuthenticator userCredentialsAuthenticator = mock(UserCredentialsAuthenticator.class);
      when(userCredentialsAuthenticator.doUserAuthentication(eq(username), eq(userPwd))).thenReturn(isAuthSuccessfull);
      return userCredentialsAuthenticator;
   }
}
