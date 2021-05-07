package com.adcubum.timerecording.ui.core.view.region;

import com.adcubum.timerecording.ui.core.view.Page;

/**
 * The {@link Region} defines a visible region on a {@link Page}
 * The size of {@link Region} is defined by a {@link Dimension}
 * 
 * @author dominic
 *
 */
public interface Region {

   /**
    * @return the {@link Dimension} of this {@link Region}
    */
   Dimension getDimension();
}
