package com.myownb3.dominic.ui.core.view;

import javax.swing.text.View;

import com.myownb3.dominic.ui.core.control.Controller;
import com.myownb3.dominic.ui.core.model.PageModel;

/**
 * A {@link Page} content one ore more {@link View} and displays a main content
 * of the application. Before a {@link Page} is shown, it is <br>
 * initialized and before a Page is leaved, it is checked if it is valid or not.
 * 
 * @author Dominic Stalder
 *
 */
public interface Page<IN_VO extends PageModel, OUT_VO extends PageModel> {

   /**
    * Returns the controller of this Page
    * 
    * @return the controller of this Page
    */
   public Controller<IN_VO, OUT_VO> getController();

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
    * Defines weather this Page is dirty or not
    * 
    * @param isDirty
    *        - this value is <code>true</code> if the page is set to dirty
    *        or <code>false</code> if not.
    */
   public void setDirty(boolean isDirty);

   /**
    * Returns <code>true</code> if this Page is currently visible. Otherwise
    * <code>false</code>
    * 
    * @return <code>true</code> if this Page is currently visible. Otherwise
    *         <code>false</code>
    */
   public boolean isVisible();

   /**
    * @return the {@link PageContent} of this Page
    */
   public PageContent getContent();

   /**
    * Refreshes this Page
    */
   public void refresh();

   /**
    * Shows this {@link Page}
    */
   public void show();

   /**
    * Hides this {@link Page}
    */
   public void hide();
}
