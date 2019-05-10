package com.myownb3.dominic.ui.views.overview.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.myownb3.dominic.librarys.text.res.TextLabel;
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

    private List<List<TableCellValue>> colmnValues;
    private List<String> columnNames;

    public BusinessDayTableModel(TableModelListener listener) {
	columnNames = new ArrayList<>();
	colmnValues = new ArrayList<>();
	this.addTableModelListener(listener);
    }

    public void init(BusinessDay4Export bussinessDay) {
	Objects.requireNonNull(bussinessDay);
	this.colmnValues = getBusinessDayCells(bussinessDay);
	this.columnNames = getTableHeaders(bussinessDay);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	Optional<TableCellValue> tableCellValueOptional = getCellOptionalAt(rowIndex, columnIndex);
	String newValueAsString = (String) aValue;

	tableCellValueOptional.ifPresent(tableCellValue -> {
	    if (!StringUtil.isEqual(newValueAsString, tableCellValue.getValue())) {
		tableCellValue.setValue(newValueAsString);
		fireTableCellUpdated(rowIndex, columnIndex);
	    }
	});
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

	Optional<TableCellValue> tableCellValueOpt = getCellOptionalAt(rowIndex, columnIndex);
	return tableCellValueOpt.map(TableCellValue::isEditable).orElse(false);
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
	return (Class) String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

	Optional<TableCellValue> tableCellValue = getCellOptionalAt(rowIndex, columnIndex);
	return tableCellValue.map(TableCellValue::getValue).orElse(null);
    }

    private List<String> getTableHeaders(BusinessDay4Export bussinessDay) {
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

    private List<List<TableCellValue>> getBusinessDayCells(BusinessDay4Export businessDay) {
	List<List<TableCellValue>> businessDayCells = new ArrayList<>();
	int counter = 1;
	for (BusinessDayInc4Export bussinessDayIncremental : businessDay.getBusinessDayIncrements()) {
	    List<TableCellValue> businessDayIncrementalCells = getBusinessDayIncrementalCells(bussinessDayIncremental,
		    businessDay.hasIncrementWithDescription(), counter);
	    businessDayCells.add(businessDayIncrementalCells);
	    counter++;
	}
	return businessDayCells;
    }

    /*
     * Creates a list which contains all Cells that are required to paint a
     * BusinessDayIncremental
     */
    private List<TableCellValue> getBusinessDayIncrementalCells(BusinessDayInc4Export bussinessDayIncremental,
	    boolean isDescriptionTitleNecessary, int no) {
	// create Cells for the introduction of a BD-inc.
	List<TableCellValue> list = new ArrayList<>();
	list.add(TableCellValue.of(no));
	list.add(TableCellValue.of(bussinessDayIncremental.getTotalDurationRep()));
	list.add(TableCellValue.of(bussinessDayIncremental.getTicketNumber(), true, ValueTypes.TICKET_NR));

	if (isDescriptionTitleNecessary) {
	    String cellValue = StringUtil.isNotEmptyOrNull(bussinessDayIncremental.getDescription())
		    ? bussinessDayIncremental.getDescription()
		    : "";
	    list.add(TableCellValue.of(cellValue, true, ValueTypes.DESCRIPTION));
	}

	// create Cells for all TimeSnippet's
	list.addAll(collectTimeSnippetData(bussinessDayIncremental));
	list.add(TableCellValue.of(ChargeType.getRepresentation(bussinessDayIncremental.getChargeType())));
	list.add(bussinessDayIncremental.isCharged() ? TableCellValue.of(TextLabel.YES)
		: TableCellValue.of(TextLabel.NO));
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

    private Optional<TableCellValue> getCellOptionalAt(int rowIndex, int columnIndex) {

	TableCellValue tableCellValue = getCellAt(rowIndex, columnIndex);
	return Optional.ofNullable(tableCellValue);
    }

    /* package */ TableCellValue getCellAt(int rowIndex, int columnIndex) {
	if (rowIndex >= 0 && rowIndex < colmnValues.size()) {
	    List<TableCellValue> rowValues = colmnValues.get(rowIndex);
	    if (columnIndex >= 0 && columnIndex < rowValues.size()) {
		TableCellValue tableCellValue = rowValues.get(columnIndex);
		return tableCellValue;
	    }
	}
	return null;
    }

}
