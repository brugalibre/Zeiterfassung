package com.adcubum.timerecording.ui.core.view.impl.region;

import com.adcubum.timerecording.ui.core.view.region.Dimension;
import com.adcubum.timerecording.ui.core.view.region.Region;

/**
 * 
 * The {@link FxRegionImpl} is a wrapper for a javafx {@link javafx.scene.layout.Region}
 * 
 * @author dominic
 *
 */
public class FxRegionImpl implements Region {

   private javafx.scene.layout.Region region;

   public FxRegionImpl(javafx.scene.layout.Region region) {
      this.region = region;
   }

   @Override
   public Dimension getDimension() {
      return new DimensionImpl(region.getPrefWidth(), region.getPrefHeight());
   }
}
