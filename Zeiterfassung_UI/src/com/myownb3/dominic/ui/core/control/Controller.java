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
 * @param <IN_VO>
 * @param <OUT_VO>
 */
public interface Controller<IN_VO extends PageModel, OUT_VO extends PageModel> {
   /**
    * Initializes this Controller with it's main page to show
    * 
    * @param page
    *        - the main page
    */
   public void initialize(Page<IN_VO, OUT_VO> page);

   /**
    * Forces this Controller to update and finally show it's content
    */
   public void show();

   /**
    * Forces this Controller to refresh it's content and, if desired, the content
    * of it's subpages
    */
   public void refresh(boolean withSubPages);

   /**
    * Returns the current set data model
    * 
    * @return the current set data model
    */
   public OUT_VO getDataModel();
}
