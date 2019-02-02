/**
 * 
 */
package com.myownb3.dominic.ui.draw.raster;

import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.draw.Renderer;
import com.myownb3.dominic.ui.draw.raster.cell.Cell;
import com.myownb3.dominic.ui.util.list.BusinessDayIncrementalCells;

/**
 *
 * Contains a bunch of {@link Cell} which belong all together. This bunch of
 * Cell's creates a {@link Raster}
 * 
 * @author Dominic
 * @param <T>
 *
 */
public interface Raster extends Renderer {
    /**
     * Returns the Width of this {@link Raster}
     * 
     * @return the Width of this {@link Raster}
     */
    int getWidth();

    /**
     * Returns the height of this {@link Raster}
     * 
     * @return the height of this {@link Raster}
     */
    int getHeight();

    /**
     * Returns the amount of rows of this {@link Raster}
     * 
     * @return the amount of rows of this {@link Raster}
     */
    int getAmountOfRows();

    /**
     * Returns the amount of columns of this {@link Raster}
     * 
     * @return the amount of columns of this {@link Raster}
     */
    int getAmountOfColumns();

    /**
     * @param collectedData
     * @param bussinessDay
     */
    void initialize(BusinessDayIncrementalCells collectedData, BusinessDay4Export bussinessDay);
}
