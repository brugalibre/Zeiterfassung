/**
 * 
 */
package com.myownb3.dominic.ui.core.control.impl;

import com.myownb3.dominic.ui.core.control.Controller;
import com.myownb3.dominic.ui.core.model.PageModel;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.view.Page;

/**
 * The {@link BaseController} provides the most basic features any
 * {@link Controller} should provide such as refreshing the current visible page
 * 
 * @author Dominic Stalder
 */
public abstract class BaseController<IN_VO extends PageModel, OUT_VO extends PageModel>
      implements Controller<IN_VO, OUT_VO> {

   // Data-model related attributes
   protected OUT_VO dataModel;
   protected PageModelResolver<IN_VO, OUT_VO> pageModelResolver;

   // The (main) page this controller controls
   protected Page<?, ?> page;

   /**
    * Initializes parts of this controllers such as it's
    * {@link PageModelResolver}<br>
    */
   @Override
   public void initialize(Page<IN_VO, OUT_VO> page) {
      this.page = page;
      PageModelResolver<IN_VO, OUT_VO> newPageModelResolver = createPageModelResolver();
      setPageModelResolver(newPageModelResolver);
      dataModel = newPageModelResolver.resolvePageVO(null);
      setBinding(dataModel);
   }

   protected abstract PageModelResolver<IN_VO, OUT_VO> createPageModelResolver();

   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   // HANDLING PAGE-VO //
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   protected OUT_VO initDataModel(IN_VO inVO) {
      OUT_VO out_VO = updateIncomingVO(inVO);
      this.dataModel = out_VO;
      return out_VO;
   }

   /**
    * @param inVO
    * @return
    */
   protected OUT_VO updateIncomingVO(IN_VO inVO) {
      return pageModelResolver.resolvePageVO(inVO);
   }

   /**
    * Sets the binding of the data model to it's corresponding UI-elements
    * 
    * @param pageModel
    *        - the updated data model
    */
   protected abstract void setBinding(OUT_VO pageModel);

   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   // SHOW & REFRESH //
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   protected void refresh() {
      if (page.isDirty()) {
         show();
      }
   }

   @Override
   public void refresh(boolean withSubPages) {
      if (withSubPages) {
         refresh();
      }
      // Nothing to do in the else case. Since in this case, we only want to refresh
      // this controllers content, this should be done by the appropriate
      // BaseController.
   }

   /**
    * Invokes the {@link Controller#show()} for the Page, which was set recently by
    * invoking {@link #setCurrentPage(PageID)}
    */
   @Override
   public void show() {
      showCurrentPage();
   }

   /**
   * 
   */
   protected void showCurrentPage() {}

   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   // GETTER AND SETTER //
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public OUT_VO getDataModel() {
      return dataModel;
   }

   /**
    * @param PageModelResolver
    *        the PageModelResolver to set
    */
   protected void setPageModelResolver(PageModelResolver<IN_VO, OUT_VO> PageModelResolver) {
      this.pageModelResolver = PageModelResolver;
   }
}
