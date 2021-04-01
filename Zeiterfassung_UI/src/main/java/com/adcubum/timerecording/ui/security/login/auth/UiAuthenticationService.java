package com.adcubum.timerecording.ui.security.login.auth;

import com.adcubum.timerecording.security.login.callback.LoginCallbackHandler;
import com.adcubum.timerecording.ui.security.login.auth.view.LoginPage;

import javafx.stage.Stage;

/**
 * The {@link UiAuthenticationService} is responsible for initializing the authentication on a ui level
 * 
 * @author Dominic
 *
 */
public class UiAuthenticationService {

   private UiAuthenticationService() {
      // private 
   }

   /**
    * Shows the {@link LoginPage} in order to do the actual login
    * <b>attention</b> This method blocks the current Thread until the user has either entered username & password or closed the login
    * window
    * 
    * @param loginCallbackHandler
    *        the {@link LoginCallbackHandler}
    */
   public static void doUserAuthentication(LoginCallbackHandler loginCallbackHandler) {
      LoginPage loginPage = new LoginPage(loginCallbackHandler, new Stage(), true);
      loginPage.show();
   }
}
