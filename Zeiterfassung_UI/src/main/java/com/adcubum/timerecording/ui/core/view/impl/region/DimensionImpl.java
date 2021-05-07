package com.adcubum.timerecording.ui.core.view.impl.region;

import com.adcubum.timerecording.ui.core.view.region.Dimension;

public class DimensionImpl implements Dimension {

   private double prefWidth;
   private double prefHeight;

   public DimensionImpl(double prefWidth, double prefHeight) {
      this.prefWidth = prefWidth;
      this.prefHeight = prefHeight;
   }

   @Override
   public double getPrefWidth() {
      return prefWidth;
   }

   @Override
   public double getPrefHeight() {
      return prefHeight;
   }
}
