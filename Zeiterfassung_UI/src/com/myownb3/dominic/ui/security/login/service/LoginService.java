package com.myownb3.dominic.ui.security.login.service;

import java.util.function.Supplier;

import com.adcubum.timerecording.security.login.auth.AuthenticationResult;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * The {@link LoginService} does the actually loging stuff like. It creates a {@link LoginTask}
 * which will be automatically called when calling {@link LoginService#login()}
 * 
 * @author dominic
 *
 */
public class LoginService extends Service<AuthenticationResult> {
   private Supplier<String> usernameSupplier;
   private Supplier<char[]> userPwdSupplier;

   public LoginService(Supplier<String> usernameSupplier, Supplier<char[]> userPwdSupplier) {
      this.usernameSupplier = usernameSupplier;
      this.userPwdSupplier = userPwdSupplier;
   }

   /**
    * Does the actually login process.
    * This calls {@link javafx.concurrent.Service#restart}
    */
   public void login() {
      this.restart();
   }

   @Override
   protected Task<AuthenticationResult> createTask() {
      return new LoginTask(usernameSupplier, userPwdSupplier);
   }
}
