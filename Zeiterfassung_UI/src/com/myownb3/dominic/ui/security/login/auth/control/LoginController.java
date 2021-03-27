/**
 * 
 */
package com.myownb3.dominic.ui.security.login.auth.control;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.awt.Toolkit;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.security.login.auth.AuthenticationResult;
import com.adcubum.timerecording.security.login.callback.LoginCallbackHandler;
import com.adcubum.timerecording.security.login.callback.LoginEndState;
import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.ui.app.inputfield.InputFieldVerifier;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.view.Page;
import com.myownb3.dominic.ui.security.login.auth.model.LoginPageModel;
import com.myownb3.dominic.ui.security.login.service.LoginService;
import com.myownb3.dominic.ui.util.ExceptionUtil;

import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Dominic
 * 
 */
public class LoginController extends BaseFXController<LoginPageModel, LoginPageModel> {

   @FXML
   private VBox vBox;

   @FXML
   private ProgressIndicator progressIndicator;

   @FXML
   private TextField userNameField;

   @FXML
   private Label loginFailedLabel;

   @FXML
   private PasswordField userPwdField;

   @FXML
   private Button loginButton;

   private LoginService loginService;
   private LoginCallbackHandler loginCallbackHandler;

   @Override
   public void show() {
      // Since we set the username in the pagemodel already we travers the focus to the password field. Since show() is blocking, we have to do this first
      userPwdField.requestFocus();
      super.show();
   }

   @Override
   public void hide() {
      super.hide();
      userPwdField.clear();
   }

   @Override
   public void initialize(Page<LoginPageModel, LoginPageModel> page) {
      createLoginService();
      super.initialize(page);
      prepareLoginStage();
      vBox.getChildren().remove(loginFailedLabel);
   }

   private void createLoginService() {
      this.loginService = new LoginService(() -> getDataModel().getUsername(), this::getUserPwd);
      loginService.setOnSucceeded(onSucceededHandler());
      loginService.setOnFailed(onFailedHandler());
   }

   private EventHandler<WorkerStateEvent> onFailedHandler() {
      return workerStateEvent -> {
         Worker<?> worker = workerStateEvent.getSource();
         if (nonNull(worker.getException())) {
            ExceptionUtil.showException(Thread.currentThread(), worker.getException());
         } else {
            handleLoginNotSuccessfull();
         }
         loginCallbackHandler.onLoginEnd(LoginEndState.FAILED);
      };
   }

   private EventHandler<WorkerStateEvent> onSucceededHandler() {
      return workerStateEvent -> {
         AuthenticationResult res = loginService.getValue();
         if (res.isSuccessfull()) {
            hide();
            loginCallbackHandler.onLoginEnd(LoginEndState.SUCCESSFULLY);
         } else {
            handleLoginNotSuccessfull();
            loginCallbackHandler.onLoginEnd(LoginEndState.FAILED);
         }
      };
   }

   private void prepareLoginStage() {
      Stage stage = getStage();
      stage.setTitle(TextLabel.LOGIN_LABEL);
      stage.getIcons().add(PictureLibrary.getClockImageIcon());
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setResizable(false);
      stage.setAlwaysOnTop(true);
      stage.sizeToScene();
      stage.setOnCloseRequest(event -> {
         hide();
         loginCallbackHandler.onLoginEnd(LoginEndState.ABORTED);
      });
   }

   @FXML
   private void login(ActionEvent actionEvent) {
      if (isValid(new InputFieldVerifier())) {
         if (!loginService.isRunning()) {
            loginService.login();
         }
      } else {
         Toolkit.getDefaultToolkit().beep();
      }
   }

   private void handleLoginNotSuccessfull() {
      Toolkit.getDefaultToolkit().beep();
      addLoginFailedLabel();
      loginFailedLabel.setText(TextLabel.LOGIN_FAILED_LABEL);
      Stage stage = getStage();
      stage.sizeToScene();
   }

   private void addLoginFailedLabel() {
      if (!vBox.getChildren().contains(loginFailedLabel)) {
         vBox.getChildren().add(loginFailedLabel);
      }
   }

   private char[] getUserPwd() {
      return userPwdField.getText().toCharArray();
   }

   private boolean isValid(InputFieldVerifier inputFieldVerifier) {
      return inputFieldVerifier.verifyString(userNameField, getDataModel().getUsername())
            && inputFieldVerifier.verifyString(userPwdField, userPwdField.getText().toCharArray());
   }

   @Override
   protected PageModelResolver<LoginPageModel, LoginPageModel> createPageModelResolver() {
      return oldPageModel -> nonNull(oldPageModel) ? oldPageModel : new LoginPageModel();
   }

   @Override
   protected void setBinding(LoginPageModel pageModel) {
      userNameField.textProperty().bindBidirectional(pageModel.getUserNameFieldProperty());
      userNameField.promptTextProperty().bindBidirectional(pageModel.getUserNameFieldPrompProperty());
      userPwdField.promptTextProperty().bindBidirectional(pageModel.getUserPwdFieldPromptProperty());
      loginButton.textProperty().bindBidirectional(pageModel.getLoginButtonProperty());
      progressIndicator.visibleProperty().bind(loginService.runningProperty());
   }

   /**
    * Sets the {@link LoginCallbackHandler}
    * 
    * @param loginCallbackHandler
    *        the callback-Handler to set
    */
   public void setCallbackHandler(LoginCallbackHandler loginCallbackHandler) {
      this.loginCallbackHandler = requireNonNull(loginCallbackHandler);
   }
}
