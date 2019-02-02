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
import java.util.Collections;
import java.util.List;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;
import com.myownb3.dominic.ui.draw.impl.Drawable;
import com.myownb3.dominic.ui.draw.impl.ParentComponent;
import com.myownb3.dominic.ui.draw.raster.Raster;
import com.myownb3.dominic.ui.draw.raster.cell.Cell;
import com.myownb3.dominic.ui.draw.raster.cell.impl.CellImpl;
import com.myownb3.dominic.ui.styles.font.Fonts;
import com.myownb3.dominic.ui.util.CellUtil;
import com.myownb3.dominic.ui.util.list.BusinessDayIncrementalCells;
import com.myownb3.dominic.util.comparator.TimeStampComparator;
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
    private BusinessDay bussinessDay;
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

	int height = collectedData.size() * (CellUtil.getHeightForCells(collectedData.get(0)));
	int width = getMaxLengthForBusinessDay(collectedData);
	setSize(new Dimension(width, height));
	setPreferredSize(getSize());
    }

    private BusinessDayIncrementalCells collectBusinessData() {
	BusinessDayIncrementalCells businessDayCells = new BusinessDayIncrementalCells();
	int counter = 1;
	List<Cell> titleHeaders = evaluateTitleHeaderCells();
	businessDayCells.add(titleHeaders);
	for (BusinessDayIncremental bussinessDayIncremental : bussinessDay.getIncrements()) {
	    businessDayCells.add(getBusinessDayIncrementalCells(bussinessDayIncremental, counter));
	    counter++;
	}
	return businessDayCells;
    }

    private List<Cell> getBeginEndHeader() {

	int counter = getAmountOfVonBisElements();
	List<Cell> vonBisHeader = new ArrayList<>();
	for (int i = 0; i < counter; i++) {
	    vonBisHeader.add(new CellImpl(TextLabel.VON_LABEL, this));
	    vonBisHeader.add(new CellImpl(TextLabel.BIS_LABEL, this));
	}
	return vonBisHeader;
    }

    /*
     * Calculates the amount of 'begin' & 'end' cells
     * 
     * @return
     */
    private int getAmountOfVonBisElements() {
	int counter = 0;
	for (BusinessDayIncremental businessDayIncremental : bussinessDay.getIncrements()) {
	    counter = Math.max(counter, businessDayIncremental.getTimeSnippets().size());
	}
	return counter;
    }

    private boolean isDescriptionTitleNecessary() {
	return bussinessDay.getIncrements()//
		.stream()//
		.anyMatch(businessDay -> StringUtil.isNotEmptyOrNull(businessDay.getDescription()));
    }

    private List<Cell> evaluateTitleHeaderCells() {

	List<Cell> titleHeaders = new ArrayList<>();
	titleHeaders.add(new CellImpl(TextLabel.NUMMER_LABEL, this));
	titleHeaders.add(new CellImpl(TextLabel.AMOUNT_OF_HOURS_LABEL, this));
	titleHeaders.add(new CellImpl(TextLabel.TICKET, this));

	boolean isDescriptionTitleNecessary = isDescriptionTitleNecessary();
	if (isDescriptionTitleNecessary) {
	    titleHeaders.add(new CellImpl(TextLabel.DESCRIPTION_LABEL, this));
	}

	List<Cell> beginEndHeaders = getBeginEndHeader();
	titleHeaders.addAll(beginEndHeaders);

	titleHeaders.add(new CellImpl(TextLabel.CHARGE_TYPE_LABEL, this));
	return titleHeaders;
    }

    /**
     * @param bussinessDayIncremental
     */
    private List<Cell> getBusinessDayIncrementalCells(BusinessDayIncremental bussinessDayIncremental, int no) {
	return getBusinessDayIncrementalIntro(bussinessDayIncremental, no);
    }

    /*
     * Creates a list which contains all Cells that are required to paint a
     * BusinessDayIncremental
     */
    private List<Cell> getBusinessDayIncrementalIntro(BusinessDayIncremental bussinessDayIncremental, int no) {
	// create Cells for the introduction of a BD-inc.
	List<Cell> list = new ArrayList<>();
	list.add(new CellImpl(String.valueOf(no), this));
	list.add(new CellImpl(String.valueOf(bussinessDayIncremental.getTotalDuration()), this));
	list.add(new CellImpl(bussinessDayIncremental.getTicketNumber(), this));

	if (isDescriptionTitleNecessary()) {
	    String cellValue = StringUtil.isNotEmptyOrNull(bussinessDayIncremental.getDescription())
		    ? bussinessDayIncremental.getDescription()
		    : "";
	    list.add(new CellImpl(cellValue, this));
	}

	// create Cells for all TimeSnippet's
	list.addAll(collectTimeSnippetData(bussinessDayIncremental));
	list.add(new CellImpl(bussinessDayIncremental.getChargeType(), this));
	return list;
    }

    /*
     * Creates a list which contains all Cells with the content about each
     * TimeSnippet
     */
    private List<Cell> collectTimeSnippetData(BusinessDayIncremental bussinessDayIncremental) {
	// = for all time snippet
	List<Cell> snippetCells = new ArrayList<>();
	int snippetCellsCounter = 0;
	Collections.sort(bussinessDayIncremental.getTimeSnippets(), new TimeStampComparator());
	for (TimeSnippet snippet : bussinessDayIncremental.getTimeSnippets()) {
	    // start point
	    String value = String.valueOf(snippet.getBeginTimeStamp());
	    snippetCells.add(new CellImpl(value, this));
	    // end point
	    value = String.valueOf(snippet.getEndTimeStamp());
	    snippetCells.add(new CellImpl(value, this));
	    snippetCellsCounter++;
	}

	addPlaceHolderForMissingCell(snippetCells, snippetCellsCounter);
	return snippetCells;
    }

    /*
     * All rows must fit with it content to the title header. Thats why we have to
     * add some placeholders if this row has less TimeSnipets then the maximum
     * amount of TimeSnippet-Cells
     * 
     */
    private void addPlaceHolderForMissingCell(List<Cell> snippetCells, int snippetCellsCounter) {
	int amountOfEmptyTimeSnippets = getAmountOfVonBisElements() - snippetCellsCounter;
	for (int i = 0; i < amountOfEmptyTimeSnippets; i++) {
	    snippetCells.add(new CellImpl("", this));
	    snippetCells.add(new CellImpl("", this));
	}
    }

    /**
     * @param collectedData
     * @return
     */
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

    /**
     * @param g
     * @param blah
     */
    private void drawBusinessDay(Graphics2D g, Raster raster) {
	// draw the raster and it's cells
	raster.draw(g);
	// draw another bunch of cells, which don't belong to this raster
	drawFinalInformation(g, bussinessDay, raster);
    }

    /**
     * @param g
     * @param bussinessDay
     * @param raster2
     */
    private void drawFinalInformation(Graphics2D g, BusinessDay bussinessDay, Raster raster) {
	String value = TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL + separatorWithDoublePoint
		+ bussinessDay.getTotalDuration();

	int height = raster.getHeight();

	Cell space = new CellImpl("", this);
	space.setBounds(new Rectangle(0, height, space.getWidth(), space.getHeight()));
	((CellImpl) space).setDrawShape(false);
	space.draw(g);

	Cell finalInformation = new CellImpl(value, this);
	finalInformation.setBounds(new Rectangle(0, height + space.getHeight(), finalInformation.getWidth(),
		finalInformation.getHeight()));
	((CellImpl) finalInformation).setDrawShape(false);
	finalInformation.draw(g);
    }

    /**
     * Sets the {@link #bussinessDay} value
     * 
     * @param bussinessDay2
     */
    public void setBussinessDay(BusinessDay bussinessDay) {
	this.bussinessDay = bussinessDay;
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
