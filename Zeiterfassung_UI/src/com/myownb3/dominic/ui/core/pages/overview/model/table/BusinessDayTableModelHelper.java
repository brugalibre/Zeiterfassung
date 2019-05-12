package com.myownb3.dominic.ui.core.pages.overview.model.table;

import static javafx.collections.FXCollections.observableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.TimeSnippet4Export;
import com.myownb3.dominic.util.utils.StringUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

public class BusinessDayTableModelHelper {

    private List<BusinessDayIncTableRowValue> colmnValues;
    private List<TableColumn<BusinessDayIncTableRowValue, String>> columnNames;
    private EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener;

    public BusinessDayTableModelHelper(
	    EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener) {

	columnNames = new ArrayList<>();
	colmnValues = new ArrayList<>();
	this.changeListener = changeListener;
    }

    public void init(BusinessDay4Export bussinessDay, TableView<BusinessDayIncTableRowValue> tableView) {
	Objects.requireNonNull(bussinessDay);
	this.colmnValues = getBusinessDayCells(bussinessDay);
	this.columnNames = getTableHeaders(bussinessDay);
	tableView.setEditable(true);
	tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
	tableView.getColumns().setAll(columnNames);
	ObservableList<BusinessDayIncTableRowValue> observableList = observableList(colmnValues);
	tableView.setItems(observableList);
    }

    private List<TableColumn<BusinessDayIncTableRowValue, String>> getTableHeaders(BusinessDay4Export bussinessDay) {
	List<TableColumn<BusinessDayIncTableRowValue, String>> titleHeaders = new ArrayList<>();
	TableColumn<BusinessDayIncTableRowValue, String> numberTableColumn = new TableColumn<BusinessDayIncTableRowValue, String>(
		TextLabel.NUMMER_LABEL);
	setCellValueFactory(numberTableColumn, "number");
	titleHeaders.add(numberTableColumn);
	TableColumn<BusinessDayIncTableRowValue, String> amountOfHoursTableColumn = new TableColumn<BusinessDayIncTableRowValue, String>(
		TextLabel.AMOUNT_OF_HOURS_LABEL);
	titleHeaders.add(amountOfHoursTableColumn);
	setCellValueFactory(amountOfHoursTableColumn, "totalDuration");
	TableColumn<BusinessDayIncTableRowValue, String> ticketTableColumnt = new TableColumn<BusinessDayIncTableRowValue, String>(
		TextLabel.TICKET);
	titleHeaders.add(ticketTableColumnt);
	setEditableCellValueFactory(ticketTableColumnt, "ticketNumber", changeListener);

	boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
	if (isDescriptionTitleNecessary) {
	    TableColumn<BusinessDayIncTableRowValue, String> descriptionTableColumn = new TableColumn<BusinessDayIncTableRowValue, String>(
		    TextLabel.DESCRIPTION_LABEL);
	    setEditableCellValueFactory(descriptionTableColumn, "description", changeListener);
	    titleHeaders.add(descriptionTableColumn);
	}

	int counter = bussinessDay.getAmountOfVonBisElements();
	List<TableColumn<BusinessDayIncTableRowValue, String>> beginEndHeaders = new ArrayList<>();
	for (int i = 0; i < counter; i++) {

	    TableColumn<BusinessDayIncTableRowValue, String> beginTableColumn = new TableColumn<BusinessDayIncTableRowValue, String>(
		    TextLabel.VON_LABEL);

	    TableColumn<BusinessDayIncTableRowValue, String> endTableColumn = new TableColumn<BusinessDayIncTableRowValue, String>(
		    TextLabel.BIS_LABEL);

	    beginEndHeaders.add(beginTableColumn);
	    beginEndHeaders.add(endTableColumn);
	}

	for (int i = 0; i < beginEndHeaders.size(); i++) {
	    TableColumn<BusinessDayIncTableRowValue, String> tableColumn = beginEndHeaders.get(i);

	    tableColumn.setCellValueFactory(getTimeSnippetBeginEndCellValueFactory(i, tableColumn));
	    tableColumn.setCellFactory(getTimeSnippetBeginEndCellFactory(i, tableColumn));
	}

	titleHeaders.addAll(beginEndHeaders);

	TableColumn<BusinessDayIncTableRowValue, String> chargeTypeTableColumn = new TableColumn<BusinessDayIncTableRowValue, String>(
		TextLabel.CHARGE_TYPE_LABEL);
	titleHeaders.add(chargeTypeTableColumn);
	setCellValueFactory(chargeTypeTableColumn, "chargeType");
	TableColumn<BusinessDayIncTableRowValue, String> isChargedTableColumn = new TableColumn<BusinessDayIncTableRowValue, String>(
		TextLabel.CHARGED);
	titleHeaders.add(isChargedTableColumn);
	setCellValueFactory(isChargedTableColumn, "isCharged");
	return titleHeaders;
    }

