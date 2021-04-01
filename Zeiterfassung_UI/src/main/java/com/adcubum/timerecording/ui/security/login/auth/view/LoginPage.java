/**
 * 
 */
package com.adcubum.timerecording.ui.security.login.auth.view;

import com.adcubum.timerecording.security.login.callback.LoginCallbackHandler;
import com.adcubum.timerecording.ui.core.view.impl.AbstractFXPage;
import com.adcubum.timerecording.ui.security.login.auth.control.LoginController;
import com.adcubum.timerecording.ui.security.login.auth.model.LoginPageModel;

import javafx.stage.Stage;

/**
 * @author Dominic
 *
 */
public class LoginPage extends AbstractFXPage<LoginPageModel, LoginPageModel> {

   public LoginPage(LoginCallbackHandler userLogedInCallbackHandler, Stage primaryStage, boolean isBlocking) {
      super(primaryStage, isBlocking);
      ((LoginController) getController()).setCallbackHandler(userLogedInCallbackHandler);
   }
}
