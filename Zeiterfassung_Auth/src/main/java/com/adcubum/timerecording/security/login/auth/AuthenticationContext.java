package com.adcubum.timerecording.security.login.auth;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

public class AuthenticationContext {

   private String username;
   private Supplier<char[]> userPwdSupplier;

   public AuthenticationContext(String username, Supplier<char[]> userPwdSupplier) {
      this.username = requireNonNull(username);
      this.userPwdSupplier = requireNonNull(userPwdSupplier);
   }

   /**
    * @return the username
    */
   public String getUsername() {
      return username;
   }

   /**
    * Returns the user password as a char-array. This method actually calls the underlying {@link Supplier} which then retrieves the
    * password
    * 
    * @return the user password as a char-array
    */
   public char[] getUserPw() {
      return userPwdSupplier.get();
   }
}
