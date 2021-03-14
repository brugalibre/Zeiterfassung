/**
 * 
 */
package com.myownb3.dominic.ui.core.model.resolver;

import com.myownb3.dominic.ui.core.control.Controller;
import com.myownb3.dominic.ui.core.model.PageModel;

/**
 * The {@link PageModelResolver} is used by the {@link Controller} in order to
 * resolve it's appropriate {@link PageModel} with updated values
 * 
 * @author Dominic Stalder
 */
public interface PageModelResolver<I extends PageModel, O extends PageModel> {

   /**
    * Resolves the {@link PageModel}
    * 
    * @param dataModelIn
    * @return the new resolved {@link PageModel}
    */
   public O resolvePageModel(I dataModelIn);
}
