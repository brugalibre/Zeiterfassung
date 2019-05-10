/**
 * 
 */
package com.myownb3.dominic.ui.core.model.resolver;

import com.myownb3.dominic.ui.core.model.PageModel;

/**
 * The {@link PageModelResolver} is used by the {@link Controller} in order to
 * resolve it's appropriate {@link PageModel} with updated values
 * 
 * @author Dominic Stalder
 */
public interface PageModelResolver<IN_MODEL extends PageModel, OUT_MODEL extends PageModel> {

    /**
     * Resolved the {@link PageModel}
     * 
     * @param inPageModel
     * @return the new resolved {@link PageModel}
     */
    public OUT_MODEL resolvePageVO(IN_MODEL inPageModel);
}
