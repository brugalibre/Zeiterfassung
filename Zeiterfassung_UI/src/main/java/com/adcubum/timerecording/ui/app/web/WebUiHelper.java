package com.adcubum.timerecording.ui.app.web;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.message.MessageFactory;
import com.adcubum.timerecording.message.MessageType;

public class WebUiHelper {

   private UiCallbackHandler uiCallbackHandler;

   public WebUiHelper(UiCallbackHandler uiCallbackHandler) {
      this.uiCallbackHandler = uiCallbackHandler;
   }

   /**
    * Opens the Browser at the given adress
    */
   public void openUrlInBrowser(String url) {
      if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
         try {
            Desktop.getDesktop().browse(new URI(url));
         } catch (IOException | URISyntaxException e) {
            displayErrorMsg(TextLabel.SHOW_WEB_UI_FAILED);
         }
      } else {
         displayErrorMsg(TextLabel.SHOW_WEB_UI_NOT_SUPPORTED);
      }
   }

   private void displayErrorMsg(String errorMsg) {
      Message message = MessageFactory.createNew(MessageType.ERROR, TextLabel.EXCEPTION_DIALOG_TITLE, errorMsg);
      uiCallbackHandler.displayMessage(message);
   }
}
