package com.myownb3.dominic.ui.draw.raster.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.TimeSnippet4Export;
import com.myownb3.dominic.util.utils.StringUtil;

public class BusinessDayTableModel extends AbstractTableModel implements TableModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BusinessDay4Export bussinessDay;
    private List<List<String>> colmnValues;
    private List<String> columnNames;

    public BusinessDayTableModel() {
	columnNames = new ArrayList<>();
	colmnValues = new ArrayList<>();
    }

    public void init(BusinessDay4Export bussinessDay) {
	Objects.requireNonNull(bussinessDay);
	this.bussinessDay = bussinessDay;
	this.colmnValues = getBusinessDayCells();
	this.columnNames = getTableHeaders();
    }

    private List<String> getTableHeaders() {
	List<String> titleHeaders = new ArrayList<>();
	titleHeaders.add(TextLabel.NUMMER_LABEL);
	titleHeaders.add(TextLabel.AMOUNT_OF_HOURS_LABEL);
	titleHeaders.add(TextLabel.TICKET);

	boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
	if (isDescriptionTitleNecessary) {
	    titleHeaders.add(TextLabel.DESCRIPTION_LABEL);
	}

	int counter = bussinessDay.getAmountOfVonBisElements();
	List<String> beginEndHeaders = new ArrayList<>();
	for (int i = 0; i < counter; i++) {
	    beginEndHeaders.add(TextLabel.VON_LABEL);
	    beginEndHeaders.add(TextLabel.BIS_LABEL);
	}

	titleHeaders.addAll(beginEndHeaders);

	titleHeaders.add(TextLabel.CHARGE_TYPE_LABEL);
	titleHeaders.add(TextLabel.CHARGED);

	return titleHeaders;
    }

    private List<List<String>> getBusinessDayCells() {
	List<List<String>> businessDayCells = new ArrayList<>();
	int counter = 1;
	for (BusinessDayInc4Export bussinessDayIncremental : bussinessDay.getBusinessDayIncrements()) {
	    List<String> businessDayIncrementalCells = getBusinessDayIncrementalCells(bussinessDayIncremental, counter);
	    businessDayCells.add(businessDayIncrementalCells);
	    counter++;
	}
	return businessDayCells;
    }

    /*
     * Creates a list which contains all Cells that are required to paint a
     * BusinessDayIncremental
     */
    private List<String> getBusinessDayIncrementalCells(BusinessDayInc4Export bussinessDayIncremental, int no) {
	// create Cells for the introduction of a BD-inc.
	List<String> list = new ArrayList<>();
	list.add(String.valueOf(no));
	list.add(String.valueOf(bussinessDayIncremental.getTotalDuration()));
	list.add(bussinessDayIncremental.getTicketNumber());

	boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
	if (isDescriptionTitleNecessary) {
	    String cellValue = StringUtil.isNotEmptyOrNull(bussinessDayIncremental.getDescription())
		    ? bussinessDayIncremental.getDescription() : "";
	    list.add(cellValue);
	}

	// create Cells for all TimeSnippet's
	list.addAll(collectTimeSnippetData(bussinessDayIncremental));
	list.add(ChargeType.getRepresentation(bussinessDayIncremental.getChargeType()));
	list.add(bussinessDayIncremental.isCharged() ? TextLabel.YES : TextLabel.NO);
	return list;
    }

    /*
     * Creates a list which contains all Cells with the content about each
     * TimeSnippet
     */
    private List<String> collectTimeSnippetData(BusinessDayInc4Export bussinessDayIncremental) {
	// = for all time snippet
	List<String> snippetCells = new ArrayList<>();
	for (TimeSnippet4Export snippet : bussinessDayIncremental.getTimeSnippets()) {
	    // start point
	    String value = String.valueOf(snippet.getBeginTimeStamp());
	    snippetCells.add(value);
	    // end point
	    value = String.valueOf(snippet.getEndTimeStamp());
	    snippetCells.add(value);
	}
	for (int i = 0; i < bussinessDayIncremental.getTimeSnippetPlaceHolders().size(); i++) {
	    snippetCells.add("");
	}
	return snippetCells;
    }

    @Override
    public int getColumnCount() {
	return columnNames.size();
    }

    @Override
    public int getRowCount() {

	return colmnValues.size();
    }

    @Override
    public String getColumnName(int col) {
	return columnNames.get(col);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class getColumnClass(int c) {
	return getValueAt(0, c).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

	List<String> rowValues = colmnValues.get(rowIndex);
	return rowValues.get(columnIndex);
    }
}
