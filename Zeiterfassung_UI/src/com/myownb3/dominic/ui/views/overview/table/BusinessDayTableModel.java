package com.myownb3.dominic.ui.views.overview.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.TimeSnippet4Export;
import com.myownb3.dominic.util.utils.StringUtil;

public class BusinessDayTableModel extends AbstractTableModel implements TableModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private BusinessDayChangedCallbackHandler handler;
    
    private BusinessDay4Export bussinessDay;
    private List<List<TableCellValue>> colmnValues;
    private List<String> columnNames;

    public BusinessDayTableModel(BusinessDayChangedCallbackHandler handler) {
	columnNames = new ArrayList<>();
	colmnValues = new ArrayList<>();
	this.handler = handler;
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

    private List<List<TableCellValue>> getBusinessDayCells() {
	List<List<TableCellValue>> businessDayCells = new ArrayList<>();
	int counter = 1;
	for (BusinessDayInc4Export bussinessDayIncremental : bussinessDay.getBusinessDayIncrements()) {
	    List<TableCellValue> businessDayIncrementalCells = getBusinessDayIncrementalCells(bussinessDayIncremental, counter);
	    businessDayCells.add(businessDayIncrementalCells);
	    counter++;
	}
	return businessDayCells;
    }

    /*
     * Creates a list which contains all Cells that are required to paint a
     * BusinessDayIncremental
     */
    private List<TableCellValue> getBusinessDayIncrementalCells(BusinessDayInc4Export bussinessDayIncremental, int no) {
	// create Cells for the introduction of a BD-inc.
	List<TableCellValue> list = new ArrayList<>();
	list.add(TableCellValue.of(no));
	list.add(TableCellValue.of(bussinessDayIncremental.getTotalDurationRep()));
	list.add(TableCellValue.of(bussinessDayIncremental.getTicketNumber(), true, ValueTypes.TICKET_NR));

	boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
	if (isDescriptionTitleNecessary) {
	    String cellValue = StringUtil.isNotEmptyOrNull(bussinessDayIncremental.getDescription())
		    ? bussinessDayIncremental.getDescription() : "";
	    list.add(TableCellValue.of(cellValue, true, ValueTypes.DESCRIPTION));
	}

	// create Cells for all TimeSnippet's
	list.addAll(collectTimeSnippetData(bussinessDayIncremental));
	list.add(TableCellValue.of(ChargeType.getRepresentation(bussinessDayIncremental.getChargeType())));
	list.add(bussinessDayIncremental.isCharged() ? TableCellValue.of(TextLabel.YES) : TableCellValue.of(TextLabel.NO));
	return list;
    }

    /*
     * Creates a list which contains all Cells with the content about each
     * TimeSnippet
     */
    private List<TimeSnippetCellValue> collectTimeSnippetData(BusinessDayInc4Export bussinessDayIncremental) {
	// = for all time snippet
	List<TimeSnippetCellValue> snippetCells = new ArrayList<>();
	int sequence = 0;
	for (TimeSnippet4Export snippet : bussinessDayIncremental.getTimeSnippets()) {
	    // start point
	    String value = String.valueOf(snippet.getBeginTimeStampRep());
	    snippetCells.add(TimeSnippetCellValue.of(value, sequence, ValueTypes.BEGIN));
	    // end point
	    value = String.valueOf(snippet.getEndTimeStamp());
	    snippetCells.add(TimeSnippetCellValue.of(value, sequence, ValueTypes.END));
	    sequence++;
	}
	for (int i = 0; i < bussinessDayIncremental.getTimeSnippetPlaceHolders().size(); i++) {
	    snippetCells.add(TimeSnippetCellValue.of("", sequence, false, ValueTypes.NONE));
	    sequence++;
	}
	return snippetCells;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	
	TableCellValue tableCellValue = getCellAt(rowIndex, columnIndex);
	
	List<TableCellValue> rowValues = colmnValues.get(rowIndex);
	String newValueAsString = (String) aValue;
	rowValues.set(columnIndex, TableCellValue.of(newValueAsString, tableCellValue));
	handler.handleBusinessDayChanged(ChangedValue.of(getOrderNumber(rowIndex), newValueAsString, tableCellValue.getValueType(),
		getIndexForFromUpto(tableCellValue)));
    }

    private int getIndexForFromUpto(TableCellValue tableCellValue) {
	if (tableCellValue instanceof TimeSnippetCellValue) {
	    return ((TimeSnippetCellValue) tableCellValue).getSequence();
	}
	return -1;
    }

    private int getOrderNumber(int rowIndex) {
	List<TableCellValue> column = colmnValues.get(rowIndex);
	TableCellValue tableCellValue = column.get(0);
	return Integer.valueOf((String) tableCellValue.getValue());
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

	TableCellValue tableCellValue = getCellAt(rowIndex, columnIndex);
	return tableCellValue.isEditable();
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

	TableCellValue tableCellValue = getCellAt(rowIndex, columnIndex);
	return tableCellValue.getValue();
    }

    private TableCellValue getCellAt(int rowIndex, int columnIndex) {
	List<TableCellValue> rowValues = colmnValues.get(rowIndex);
	TableCellValue tableCellValue = rowValues.get(columnIndex);
	return tableCellValue;
    }
}
