package com.adcubum.timerecording.ui.core.view;

import javax.swing.text.View;

import com.adcubum.timerecording.ui.core.control.Controller;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.view.pagecontent.PageContent;
import com.adcubum.timerecording.ui.core.view.region.Dimension;

/**
 * A {@link Page} content one ore more {@link View} and displays a main content
 * of the application. Before a {@link Page} is shown, it is <br>
 * initialized and before a Page is leaved, it is checked if it is valid or not.
 * 
 * @author Dominic Stalder
 *
 */
public interface Page<I extends PageModel, O extends PageModel> {

   /**
    * Returns the controller of this Page
    * 
    * @return the controller of this Page
    */
   public Controller<I, O> getController();

   /**
    * Returns <code>true</code> if this Page is dirty and needs to be refreshed or
    * <code>false</code> if not
    * 
    * @return <code>true</code> if this Page is dirty and needs to be refreshed or
    *         <code>false</code> if not
    */
   public default boolean isDirty() {
      return true;
   }

   /**
    * @return the {@link PageContent} of this Page
    */
   public PageContent getContent();

   /**
    * Shows this {@link Page}
    */
   public void show();

   /**
    * Hides this {@link Page}
    */
   public void hide();

   /**
    * @return <code>true</code> if the caller is blocked until this page is hidden or <code>false</code> if not
    */
   public boolean isBlocking();

   /**
    * @return the {@link Dimension} this {@link Page} requires to be displayed
    */
   public Dimension getDimension();
}
