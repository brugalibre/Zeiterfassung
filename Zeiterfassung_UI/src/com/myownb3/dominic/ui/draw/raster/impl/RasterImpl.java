/**
 * 
 */
package com.myownb3.dominic.ui.draw.raster.impl;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.ui.draw.impl.ParentComponent;
import com.myownb3.dominic.ui.draw.raster.cell.Cell;
import com.myownb3.dominic.ui.util.CellUtil;
import com.myownb3.dominic.ui.util.list.BusinessDayIncrementalCells;
import com.myownb3.dominic.ui.views.overview.OverviewView;

/**
 * An implementation of the {@link AbstractRaster} - is responsible for drawing
 * all it {@link Cell}
 * 
 * @author Dominic
 *
 */
public class RasterImpl extends AbstractRaster<Cell> {
    /**
     * @param value
     * @param bussinessDay
     */
    public RasterImpl(BusinessDayIncrementalCells value, BusinessDay bussinessDay, ParentComponent parent) {
	super(value, bussinessDay, parent);
	if (value.isEmpty())
	    throw new IllegalArgumentException("Given cells must not be empty!");
	initialize(value, bussinessDay);
    }

    @Override
    public void draw(Graphics2D g) {
	int counter = 1;
	int height = 0;
	int length = getMaxLengthForBusinessDay((BusinessDayIncrementalCells) value);

	for (List<Cell> incrementalCells : value) {
	    drawBusinessIncrement(g, incrementalCells, height, length);
	    height = counter * CellUtil.getHeightForCells(incrementalCells);
	    counter++;
	}
    }

    /**
     * @param collectedData
     * @param bussinessDay
     */
    public void initialize(BusinessDayIncrementalCells collectedData, BusinessDay bussinessDay) {
	this.value = collectedData;
	initCells(collectedData, bussinessDay);
	amountOfRows = collectedData.size();
	amountOfColumns = calculateMaxRows(collectedData, bussinessDay);

	int cellHeigth = 0;
	if (!collectedData.isEmpty() && !collectedData.get(0).isEmpty()) {

	    cellHeigth = collectedData.get(0).get(0).getHeight();
	}
	height = amountOfRows * cellHeigth;
    }

    private void initCells(BusinessDayIncrementalCells collectedData, BusinessDay bussinessDay) {
	int maxRow = calculateMaxRows(collectedData, bussinessDay);

	for (int i = 0; i < maxRow; i++) {
	    int currentCellWidth = 0;
	    // get max length
	    for (List<Cell> businessDayIncrementCells : collectedData) {
		Cell currentCell = businessDayIncrementCells.get(i);
		currentCellWidth = Math.max(currentCellWidth, currentCell.getWidth());
	    }
	    // set max length
	    for (List<Cell> businessDayIncrementCells : collectedData) {
		Cell currentCell = businessDayIncrementCells.get(i);
		currentCell.setWidth(currentCellWidth);
	    }
	}
    }

    private int calculateMaxRows(BusinessDayIncrementalCells collectedData, BusinessDay bussinessDay) {

	int calculatedRows = 0;
	for (BusinessDayIncremental inc : bussinessDay.getIncrements()) {
	    calculatedRows = Math.max(calculatedRows, (2 * inc.getTimeSnippets().size()));
	}
	int additionallyValueForDescHeader = bussinessDay.hasIncrementWithDescription() ? 1 : 0;
	return calculatedRows + OverviewView.AMOUNT_OF_FIX_HEADERS + additionallyValueForDescHeader;
    }

    private int getMaxLengthForBusinessDay(BusinessDayIncrementalCells collectedData) {
	int length = 0;
	for (List<Cell> cells : collectedData) {
	    length = Math.max(CellUtil.getLengthForCells(cells), length);
	}
	return length;
    }

    private void drawBusinessIncrement(Graphics2D g, List<Cell> incrementalCells, int height, int length) {
	int x = 0;
	for (Cell cell : incrementalCells) {
	    cell.setBounds(new Rectangle(x, height, cell.getWidth(), cell.getHeight()));
	    cell.draw(g);
	    x = x + cell.getWidth();
	}
    }
}
