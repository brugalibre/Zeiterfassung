/**
 * 
 */
package com.adcubum.timerecording.ui.core.view.pagecontent;

import com.adcubum.timerecording.ui.core.view.region.Region;

/**
 * The PageContent contains the actually content of a Page which all it's
 * UI-components
 * 
 * @author Dominic Stalder
 * @since rel2.00.00
 */
public interface PageContent {

   /**
    * @return the {@link Region} this {@link PageContent} requires to be displayed
    */
   Region getRegion();
}
