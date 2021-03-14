/**
 * 
 */
package com.myownb3.dominic.ui.core.control;

import com.myownb3.dominic.ui.core.model.PageModel;
import com.myownb3.dominic.ui.core.view.Page;

/**
 * The {@link Controller} as part of the MVC keeps controll about the ui
 * 
 * @author Dominic
 *
 * @param <I>
 *        the incoming data model
 * @param <O>
 *        the outgoing data model
 */
public interface Controller<I extends PageModel, O extends PageModel> {
   /**
    * Initializes this Controller with it's main page to show
    * 
    * @param page
    *        - the main page
    */
   public void initialize(Page<I, O> page);

   /**
    * Forces this Controller to update and finally show it's content
    */
   public void show();

   /**
    * Leads this {@link Controller} to hides it's content
    */
   void hide();

   /**
    * Forces this Controller to refresh it's content and, if desired, the content
    * of it's subpages
    */
   public void refresh();

   /**
    * Returns the current set data model
    * 
    * @return the current set data model
    */
   public O getDataModel();
}
