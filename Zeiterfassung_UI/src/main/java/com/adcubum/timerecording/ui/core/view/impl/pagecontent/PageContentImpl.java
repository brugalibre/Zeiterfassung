/**
 * 
 */
package com.adcubum.timerecording.ui.core.view.impl.pagecontent;

import com.adcubum.timerecording.ui.core.view.pagecontent.PageContent;
import com.adcubum.timerecording.ui.core.view.region.Region;

/**
 * The FXPageContent implements the PageContent for the Java-FX environment.
 * Therefore it contains the (main)-Stage object of the application
 * 
 * @author Dominic Stalder
 */
public class PageContentImpl implements PageContent {
   private Region rootRegion; // the main node of this content

   public PageContentImpl(Region rootRegion) {
      this.rootRegion = rootRegion;
   }

   @Override
   public Region getRegion() {
      return rootRegion;
   }
}
