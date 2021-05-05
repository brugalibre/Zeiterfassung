package com.adcubum.timerecording.ui.app.settings.hotkey;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;
import static java.util.Objects.nonNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.KeyStroke;

import com.adcubum.timerecording.ui.app.settings.hotkey.callback.UiCallbackHandler;
import com.adcubum.timerecording.ui.app.settings.hotkey.exception.HotKeyRegisterException;
import com.tulskiy.keymaster.common.Provider;

/**
 * The {@link HotKeyManager} registers a key or a combination of keys and a {@link UiCallbackHandler}
 * which is called as soon as the keys are entered
 * 
 * @author dominic
 */
public class HotKeyManager {

   /** The singleton instance of the {@link HotKeyManager} */
   public static final HotKeyManager INSTANCE = new HotKeyManager();
   /** The key for which the actual keyboard-key to start or stop the time recording */
   public static final String START_STOP_HOT_KEY = "StartStopHotKey";
   /** The key for which the actual keyboard-key to come or go */
   public static final String COME_OR_GO_HOT_KEY = "ComeOrGoHotKey";

   private HotKeyManager() {
      // Private constructor
   }

   /**
    * Registers the hot key to start or stop a recording with a combination of key
    * pressing
    * 
    * @param callbackHandler
    *        the {@link UiCallbackHandler} which is called as soon as the key for the given key-Key is called
    * @param hotKeyKey
    *        the key for which the specific key or key-combination is stored
    */
   public void registerHotKey(UiCallbackHandler callbackHandler, String hotKeyKey) {
      KeyStroke keyStroke = evalKeyStroke(hotKeyKey);
      if (nonNull(keyStroke)) {
         Provider provider = Provider.getCurrentProvider(false);
         provider.register(keyStroke, hotKey -> callbackHandler.onHotKeyPressed());
      }
   }

   private KeyStroke evalKeyStroke(String hotKeyKey) {
      String hotKeyAsString = evalHotKey(hotKeyKey);
      return nonNull(hotKeyAsString) ? KeyStroke.getKeyStroke(hotKeyAsString) : null;
   }

   private String evalHotKey(String hotKeyKey) {
      String hotKey = null;
      try (InputStream resourceStream = new FileInputStream(ZEITERFASSUNG_PROPERTIES)) {
         hotKey = evalHotKeyFromProperties(resourceStream, hotKeyKey);
      } catch (IOException e) {
         throw new HotKeyRegisterException(e);
      }
      return hotKey;
   }

   private String evalHotKeyFromProperties(InputStream resourceStream, String hotKey) throws IOException {
      Properties prop = new Properties();
      prop.load(resourceStream);
      return (String) prop.get(hotKey);
   }
}
