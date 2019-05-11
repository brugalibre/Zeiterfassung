package com.myownb3.dominic.ui.core.pages.overview.model.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.util.utils.StringUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class BusinessDayTableModelHelper {

    private List<BusinessDayIncTableCellValue> colmnValues;
    private List<TableColumn<BusinessDayIncTableCellValue, String>> columnNames;
   
    private ListChangeListener<? super BusinessDayIncTableCellValue> listChangeListener;

    public BusinessDayTableModelHelper(ListChangeListener<? super BusinessDayIncTableCellValue> listChangeListener) {

	columnNames = new ArrayList<>();
	colmnValues = new ArrayList<>();
	this.listChangeListener = listChangeListener;
    }

    public void init(BusinessDay4Export bussinessDay, TableView<BusinessDayIncTableCellValue> tableView) {
	Objects.requireNonNull(bussinessDay);
	this.colmnValues = getBusinessDayCells(bussinessDay);
	this.columnNames = getTableHeaders(bussinessDay);
	tableView.getColumns().setAll(columnNames);
	ObservableList<BusinessDayIncTableCellValue> observableList = FXCollections.observableList(colmnValues);
	observableList.addListener(listChangeListener);
	tableView.setItems(observableList);
    }
    //
    // @Override
    // public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    //
    // Optional<TableCellValue> tableCellValueOptional = getCellOptionalAt(rowIndex,
    // columnIndex);
    // String newValueAsString = (String) aValue;
    //
    // tableCellValueOptional.ifPresent(tableCellValue -> {
    // if (!StringUtil.isEqual(newValueAsString, tableCellValue.getValue())) {
    // tableCellValue.setValue(newValueAsString);
    // fireTableCellUpdated(rowIndex, columnIndex);
    // }
    // });
    // }
    //
    // @Override
    // public boolean isCellEditable(int rowIndex, int columnIndex) {
    //
    // Optional<TableCellValue> tableCellValueOpt = getCellOptionalAt(rowIndex,
    // columnIndex);
    // return tableCellValueOpt.map(TableCellValue::isEditable).orElse(false);
    // }
    //
    // @Override
    // public int getColumnCount() {
    // return columnNames.size();
    // }
    //
    // @Override
    // public int getRowCount() {
    //
    // return colmnValues.size();
    // }
    //
    // @Override
    // public String getColumnName(int col) {
    // return columnNames.get(col);
    // }
    //
    // @SuppressWarnings({ "unchecked", "rawtypes" })
    // @Override
    // public Class getColumnClass(int c) {
    // return (Class) String.class;
    // }
    //
    // @Override
    // public Object getValueAt(int rowIndex, int columnIndex) {
    //
    // Optional<TableCellValue> tableCellValue = getCellOptionalAt(rowIndex,
    // columnIndex);
    // return tableCellValue.map(TableCellValue::getValue).orElse(null);
    // }

    private List<TableColumn<BusinessDayIncTableCellValue, String>> getTableHeaders(BusinessDay4Export bussinessDay) {
	List<TableColumn<BusinessDayIncTableCellValue, String>> titleHeaders = new ArrayList<>();
	TableColumn<BusinessDayIncTableCellValue, String> numberTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(
		TextLabel.NUMMER_LABEL);
	setCellValueFactory(numberTableColumn,"number");
	titleHeaders.add(numberTableColumn);
	TableColumn<BusinessDayIncTableCellValue, String> amountOfHoursTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(TextLabel.AMOUNT_OF_HOURS_LABEL);
	titleHeaders.add(amountOfHoursTableColumn);
	setCellValueFactory(amountOfHoursTableColumn,"duration");
	TableColumn<BusinessDayIncTableCellValue, String> ticketTableColumnt = new TableColumn<BusinessDayIncTableCellValue, String>(TextLabel.TICKET);
	titleHeaders.add(ticketTableColumnt);
	setCellValueFactory(ticketTableColumnt,"ticketNumber");

	boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
	if (isDescriptionTitleNecessary) {
	    TableColumn<BusinessDayIncTableCellValue, String> descriptionTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(TextLabel.DESCRIPTION_LABEL);
	    titleHeaders.add(descriptionTableColumn);
	    setCellValueFactory(descriptionTableColumn,"description");
	}

	int counter = bussinessDay.getAmountOfVonBisElements();
	List<TableColumn<BusinessDayIncTableCellValue, String>> beginEndHeaders = new ArrayList<>();
	for (int i = 0; i < counter; i++) {
	    TableColumn<BusinessDayIncTableCellValue, String> vonTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(TextLabel.VON_LABEL);
	    beginEndHeaders.add(vonTableColumn);
	    TableColumn<BusinessDayIncTableCellValue, String> bisTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(TextLabel.BIS_LABEL);
	    bisTableColumn.setCellValueFactory(new Callback<CellDataFeatures<BusinessDayIncTableCellValue,String>,ObservableValue<String>>() {
		public ObservableValue<String> call(CellDataFeatures<BusinessDayIncTableCellValue, String> cellData) {
		    BusinessDayIncTableCellValue value = cellData.getValue();
		    
		    return new SimpleStringProperty();
		}
	    });
	    beginEndHeaders.add(bisTableColumn);
	}

	titleHeaders.addAll(beginEndHeaders);

	TableColumn<BusinessDayIncTableCellValue, String> chargeTypeTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(TextLabel.CHARGE_TYPE_LABEL);
	titleHeaders.add(chargeTypeTableColumn);
	setCellValueFactory(chargeTypeTableColumn, "chargeType");
	TableColumn<BusinessDayIncTableCellValue, String> isChargedTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(TextLabel.CHARGED);
	titleHeaders.add(isChargedTableColumn);
	setCellValueFactory(chargeTypeTableColumn, "isCharged");
	return titleHeaders;
    }

    private void setCellValueFactory(TableColumn<BusinessDayIncTableCellValue, String> numberTableColumn, String paramName) {
	numberTableColumn.setCellValueFactory(new PropertyValueFactory<BusinessDayIncTableCellValue, String>(paramName));
    }

    private List<BusinessDayIncTableCellValue> getBusinessDayCells(BusinessDay4Export businessDay) {
	List<BusinessDayIncTableCellValue> businessDayCells = new ArrayList<>();
	int counter = 1;
	for (BusinessDayInc4Export bussinessDayIncremental : businessDay.getBusinessDayIncrements()) {
	    BusinessDayIncTableCellValue businessDayIncrementalCell = getBusinessDayIncrementalCell(
		    bussinessDayIncremental, businessDay.hasIncrementWithDescription(), counter);
	    businessDayCells.add(businessDayIncrementalCell);
	    counter++;
	}
	return businessDayCells;
    }

    /*
     * Creates a list which contains all Cells that are required to paint a
     * BusinessDayIncremental
     */
    private BusinessDayIncTableCellValue getBusinessDayIncrementalCell(BusinessDayInc4Export bussinessDayIncremental,
	    boolean isDescriptionTitleNecessary, int no) {
	// create Cells for the introduction of a BD-inc.

	BusinessDayIncTableCellValue businessDayIncTableCellValue = new BusinessDayIncTableCellValue();

	businessDayIncTableCellValue.setNumber(no);
	businessDayIncTableCellValue.setTotalDuration(bussinessDayIncremental.getTotalDurationRep());
	businessDayIncTableCellValue.setTicketNumber(bussinessDayIncremental.getTicketNumber());

	if (isDescriptionTitleNecessary) {
	    String cellValue = StringUtil.isNotEmptyOrNull(bussinessDayIncremental.getDescription())
		    ? bussinessDayIncremental.getDescription()
		    : "";
	    businessDayIncTableCellValue.setDescritpion(cellValue);
	}

	// create Cells for all TimeSnippet's
	// businessDayIncTableCellValue.setAllTimeSnippets(collectTimeSnippetData(bussinessDayIncremental));
	businessDayIncTableCellValue
		.setChargeType(ChargeType.getRepresentation(bussinessDayIncremental.getChargeType()));
	businessDayIncTableCellValue.setIsCharged(bussinessDayIncremental.isCharged() ? TextLabel.YES : TextLabel.NO);
	return businessDayIncTableCellValue;
    }

    // /*
    // * Creates a list which contains all Cells with the content about each
    // * TimeSnippet
    // */
    // private List<TimeSnippetCellValue>
    // collectTimeSnippetData(BusinessDayInc4Export bussinessDayIncremental) {
    // // = for all time snippet
    // List<TimeSnippetCellValue> snippetCells = new ArrayList<>();
    // int sequence = 0;
    // for (TimeSnippet4Export snippet : bussinessDayIncremental.getTimeSnippets())
    // {
    // // start point
    // String value = String.valueOf(snippet.getBeginTimeStampRep());
    // snippetCells.add(TimeSnippetCellValue.of(value, sequence, ValueTypes.BEGIN));
    // // end point
    // value = String.valueOf(snippet.getEndTimeStamp());
    // snippetCells.add(TimeSnippetCellValue.of(value, sequence, ValueTypes.END));
    // sequence++;
    // }
    // for (int i = 0; i <
    // bussinessDayIncremental.getTimeSnippetPlaceHolders().size(); i++) {
    // snippetCells.add(TimeSnippetCellValue.of("", sequence, false,
    // ValueTypes.NONE));
    // sequence++;
    // }
    // return snippetCells;
    // }
    //
    // private Optional<TableCellValue> getCellOptionalAt(int rowIndex, int
    // columnIndex) {
    //
    // TableCellValue tableCellValue = getCellAt(rowIndex, columnIndex);
    // return Optional.ofNullable(tableCellValue);
    // }
    //
    // /* package */ TableCellValue getCellAt(int rowIndex, int columnIndex) {
    // if (rowIndex >= 0 && rowIndex < colmnValues.size()) {
    // List<TableCellValue> rowValues = colmnValues.get(rowIndex);
    // if (columnIndex >= 0 && columnIndex < rowValues.size()) {
    // TableCellValue tableCellValue = rowValues.get(columnIndex);
    // return tableCellValue;
    // }
    // }
    // return null;
    // }

}