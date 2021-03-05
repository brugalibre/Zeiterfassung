package com.myownb3.dominic.ui.app.pages.overview.model.table;

import static javafx.collections.FXCollections.observableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.myownb3.dominic.ui.core.view.table.EditableCell;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class BusinessDayTableModelHelper {

   private List<BusinessDayIncTableRowValue> colmnValues;
   private List<TableColumn<BusinessDayIncTableRowValue, String>> columnNames;
   private EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener;

   public BusinessDayTableModelHelper(EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener) {
      columnNames = new ArrayList<>();
      colmnValues = new ArrayList<>();
      this.changeListener = changeListener;
   }

   public void init(BusinessDayVO bussinessDay, TableView<BusinessDayIncTableRowValue> tableView) {
      Objects.requireNonNull(bussinessDay);
      this.colmnValues = getBusinessDayCells(bussinessDay);
      this.columnNames = getTableHeaders(bussinessDay);
      tableView.setEditable(true);
      tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
      tableView.getColumns().setAll(columnNames);
      ObservableList<BusinessDayIncTableRowValue> observableList = observableList(colmnValues);
      tableView.setItems(observableList);
   }

   private List<TableColumn<BusinessDayIncTableRowValue, String>> getTableHeaders(BusinessDayVO bussinessDay) {
      List<TableColumn<BusinessDayIncTableRowValue, String>> titleHeaders = new ArrayList<>();
      TableColumn<BusinessDayIncTableRowValue, String> numberTableColumn = new TableColumn<>(
            TextLabel.NUMMER_LABEL);
      setNonEditableCellValueFactory(numberTableColumn, TableConst.NUMBER);
      titleHeaders.add(numberTableColumn);
      TableColumn<BusinessDayIncTableRowValue, String> amountOfHoursTableColumn = new TableColumn<>(TextLabel.AMOUNT_OF_HOURS_LABEL);
      amountOfHoursTableColumn.setId(TextLabel.AMOUNT_OF_HOURS_LABEL);
      titleHeaders.add(amountOfHoursTableColumn);
      setEditableCellValueFactory(amountOfHoursTableColumn, TableConst.TOTAL_DURATION);
      TableColumn<BusinessDayIncTableRowValue, String> ticketTableColumn = new TableColumn<>(TextLabel.TICKET);
      ticketTableColumn.setId(TextLabel.TICKET);
      titleHeaders.add(ticketTableColumn);
      setEditableCellValueFactory(ticketTableColumn, TableConst.TICKET_NUMBER);

      boolean isDescriptionTitleNecessary = bussinessDay.hasIncrementWithDescription();
      if (isDescriptionTitleNecessary) {
         TableColumn<BusinessDayIncTableRowValue, String> descriptionTableColumn = new TableColumn<>(TextLabel.DESCRIPTION_LABEL);
         setEditableCellValueFactory(descriptionTableColumn, TableConst.DESCRIPTION);
         titleHeaders.add(descriptionTableColumn);
      }

      TableColumn<BusinessDayIncTableRowValue, String> beginTableColumn = new TableColumn<>(TextLabel.VON_LABEL);
      beginTableColumn.setId(TextLabel.VON_LABEL);
      TableColumn<BusinessDayIncTableRowValue, String> endTableColumn = new TableColumn<>(TextLabel.BIS_LABEL);
      endTableColumn.setId(TextLabel.BIS_LABEL);
      setEditableCellValueFactory(beginTableColumn, TableConst.BEGIN);
      beginTableColumn.setCellValueFactory(getTimeSnippetBeginCellValueFactory());
      setEditableCellValueFactory(endTableColumn, TableConst.END);
      endTableColumn.setCellValueFactory(getTimeSnippetEndCellValueFactory());

      titleHeaders.add(beginTableColumn);
      titleHeaders.add(endTableColumn);

      TableColumn<BusinessDayIncTableRowValue, String> bookTypeTableColumn = new TableColumn<>(TextLabel.BOOK_TYPE_LABEL);
      titleHeaders.add(bookTypeTableColumn);
      setEditableColumBoxCellFactory(bookTypeTableColumn);
      TableColumn<BusinessDayIncTableRowValue, String> isBookedTableColumn = new TableColumn<>(TextLabel.BOOKED);
      titleHeaders.add(isBookedTableColumn);
      setNonEditableCellValueFactory(isBookedTableColumn, TableConst.IS_BOOKED);
      return titleHeaders;
   }

   private void setEditableColumBoxCellFactory(
         TableColumn<BusinessDayIncTableRowValue, String> chargeTypeTableColumn) {
      List<String> allServiceCodes = readAllServiceCodes();
      chargeTypeTableColumn.setCellValueFactory(cellData -> cellData.getValue().chargeTypeProperty());
      // We should probably write our own cell factory in order to dynamically evaluate the possible service codes for the given Ticket
      chargeTypeTableColumn.setCellFactory(ComboBoxTableCell.forTableColumn(allServiceCodes.toArray(new String[] {})));
      chargeTypeTableColumn.editableProperty().set(true);
      chargeTypeTableColumn.setOnEditCommit(changeListener);
   }

   private static List<String> readAllServiceCodes() {
      ServiceCodeAdapter serviceCodeAdapter = TimeRecorder.INSTANCE.getServiceCodeAdapter();
      return serviceCodeAdapter.getAllServiceCodes();
   }

   private Callback<CellDataFeatures<BusinessDayIncTableRowValue, String>, ObservableValue<String>> getTimeSnippetBeginCellValueFactory() {
      return cellData -> {
         BusinessDayIncTableRowValue businessDayIncTableCellValue = cellData.getValue();
         TimeSnippetCellValue beginTimeSnippet = businessDayIncTableCellValue.getBeginTimeSnippet();
         return new SimpleStringProperty(beginTimeSnippet.getBeginOrEnd());
      };
   }

   private Callback<CellDataFeatures<BusinessDayIncTableRowValue, String>, ObservableValue<String>> getTimeSnippetEndCellValueFactory() {
      return cellData -> {
         BusinessDayIncTableRowValue businessDayIncTableCellValue = cellData.getValue();
         TimeSnippetCellValue beginTimeSnippet = businessDayIncTableCellValue.getEndTimeSnippet();
         return new SimpleStringProperty(beginTimeSnippet.getBeginOrEnd());
      };
   }

   private void setNonEditableCellValueFactory(TableColumn<BusinessDayIncTableRowValue, String> tableColumn, String paramName) {
      tableColumn.setCellValueFactory(new PropertyValueFactory<>(paramName));
   }

   private void setEditableCellValueFactory(TableColumn<BusinessDayIncTableRowValue, String> tableColumn, String paramName) {
      tableColumn.setEditable(true);
      tableColumn.setOnEditCommit(changeListener);
      tableColumn.setCellFactory(column -> createEditableTableCell());
      setNonEditableCellValueFactory(tableColumn, paramName);// For diplaying the value read only
   }

   private TableCell<BusinessDayIncTableRowValue, String> createEditableTableCell() {
      return EditableCell.createStringEditCell();
   }

   private List<BusinessDayIncTableRowValue> getBusinessDayCells(BusinessDayVO businessDay) {
      List<BusinessDayIncTableRowValue> businessDayCells = new ArrayList<>();
      int counter = 1;
      for (BusinessDayIncrementVO bussinessDayIncremental : businessDay.getBusinessDayIncrements()) {
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
   private BusinessDayIncTableRowValue getBusinessDayIncrementalCell(BusinessDayIncrementVO bussinessDayIncremental,
         boolean isDescriptionTitleNecessary, int no) {
      // create Cells for the introduction of a BD-inc.

      BusinessDayIncTableRowValue businessDayIncTableCellValue = new BusinessDayIncTableRowValue();

      businessDayIncTableCellValue.setNumber(String.valueOf(no));
      businessDayIncTableCellValue.setTotalDuration(bussinessDayIncremental.getTotalDurationRep());
      businessDayIncTableCellValue.setTicketNumber(bussinessDayIncremental.getTicketNumber());

      if (isDescriptionTitleNecessary) {
         String cellValue = bussinessDayIncremental.hasDescription() ? bussinessDayIncremental.getDescription() : "";
         businessDayIncTableCellValue.setDescription(cellValue);
      }
      ServiceCodeAdapter serviceCodeAdapter = TimeRecorder.INSTANCE.getServiceCodeAdapter();
      // create Cells for all TimeSnippet's
      businessDayIncTableCellValue.setTimeSnippets(getTimeSnippets(bussinessDayIncremental));
      businessDayIncTableCellValue
            .setChargeType(serviceCodeAdapter.getServiceCodeDescription4ServiceCode(bussinessDayIncremental.getChargeType()));
      businessDayIncTableCellValue.setIsBooked(bussinessDayIncremental.isBooked() ? TextLabel.YES : TextLabel.NO);
      businessDayIncTableCellValue.setValueTypes(isDescriptionTitleNecessary);
      return businessDayIncTableCellValue;
   }

   private BeginAndEndCellValue getTimeSnippets(BusinessDayIncrementVO bussinessDayIncremental) {
      TimeSnippet snippet = bussinessDayIncremental.getCurrentTimeSnippet();
      String begin = String.valueOf(snippet.getBeginTimeStampRep());
      String end = String.valueOf(snippet.getEndTimeStamp());
      return new BeginAndEndCellValue(TimeSnippetCellValue.of(begin, ValueTypes.BEGIN), TimeSnippetCellValue.of(end, ValueTypes.END));
   }
}
