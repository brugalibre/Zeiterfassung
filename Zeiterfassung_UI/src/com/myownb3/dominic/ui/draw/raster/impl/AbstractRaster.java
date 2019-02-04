/**
 * 
 */
package com.myownb3.dominic.ui.draw.raster.impl;

import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.draw.impl.Drawable;
import com.myownb3.dominic.ui.draw.impl.ParentComponent;
import com.myownb3.dominic.ui.draw.raster.Raster;
import com.myownb3.dominic.ui.draw.raster.cell.Cell;
import com.myownb3.dominic.util.list.TwoDimensionalList;

/**
 * Contains a bunch of {@link Cell} which belong all together. This bunch of
 * Cell's creates a {@link Raster}
 * 
 * @author Dominic
 * @param <T>
 *
 */
public abstract class AbstractRaster<T> extends Drawable<TwoDimensionalList<T>> implements Raster {
    protected int amountOfRows;
    protected int amountOfColumns;

    public AbstractRaster(TwoDimensionalList<T> value, BusinessDay4Export bussinessDay, ParentComponent parent) {
	super(value, parent);
    }

    @Override
    public int getWidth() {
	return 0;
    }

    @Override
    public int getHeight() {
	return height;
    }
}
