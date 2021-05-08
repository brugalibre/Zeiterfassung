/**
 * 
 */
package com.adcubum.timerecording.ui.core.control;

import java.util.function.Consumer;

import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.view.Page;
import com.adcubum.timerecording.ui.core.view.region.Dimension;

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
    * This leads this {@link Controller} to show it's content, given the initial datamodel
    * 
    * @param dataModelIn
    *        the incoming {@link PageModel}
    */
   public void show(I dataModelIn);

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
    * @return the dimension of the content this {@link Controller} shows
    */
   public Dimension getDimension();

   /**
    * Adds the given {@link Consumer} as a handler whenever the {@link Dimension} of this Controller changes
    * 
    * @param onResizeHandler
    *        the handler
    */
   void addOnResizeHandler(Consumer<Dimension> onResizeHandler);
}
