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
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

public class WebUiHelper {

   /** The key in order to store the port value in the {@link Settings} */
   public static final ValueKey<String> SERVER_PORT_KEY = ValueKeyFactory.createNew("port", String.class);
   private UiCallbackHandler uiCallbackHandler;

   public WebUiHelper(UiCallbackHandler uiCallbackHandler) {
      this.uiCallbackHandler = uiCallbackHandler;
   }

   /**
    * Opens the Browser at the given adress
    */
   public void openUrlInBrowser() {
      if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
         try {
            URI uri = getWebUri();
            Desktop.getDesktop().browse(uri);
         } catch (IOException | URISyntaxException e) {
            displayErrorMsg(TextLabel.SHOW_WEB_UI_FAILED);
         }
      } else {
         displayErrorMsg(TextLabel.SHOW_WEB_UI_NOT_SUPPORTED);
      }
   }

   private static URI getWebUri() throws URISyntaxException {
      String port = Settings.INSTANCE.getSettingsValue(SERVER_PORT_KEY);
      return new URI("http://localhost:" + port);
   }

   private void displayErrorMsg(String errorMsg) {
      Message message = MessageFactory.createNew(MessageType.ERROR, TextLabel.EXCEPTION_DIALOG_TITLE, errorMsg);
      uiCallbackHandler.displayMessage(message);
   }
}
