package com.adcubum.timerecording.ui.app.pages.mainpage.control.callback;

/**
 * The {@link MainWindowCallbackHandler} is used by any Subpage to callback it's parent page
 * 
 * @author dstalder
 *
 */
public interface MainWindowCallbackHandler {

   /**
    * Updates the view states
    */
   void updateUIStates();

   void export();

   void clearBusinessDayContentsAndDispose();
}
