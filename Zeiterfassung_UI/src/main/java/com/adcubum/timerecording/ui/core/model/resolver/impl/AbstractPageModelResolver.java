package com.adcubum.timerecording.ui.core.model.resolver.impl;

import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;

public abstract class AbstractPageModelResolver<I extends PageModel, O extends PageModel> implements PageModelResolver<I, O> {

   // The previous/current page model is used to keep the bindings even if we call the page with a 'foreign' page model
   protected O currentPageModel;

   @Override
   public O resolvePageModel(I dataModelIn) {
      O newPageModel = resolveNewPageModel(dataModelIn);
      currentPageModel = newPageModel;
      return newPageModel;
   }

   /**
    * Does the actual resolving of the new {@link PageModel}
    * 
    * @param dataModelIn
    *        the current {@link PageModel}
    * @return the new resolved/builded {@link PageModel}
    */
   protected abstract O resolveNewPageModel(I dataModelIn);

}
