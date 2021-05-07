/**
 * 
 */
package com.adcubum.timerecording.ui.core.control.impl;

import com.adcubum.timerecording.ui.core.control.Controller;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;
import com.adcubum.timerecording.ui.core.view.Page;
import com.adcubum.timerecording.ui.core.view.region.Dimension;

/**
 * The {@link BaseController} provides the most basic features any
 * {@link Controller} should provide such as refreshing the current visible page
 * 
 * @param <O>
 *        - the outgoing data-model
 * @param <I>
 *        - the incoming data-model
 * @author Dominic Stalder
 */
public abstract class BaseController<I extends PageModel, O extends PageModel>
      implements Controller<I, O> {

   // Data-model related attributes
   protected O dataModel;
   protected PageModelResolver<I, O> pageModelResolver;

   // The (main) page this controller controls
   protected Page<?, ?> page;

   /**
    * Initializes parts of this controllers such as it's
    * {@link PageModelResolver}<br>
    */
   @Override
   public void initialize(Page<I, O> page) {
      this.page = page;
      PageModelResolver<I, O> newPageModelResolver = createPageModelResolver();
      setPageModelResolver(newPageModelResolver);
      dataModel = newPageModelResolver.resolvePageModel(null);
      setBinding(dataModel);
   }

   protected abstract PageModelResolver<I, O> createPageModelResolver();

   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   // HANDLING PAGE-VO //
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   protected O initDataModel(I dataModelIn) {
      this.dataModel = updateIncomingVO(dataModelIn);
      return dataModel;
   }

   /**
    * @param dataModelIn
    * @return
    */
   protected O updateIncomingVO(I dataModelIn) {
      return pageModelResolver.resolvePageModel(dataModelIn);
   }

   /**
    * Sets the binding of the data model to it's corresponding UI-elements
    * 
    * @param pageModel
    *        - the updated data model
    */
   protected abstract void setBinding(O pageModel);

   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   // SHOW & REFRESH //
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   @SuppressWarnings("unchecked")
   @Override
   public void refresh() {
      if (page.isDirty()) {
         show((I) dataModel);
      }
   }

   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   // GETTER AND SETTER //
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   /**
    * @param pageModelResolver
    *        the PageModelResolver to set
    */
   protected void setPageModelResolver(PageModelResolver<I, O> pageModelResolver) {
      this.pageModelResolver = pageModelResolver;
   }

   @Override
   public Dimension getDimension() {
      return page.getDimension();
   }
}
