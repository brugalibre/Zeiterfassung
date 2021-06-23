package com.adcubum.timerecording.ui.app.settings.hotkey;

import static java.util.Objects.nonNull;

import javax.swing.KeyStroke;

import com.adcubum.timerecording.ui.app.settings.hotkey.callback.UiCallbackHandler;
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
      KeyStroke keyStroke = evalKeyStroke(callbackHandler, hotKeyKey);
      if (nonNull(keyStroke)) {
         Provider provider = Provider.getCurrentProvider(false);
         provider.register(keyStroke, hotKey -> callbackHandler.onHotKeyPressed());
      }
   }

   private KeyStroke evalKeyStroke(UiCallbackHandler callbackHandler, String hotKeyKey) {
      String hotKeyAsString = evalHotKey(callbackHandler, hotKeyKey);
      return nonNull(hotKeyAsString) ? KeyStroke.getKeyStroke(hotKeyAsString) : null;
   }

   private String evalHotKey(UiCallbackHandler callbackHandler, String hotKeyKey) {
      return callbackHandler.getSettingsValue(hotKeyKey);
   }
}
