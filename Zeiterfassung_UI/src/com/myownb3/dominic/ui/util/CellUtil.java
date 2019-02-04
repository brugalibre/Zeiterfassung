/**
 * 
 */
package com.myownb3.dominic.ui.util;

import java.util.List;

import com.myownb3.dominic.ui.draw.raster.cell.Cell;

/**
 * @author Dominic
 *
 */
public class CellUtil {

    /**
     * @param incrementalCells
     * @return
     */
    public static int getLengthForCells(List<Cell> incrementalCells) {
	int sum = 0;
	for (Cell cell : incrementalCells) {
	    sum = sum + cell.getWidth();
	}
	return sum;
    }

    /**
     * @param incrementalCells
     * @return
     */
    public static int getHeightForCells(List<Cell> incrementalCells) {
	if (incrementalCells.isEmpty()) {
	    return 0;
	}
	return incrementalCells.get(0).getHeight();
    }
}
