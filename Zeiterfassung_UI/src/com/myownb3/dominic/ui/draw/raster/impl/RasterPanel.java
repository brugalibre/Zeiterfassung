/**
 * 
 */
package com.myownb3.dominic.ui.draw.raster.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.TimeSnippet4Export;
import com.myownb3.dominic.ui.draw.impl.Drawable;
import com.myownb3.dominic.ui.draw.impl.ParentComponent;
import com.myownb3.dominic.ui.draw.raster.Raster;
import com.myownb3.dominic.ui.draw.raster.cell.Cell;
import com.myownb3.dominic.ui.draw.raster.cell.impl.CellImpl;
import com.myownb3.dominic.ui.styles.font.Fonts;
import com.myownb3.dominic.ui.util.CellUtil;
import com.myownb3.dominic.ui.util.list.BusinessDayIncrementalCells;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * @author Dominic
 * 
 */
public class RasterPanel extends ParentComponent {
    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private static final String separatorWithDoublePoint = ": ";
    private BusinessDay4Export bussinessDay;
    private BusinessDayIncrementalCells collectedData;

    private Raster raster;

    public RasterPanel() {
	super();
	setLayout(new BorderLayout());
	setDoubleBuffered(true);
	setFont(Fonts.DIALOG_LABEL_FONT);
    }

    public void initialize() {
	collectedData = collectBusinessData();

	if (raster == null) {
	    raster = new RasterImpl(collectedData, bussinessDay, this);
	} else {
	    raster.initialize(collectedData, bussinessDay);
	}

	int height = collectedData.size() * CellUtil.getHeightForCells(collectedData.get(0));
	int width = getMaxLengthForBusinessDay(collectedData);
	setSize(new Dimension(width, height));
	setPreferredSize(getSize());
    }

    private BusinessDayIncrementalCells collectBusinessData() {
	BusinessDayIncrementalCells businessDayCells = new BusinessDayIncrementalCells();
	int counter = 1;
	List<Cell> titleHeaders = evaluateTitleHeaderCells();
	businessDayCells.add(titleHeaders);
	for (BusinessDayInc4Export bussinessDayIncremental : bussinessDay.getBusinessDayIncrements()) {
	    businessDayCells.add(getBusinessDayIncrementalCells(bussinessDayIncremental, counter));
	    counter++;
	}
	return businessDayCells;
    }

    private List<Cell> getBeginEndHeader() {

	int counter = bussinessDay.getAmountOfVonBisElements();
	List<Cell> vonBisHeader = new ArrayList<>();
	for (int i = 0; i < counter; i++) {
	    vonBisHeader.add(new CellImpl(TextLabel.VON_LABEL, this));
	    vonBisHeader.add(new CellImpl(TextLabel.BIS_LABEL, this));
	}
	return vonBisHeader;
    }

    private List<Cell> evaluateTitleHeaderCells() {

	List<Cell> titleHeaders = new ArrayList<>();
	titleHeaders.add(new CellImpl(TextLabel.NUMMER_LABEL, this));
	titleHeaders.add(new CellImpl(TextLabel.AMOUNT_OF_HOURS_LABEL, this));
	titleHeaders.add(new CellImpl(TextLabel.TICKET, this));

	boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
	if (isDescriptionTitleNecessary) {
	    titleHeaders.add(new CellImpl(TextLabel.DESCRIPTION_LABEL, this));
	}

	List<Cell> beginEndHeaders = getBeginEndHeader();
	titleHeaders.addAll(beginEndHeaders);

	titleHeaders.add(new CellImpl(TextLabel.CHARGE_TYPE_LABEL, this));
	return titleHeaders;
    }

    /*
     * Creates a list which contains all Cells that are required to paint a
     * BusinessDayIncremental
     */
    private List<Cell> getBusinessDayIncrementalCells(BusinessDayInc4Export bussinessDayIncremental, int no) {
	// create Cells for the introduction of a BD-inc.
	List<Cell> list = new ArrayList<>();
	list.add(new CellImpl(String.valueOf(no), this));
	list.add(new CellImpl(String.valueOf(bussinessDayIncremental.getTotalDuration()), this));
	list.add(new CellImpl(bussinessDayIncremental.getTicketNumber(), this));

	boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
	if (isDescriptionTitleNecessary) {
	    String cellValue = StringUtil.isNotEmptyOrNull(bussinessDayIncremental.getDescription())
		    ? bussinessDayIncremental.getDescription()
		    : "";
	    list.add(new CellImpl(cellValue, this));
	}

	// create Cells for all TimeSnippet's
	list.addAll(collectTimeSnippetData(bussinessDayIncremental));
	list.add(new CellImpl(ChargeType.getRepresentation(bussinessDayIncremental.getChargeType()), this));
	return list;
    }

    /*
     * Creates a list which contains all Cells with the content about each
     * TimeSnippet
     */
    private List<Cell> collectTimeSnippetData(BusinessDayInc4Export bussinessDayIncremental) {
	// = for all time snippet
	List<Cell> snippetCells = new ArrayList<>();
	for (TimeSnippet4Export snippet : bussinessDayIncremental.getTimeSnippets()) {
	    // start point
	    String value = String.valueOf(snippet.getBeginTimeStamp());
	    snippetCells.add(new CellImpl(value, this));
	    // end point
	    value = String.valueOf(snippet.getEndTimeStamp());
	    snippetCells.add(new CellImpl(value, this));
	}
	for (int i = 0; i < bussinessDayIncremental.getTimeSnippetPlaceHolders().size(); i++) {
	    snippetCells.add(new CellImpl("", this));
	}
	return snippetCells;
    }

    private int getMaxLengthForBusinessDay(BusinessDayIncrementalCells collectedData) {
	int length = 0;
	for (List<Cell> cells : collectedData) {
	    length = Math.max(CellUtil.getLengthForCells(cells), length);
	}
	return length;
    }

    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	drawBusinessDay((Graphics2D) g, raster);
    }

    private void drawBusinessDay(Graphics2D g, Raster raster) {
	// draw the raster and it's cells
	raster.draw(g);
	// draw another bunch of cells, which don't belong to this raster
	drawFinalInformation(g, bussinessDay, raster);
    }

    private void drawFinalInformation(Graphics2D g, BusinessDay4Export bussinessDay, Raster raster) {
	String value = TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL + separatorWithDoublePoint
		+ bussinessDay.getTotalDuration();

	int height = raster.getHeight();

	Cell space = new CellImpl("", this);
	space.setBounds(new Rectangle(0, height, space.getWidth(), space.getHeight()));
	space.setDrawShape(false);
	space.draw(g);

	Cell finalInformation = new CellImpl(value, this);
	finalInformation.setBounds(new Rectangle(0, height + space.getHeight(), finalInformation.getWidth(),
		finalInformation.getHeight()));
	finalInformation.setDrawShape(false);
	finalInformation.draw(g);
    }

    /**
     * Sets the {@link #bussinessDay} value
     * 
     * @param bussinessDay2
     */
    public void setBussinessDay(BusinessDay bussinessDay) {
	this.bussinessDay = new BusinessDay4Export(bussinessDay);
    }

    @Override
    protected void afterAction() {
    }

    @Override
    protected void checkCondition(Drawable<?> drawable) {
    }

    @Override
    protected void recalculate(Drawable<?> drawable) {
    }
}
