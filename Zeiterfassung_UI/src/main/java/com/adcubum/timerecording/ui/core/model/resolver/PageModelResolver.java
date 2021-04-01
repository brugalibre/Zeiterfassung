/**
 * 
 */
package com.adcubum.timerecording.ui.core.model.resolver;

import com.adcubum.timerecording.ui.core.control.Controller;
import com.adcubum.timerecording.ui.core.model.PageModel;

/**
 * The {@link PageModelResolver} is used by the {@link Controller} in order to
 * resolve it's appropriate {@link PageModel} with updated values
 * 
 * @author Dominic Stalder
 */
@FunctionalInterface
public interface PageModelResolver<I extends PageModel, O extends PageModel> {

   /**
    * Resolves the {@link PageModel}
    * 
    * @param dataModelIn
    * @return the new resolved {@link PageModel}
    */
   public O resolvePageModel(I dataModelIn);
}
