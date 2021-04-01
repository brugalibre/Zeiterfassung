/**
 * 
 */
package com.adcubum.timerecording.ui.app.settings.hotkey.callback;

import com.adcubum.timerecording.ui.app.settings.hotkey.HotKeyManager;

/**
 * @author Dominic
 *
 */
public interface UiCallbackHandler {

   /**
    * Is called whenever the {@link HotKeyManager} has notified the hot key pressed
    */
   public void onHotKeyPressed();
}