    private Callback<CellDataFeatures<BusinessDayIncTableRowValue, String>, ObservableValue<String>> getTimeSnippetBeginEndCellValueFactory(
	    final int i, TableColumn<BusinessDayIncTableRowValue, String> tableColumn) {
	return cellData -> {
	    BusinessDayIncTableRowValue businessDayIncTableCellValue = cellData.getValue();

	    TimeSnippetCellValue timeSnippet4Index = businessDayIncTableCellValue.getTimeSnippet(i);
	    return new SimpleStringProperty(timeSnippet4Index.getBeginOrEnd());
	};
    }

    private Callback<TableColumn<BusinessDayIncTableRowValue, String>, TableCell<BusinessDayIncTableRowValue, String>> getTimeSnippetBeginEndCellFactory(
	    final int i, TableColumn<BusinessDayIncTableRowValue, String> tableColumn) {
	return cellData -> {
	    // XXX Ugly hack: cellData.getCellData(i) is null although there is a value
	    // visible on the table
	    if (true || !StringUtil.isEmptyOrNull(cellData.getCellData(i))) {
		tableColumn.editableProperty().set(true);
		tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		tableColumn.setOnEditCommit(changeListener);
		return new TextFieldTableCell<>(new DefaultStringConverter());
	    }
	    return new TextFieldTableCell<>(new DefaultStringConverter());
	    // return new TableCell<>();
	};
    }

    private void setCellValueFactory(TableColumn<BusinessDayIncTableRowValue, String> tableColumn, String paramName) {
	tableColumn.setCellValueFactory(new PropertyValueFactory<BusinessDayIncTableRowValue, String>(paramName));
    }

    private void setEditableCellValueFactory(TableColumn<BusinessDayIncTableRowValue, String> tableColumn,
	    String paramName, EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener) {
	tableColumn.setEditable(true);
	tableColumn.setOnEditCommit(changeListener);
	Callback<TableColumn<BusinessDayIncTableRowValue, String>, TableCell<BusinessDayIncTableRowValue, String>> forTableColumn = TextFieldTableCell
		.forTableColumn();
	tableColumn.setCellFactory(forTableColumn);
	setCellValueFactory(tableColumn, paramName);
    }

    private List<BusinessDayIncTableRowValue> getBusinessDayCells(BusinessDay4Export businessDay) {
	List<BusinessDayIncTableRowValue> businessDayCells = new ArrayList<>();
	int counter = 1;
	for (BusinessDayInc4Export bussinessDayIncremental : businessDay.getBusinessDayIncrements()) {
	    BusinessDayIncTableRowValue businessDayIncrementalCell = getBusinessDayIncrementalCell(
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
    private BusinessDayIncTableRowValue getBusinessDayIncrementalCell(BusinessDayInc4Export bussinessDayIncremental,
	    boolean isDescriptionTitleNecessary, int no) {
	// create Cells for the introduction of a BD-inc.

	BusinessDayIncTableRowValue businessDayIncTableCellValue = new BusinessDayIncTableRowValue();

	businessDayIncTableCellValue.setNumber(String.valueOf(no));
	businessDayIncTableCellValue.setTotalDuration(bussinessDayIncremental.getTotalDurationRep());
	businessDayIncTableCellValue.setTicketNumber(bussinessDayIncremental.getTicketNumber());

	if (isDescriptionTitleNecessary) {
	    String cellValue = StringUtil.isNotEmptyOrNull(bussinessDayIncremental.getDescription())
		    ? bussinessDayIncremental.getDescription()
		    : "";
	    businessDayIncTableCellValue.setDescription(cellValue);
	}

	// create Cells for all TimeSnippet's
	businessDayIncTableCellValue.setTimeSnippets(getTimeSnippets(bussinessDayIncremental));
	businessDayIncTableCellValue
		.setChargeType(ChargeType.getRepresentation(bussinessDayIncremental.getChargeType()));
	businessDayIncTableCellValue.setIsCharged(bussinessDayIncremental.isCharged() ? TextLabel.YES : TextLabel.NO);

	businessDayIncTableCellValue.setValueTypes(isDescriptionTitleNecessary);
	return businessDayIncTableCellValue;
    }

    /*
     * Creates a list which contains all Cells with the content about each
     * TimeSnippet
     */
    private List<TimeSnippetCellValue> getTimeSnippets(BusinessDayInc4Export bussinessDayIncremental) {
	// = for all time snippet
	List<TimeSnippetCellValue> snippetCells = new ArrayList<>();
	int sequence = 0;
	for (TimeSnippet4Export snippet : bussinessDayIncremental.getTimeSnippets()) {
	    String begin = String.valueOf(snippet.getBeginTimeStampRep());
	    snippetCells.add(TimeSnippetCellValue.of(begin, sequence, ValueTypes.BEGIN));
	    String end = String.valueOf(snippet.getEndTimeStamp());
	    snippetCells.add(TimeSnippetCellValue.of(end, sequence, ValueTypes.END));
	    sequence++;
	}
	for (int i = 0; i < bussinessDayIncremental.getTimeSnippetPlaceHolders().size(); i++) {
	    snippetCells.add(TimeSnippetCellValue.of("", sequence, ValueTypes.NONE));
	    sequence++;
	}
	return snippetCells;
    }
}
