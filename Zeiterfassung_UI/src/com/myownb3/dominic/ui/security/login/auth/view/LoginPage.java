/**
 * 
 */
package com.myownb3.dominic.ui.security.login.auth.view;

import com.adcubum.timerecording.security.login.callback.LoginCallbackHandler;
import com.myownb3.dominic.ui.core.view.impl.AbstractFXPage;
import com.myownb3.dominic.ui.security.login.auth.control.LoginController;
import com.myownb3.dominic.ui.security.login.auth.model.LoginPageModel;

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
