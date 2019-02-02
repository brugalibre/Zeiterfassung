/**
 * 
 */
package com.myownb3.dominic.ui.draw.raster.cell;

import java.awt.Font;

import com.myownb3.dominic.ui.draw.Renderer;

/**
 * @author Dominic
 *
 */
public interface Cell extends Renderer {

    /**
     * @return the font of this {@link Cell}
     */
    Font getFont();

    /**
     * @return the height of this {@link Cell}
     */
    int getHeight();

    /**
     * @return the width of this {@link Cell}
     */
    int getWidth();

    /**
     * @param currentCellWidth
     */
    void setWidth(int currentCellWidth);

    /**
     * Defines if this Cell is drawn or not
     * 
     * @param drawShape
     *            the new value
     */
    void setDrawShape(boolean drawShape);
}