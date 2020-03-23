/**
 * 
 */
package com.myownb3.dominic.ui.app.callback;

import com.myownb3.dominic.ui.app.settings.hotkey.HotKeyManager;

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
