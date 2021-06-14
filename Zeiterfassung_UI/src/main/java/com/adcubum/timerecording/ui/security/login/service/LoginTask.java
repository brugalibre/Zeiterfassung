package com.adcubum.timerecording.ui.security.login.service;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import com.adcubum.timerecording.security.login.auth.AuthenticationResult;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;

import javafx.concurrent.Task;

public class LoginTask extends Task<AuthenticationResult> {

   private Supplier<String> usernameSupplier;
   private Supplier<char[]> userPwdSupplier;

   /**
    * Creates a new {@link LoginTask}
    * 
    * @param usernameSupplier
    *        the {@link Supplier} for the user name
    * @param userPwdSupplier
    *        the {@link Supplier} for the user password
    */
   public LoginTask(Supplier<String> usernameSupplier, Supplier<char[]> userPwdSupplier) {
      this.usernameSupplier = requireNonNull(usernameSupplier);
      this.userPwdSupplier = requireNonNull(userPwdSupplier);
   }

   @Override
   protected AuthenticationResult call() throws Exception {
      return AuthenticationService.INSTANCE.doUserAuthentication(usernameSupplier.get(), userPwdSupplier.get());
   }
}
