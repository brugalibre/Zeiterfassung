package com.myownb3.dominic.ui.app.settings.hotkey;

import static com.myownb3.dominic.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;
import static java.util.Objects.nonNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.KeyStroke;

import com.myownb3.dominic.ui.app.settings.hotkey.callback.UiCallbackHandler;
import com.myownb3.dominic.ui.app.settings.hotkey.exception.HotKeyRegisterException;
import com.tulskiy.keymaster.common.Provider;

public class HotKeyManager {

   public static final HotKeyManager INSTANCE = new HotKeyManager();
   private static final String START_STOP_HOT_KEY = "StartStopHotKey";

   private HotKeyManager() {
      // Private constructor
   }

   /**
    * Registers the hot key to start or stop a recording with a combination of key
    * pressing
    */
   public void registerHotKey(UiCallbackHandler callbackHandler) {
      KeyStroke keyStroke = evalKeyStroke();
      if (nonNull(keyStroke)) {
         Provider provider = Provider.getCurrentProvider(false);
         provider.register(keyStroke, hotKey -> callbackHandler.onHotKeyPressed());
      }
   }

   private KeyStroke evalKeyStroke() {
      String hotKeyAsString = evalHotKey();
      return nonNull(hotKeyAsString) ? KeyStroke.getKeyStroke(hotKeyAsString) : null;
   }

   private String evalHotKey() {
      String hotKey = null;
      try (InputStream resourceStream = new FileInputStream(ZEITERFASSUNG_PROPERTIES)) {
         hotKey = evalHotKeyFromProperties(resourceStream);
      } catch (IOException e) {
         throw new HotKeyRegisterException(e);
      }
      return hotKey;
   }

   private String evalHotKeyFromProperties(InputStream resourceStream) throws IOException {
      Properties prop = new Properties();
      prop.load(resourceStream);
      return (String) prop.get(START_STOP_HOT_KEY);
   }
}
