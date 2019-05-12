package com.myownb3.dominic.ui.core.pages.overview.model.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.TimeSnippet4Export;
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

    private List<TableColumn<BusinessDayIncTableCellValue, String>> getTableHeaders(BusinessDay4Export bussinessDay) {
	List<TableColumn<BusinessDayIncTableCellValue, String>> titleHeaders = new ArrayList<>();
	TableColumn<BusinessDayIncTableCellValue, String> numberTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(
		TextLabel.NUMMER_LABEL);
	setCellValueFactory(numberTableColumn, "number");
	titleHeaders.add(numberTableColumn);
	TableColumn<BusinessDayIncTableCellValue, String> amountOfHoursTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(
		TextLabel.AMOUNT_OF_HOURS_LABEL);
	titleHeaders.add(amountOfHoursTableColumn);
	setCellValueFactory(amountOfHoursTableColumn, "totalDuration");
	TableColumn<BusinessDayIncTableCellValue, String> ticketTableColumnt = new TableColumn<BusinessDayIncTableCellValue, String>(
		TextLabel.TICKET);
	titleHeaders.add(ticketTableColumnt);
	setCellValueFactory(ticketTableColumnt, "ticketNumber");

	boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
	if (isDescriptionTitleNecessary) {
	    TableColumn<BusinessDayIncTableCellValue, String> descriptionTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(
		    TextLabel.DESCRIPTION_LABEL);
	    titleHeaders.add(descriptionTableColumn);
	    setCellValueFactory(descriptionTableColumn, "description");
	}

	int counter = bussinessDay.getAmountOfVonBisElements();
	List<TableColumn<BusinessDayIncTableCellValue, String>> beginEndHeaders = new ArrayList<>();
	for (int i = 0; i < counter; i++) {
	    TableColumn<BusinessDayIncTableCellValue, String> beginTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(
		    TextLabel.VON_LABEL);
	    beginEndHeaders.add(beginTableColumn);
	    beginTableColumn.setCellValueFactory(
		    getTimeSnippetBeginEndCellValueFactory(i, timeSnippet -> timeSnippet.getBegin()));
	    TableColumn<BusinessDayIncTableCellValue, String> endTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(
		    TextLabel.BIS_LABEL);
	    endTableColumn.setCellValueFactory(
		    getTimeSnippetBeginEndCellValueFactory(i, timeSnippet -> timeSnippet.getEnd()));
	    beginEndHeaders.add(endTableColumn);
	}

	titleHeaders.addAll(beginEndHeaders);

	TableColumn<BusinessDayIncTableCellValue, String> chargeTypeTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(
		TextLabel.CHARGE_TYPE_LABEL);
	titleHeaders.add(chargeTypeTableColumn);
	setCellValueFactory(chargeTypeTableColumn, "chargeType");
	TableColumn<BusinessDayIncTableCellValue, String> isChargedTableColumn = new TableColumn<BusinessDayIncTableCellValue, String>(
		TextLabel.CHARGED);
	titleHeaders.add(isChargedTableColumn);
	setCellValueFactory(isChargedTableColumn, "isCharged");
	return titleHeaders;
    }

    private Callback<CellDataFeatures<BusinessDayIncTableCellValue, String>, ObservableValue<String>> getTimeSnippetBeginEndCellValueFactory(
	    final int i, Function<TimeSnippetCellValue, String> timeSnippetFunction) {
	return cellData -> {
	    BusinessDayIncTableCellValue businessDayIncTableCellValue = cellData.getValue();
	    TimeSnippetCellValue timeSnippet4Index = businessDayIncTableCellValue.getTimeSnippet4Index(i);
	    return new SimpleStringProperty(timeSnippetFunction.apply(timeSnippet4Index));
	};
    }

    private void setCellValueFactory(TableColumn<BusinessDayIncTableCellValue, String> numberTableColumn,
	    String paramName) {
	numberTableColumn
		.setCellValueFactory(new PropertyValueFactory<BusinessDayIncTableCellValue, String>(paramName));
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
	    String end = String.valueOf(snippet.getEndTimeStamp());
	    snippetCells.add(TimeSnippetCellValue.of(begin, end, sequence));
	    sequence++;
	}
	for (int i = 0; i < bussinessDayIncremental.getTimeSnippetPlaceHolders().size(); i++) {
	    snippetCells.add(TimeSnippetCellValue.of("", "", sequence));
	    sequence++;
	}
	return snippetCells;
    }
}
