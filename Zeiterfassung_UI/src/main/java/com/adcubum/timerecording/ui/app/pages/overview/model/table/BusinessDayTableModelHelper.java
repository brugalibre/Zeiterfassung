package com.adcubum.timerecording.ui.app.pages.overview.model.table;

import static java.util.Objects.nonNull;
import static javafx.collections.FXCollections.observableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.core.work.businessday.util.BusinessDayUtil;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.ui.core.view.table.EditableCell;

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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class BusinessDayTableModelHelper {

   public static final String TICKET_COLUMN_ID = TextLabel.TICKET;
   private List<BusinessDayIncTableRowValue> colmnValues;
   private List<TableColumn<BusinessDayIncTableRowValue, ?>> columnNames;
   private EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener;
   private EventHandler<CellEditEvent<BusinessDayIncTableRowValue, Ticket>> ticketChangeListener;
   private TableView<BusinessDayIncTableRowValue> tableView;

   public BusinessDayTableModelHelper(EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> changeListener,
         EventHandler<CellEditEvent<BusinessDayIncTableRowValue, Ticket>> ticketChangeListener) {
      columnNames = new ArrayList<>();
      colmnValues = new ArrayList<>();
      this.changeListener = changeListener;
      this.ticketChangeListener = ticketChangeListener;
   }

   /**
    * Selects the row of the current selected item and also the focus
    * This will mark the entire row as selected, instead of only the clicked cell
    * 
    */
   public void selectAndSetFocusToRowOfSelectedCell() {
      BusinessDayIncTableRowValue businessDayIncTableRowValue = tableView.getSelectionModel().getSelectedItem();
      if (nonNull(businessDayIncTableRowValue)) {
         tableView.getSelectionModel().clearSelection();
         tableView.getSelectionModel().select(businessDayIncTableRowValue.getNumberAsInt());
      }
   }

   /**
    * Verifies if the given MouseEvent is a right click on the TableView and also if the TableView
    * has any data displayed
    * 
    * @param event
    *        the MouseEvent
    * @return <code>true</code> if TableView was right clicked and if it contains any data. Otherwise it returns <code>false</code>
    */
   public boolean hasRightClickOnTable(MouseEvent event) {
      return isRightMousePressed(event)
            && event.getSource() == tableView
            && !tableView.getSelectionModel().isEmpty();
   }

   private static boolean isRightMousePressed(MouseEvent event) {
      return event.getEventType() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseButton.SECONDARY;
   }

   ////////////////////////////////////////////
   // Init and build the data model          //
   ////////////////////////////////////////////

   /**
    * Initialises the underlying model of the TableView of this {@link BusinessDayTableModelHelper}
    * 
    * @param bussinessDay
    *        the {@link BusinessDay} which provides the data
    * @param tableView
    *        the TableView which displays the data
    */
   public void init(BusinessDay bussinessDay, TableView<BusinessDayIncTableRowValue> tableView) {
      Objects.requireNonNull(bussinessDay);
      this.tableView = tableView;
      this.colmnValues = getBusinessDayCells(bussinessDay);
      this.columnNames = getTableHeaders(bussinessDay);
      tableView.setEditable(true);
      tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
      tableView.getColumns().setAll(columnNames);
      ObservableList<BusinessDayIncTableRowValue> observableList = observableList(colmnValues);
      tableView.setItems(observableList);
   }

   private List<TableColumn<BusinessDayIncTableRowValue, ?>> getTableHeaders(BusinessDay bussinessDay) {
      List<TableColumn<BusinessDayIncTableRowValue, ?>> titleHeaders = new ArrayList<>();
      TableColumn<BusinessDayIncTableRowValue, String> numberTableColumn = new TableColumn<>(
            TextLabel.NUMMER_LABEL);
      setNonEditableCellValueFactory(numberTableColumn, TableConst.NUMBER);
      titleHeaders.add(numberTableColumn);
      TableColumn<BusinessDayIncTableRowValue, String> amountOfHoursTableColumn = new TableColumn<>(TextLabel.AMOUNT_OF_HOURS_LABEL);
      amountOfHoursTableColumn.setId(TextLabel.AMOUNT_OF_HOURS_LABEL);
      titleHeaders.add(amountOfHoursTableColumn);
      setEditableCellValueFactory(amountOfHoursTableColumn, TableConst.TOTAL_DURATION);
      TableColumn<BusinessDayIncTableRowValue, Ticket> ticketTableColumn = new TableColumn<>(TextLabel.TICKET);
      ticketTableColumn.setId(TICKET_COLUMN_ID);
      titleHeaders.add(ticketTableColumn);
      setEditableTicketCellValueFactory(ticketTableColumn);

      boolean isDescriptionTitleNecessary = bussinessDay.hasDescription();
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
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
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

   private void setEditableTicketCellValueFactory(TableColumn<BusinessDayIncTableRowValue, Ticket> tableColumn) {
      tableColumn.setCellFactory(callback -> EditableTicketCell.createTicketEditCell());
      tableColumn.setCellValueFactory(tableRowValue -> tableRowValue.getValue().getTicketProperty());
      tableColumn.setOnEditCommit(ticketChangeListener);
      tableColumn.setEditable(true);
   }

   private TableCell<BusinessDayIncTableRowValue, String> createEditableTableCell() {
      return EditableCell.createStringEditCell();
   }

   private List<BusinessDayIncTableRowValue> getBusinessDayCells(BusinessDay businessDay) {
      List<BusinessDayIncTableRowValue> businessDayCells = new ArrayList<>();
      int counter = 1;
      for (BusinessDayIncrement bussinessDayIncremental : businessDay.getIncrements()) {
         BusinessDayIncTableRowValue businessDayIncrementalCell = getBusinessDayIncrementalCell(
               bussinessDayIncremental, businessDay.hasDescription(), counter);
         businessDayCells.add(businessDayIncrementalCell);
         counter++;
      }
      return businessDayCells;
   }

   /*
    * Creates a list which contains all Cells that are required to paint a
    * BusinessDayIncremental
    */
   private BusinessDayIncTableRowValue getBusinessDayIncrementalCell(BusinessDayIncrement bussinessDayIncremental,
         boolean isDescriptionTitleNecessary, int no) {
      // create Cells for the introduction of a BD-inc.

      BusinessDayIncTableRowValue businessDayIncTableCellValue = new BusinessDayIncTableRowValue(bussinessDayIncremental.getId());

      businessDayIncTableCellValue.setNumber(String.valueOf(no));
      businessDayIncTableCellValue.setTotalDuration(BusinessDayUtil.getTotalDurationRep(bussinessDayIncremental));
      businessDayIncTableCellValue.setTicket(bussinessDayIncremental.getTicket());

      if (isDescriptionTitleNecessary) {
         String cellValue = bussinessDayIncremental.hasDescription() ? bussinessDayIncremental.getDescription() : "";
         businessDayIncTableCellValue.setDescription(cellValue);
      }
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
      // create Cells for all TimeSnippet's
      businessDayIncTableCellValue.setTimeSnippets(getTimeSnippets(bussinessDayIncremental));
      businessDayIncTableCellValue
            .setChargeType(serviceCodeAdapter.getServiceCodeDescription4ServiceCode(bussinessDayIncremental.getChargeType()));
      businessDayIncTableCellValue.setIsBooked(bussinessDayIncremental.isBooked() ? TextLabel.YES : TextLabel.NO);
      businessDayIncTableCellValue.setValueTypes(isDescriptionTitleNecessary);
      return businessDayIncTableCellValue;
   }

   private BeginAndEndCellValue getTimeSnippets(BusinessDayIncrement bussinessDayIncremental) {
      TimeSnippet snippet = bussinessDayIncremental.getCurrentTimeSnippet();
      String begin = String.valueOf(snippet.getBeginTimeStampRep());
      String end = String.valueOf(snippet.getEndTimeStamp());
      return new BeginAndEndCellValue(TimeSnippetCellValue.of(begin, ValueTypes.BEGIN), TimeSnippetCellValue.of(end, ValueTypes.END));
   }
}
