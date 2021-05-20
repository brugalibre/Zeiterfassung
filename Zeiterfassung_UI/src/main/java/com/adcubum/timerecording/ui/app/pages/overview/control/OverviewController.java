/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.control;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.ResourceBundle;

import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.adcubum.timerecording.ui.app.TimeRecordingTray;
import com.adcubum.timerecording.ui.app.pages.mainpage.control.MainWindowController;
import com.adcubum.timerecording.ui.app.pages.overview.book.service.BookerService;
import com.adcubum.timerecording.ui.app.pages.overview.control.businessdaychange.StringBusinessDayChangeHelper;
import com.adcubum.timerecording.ui.app.pages.overview.control.businessdaychange.TicketBusinessDayChangeHelper;
import com.adcubum.timerecording.ui.app.pages.overview.control.descriptionchange.DescriptionAddHelper;
import com.adcubum.timerecording.ui.app.pages.overview.control.rowdeleter.RowDeleteHelper;
import com.adcubum.timerecording.ui.app.pages.overview.model.OverviewPageModel;
import com.adcubum.timerecording.ui.app.pages.overview.model.resolver.OverviewPageModelResolver;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.BusinessDayTableModelHelper;
import com.adcubum.timerecording.ui.app.pages.overview.view.OverviewPage;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;
import com.adcubum.timerecording.ui.core.control.impl.BaseFXController;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;
import com.adcubum.timerecording.ui.core.view.Page;
import com.adcubum.timerecording.ui.core.view.impl.region.DimensionImpl;
import com.adcubum.timerecording.ui.core.view.region.Dimension;
import com.adcubum.timerecording.ui.core.view.table.TableUtil;
import com.adcubum.timerecording.ui.util.ExceptionUtil;

import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Dominic
 * 
 */
public class OverviewController extends BaseFXController<PageModel, OverviewPageModel>
      implements EventHandler<WindowEvent> {

   private MainWindowController mainWindowController;

   @FXML
   private ProgressIndicator progressIndicator;

   @FXML
   private TableView<BusinessDayIncTableRowValue> tableView;

   @FXML
   private Label totalAmountOfTimeLabel;
   @FXML
   private Label totalAmountOfTimeValue;

   @FXML
   private Button clearButton;
   @FXML
   private Button bookButton;
   @FXML
   private Button exportButton;

   private ContextMenu contextMenu;

   private BookerService bookerService;
   private DescriptionAddHelper descAddHelper;
   private BusinessDayTableModelHelper businessDayTableModel;
   private TimeRecordingTray timeRecordingTray;

   private Stage stage;

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      initialize(new OverviewPage(this));
   }

   @Override
   public void initialize(Page<PageModel, OverviewPageModel> page) {
      createBookerService();
      super.initialize(page);
      businessDayTableModel = new BusinessDayTableModelHelper(new StringBusinessDayChangeHelper(finishAction -> refreshUI()),
            new TicketBusinessDayChangeHelper(finishAction -> refreshUI()));
      setBinding(dataModel);
      initContextMenu();
      initTable();
   }

   private void createBookerService() {
      this.bookerService = new BookerService();
      bookerService.setOnSucceeded(onSucceededHandler());
      bookerService.setOnFailed(onFailedHandler());
   }

   private EventHandler<WorkerStateEvent> onFailedHandler() {
      return workerStateEvent -> {
         Worker<?> worker = workerStateEvent.getSource();
         // Not sure if we ever get here without an exception. Well, if so we're doomed
         if (nonNull(worker.getException())) {
            ExceptionUtil.showException(Thread.currentThread(), worker.getException());
         }
      };
   }

   private EventHandler<WorkerStateEvent> onSucceededHandler() {
      return workerStateEvent -> {
         Boolean res = bookerService.getValue();
         if (nonNull(res) && res.booleanValue()) {
            refreshUI();
         }
      };
   }

   @Override
   public void show(PageModel dataModelIn) {
      super.show(dataModelIn);
      BusinessDayVO businessDayVO = dataModel.getBusinessDayVO();
      businessDayTableModel.init(businessDayVO, tableView);
      descAddHelper.setDisable(dataModel.getHasBusinessDayDescription().getValue());
      TableUtil.autoResizeTable(tableView);
      Dimension newDimension = new DimensionImpl(tableView.getPrefWidth(), getDimension().getPrefHeight());
      initStage4NewComponent(stage, newDimension);
   }

   public void init(MainWindowController mainWindowController) {
      this.mainWindowController = mainWindowController;
   }

   private void handleMouseEvent(MouseEvent event) {
      if (hasRightClickOnTable(event) && !tableView.getSelectionModel().isEmpty()) {
         BusinessDayIncTableRowValue businessDayIncTableRowValue = tableView.getSelectionModel().getSelectedItem();
         setFocusToRow(tableView, businessDayIncTableRowValue.getNumberAsInt());
         contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
         event.consume();
      } else {
         contextMenu.hide();
      }
   }

   private void setFocusToRow(TableView<?> tableView, int selectedRow) {
      tableView.getSelectionModel().clearSelection();
      tableView.getSelectionModel().select(selectedRow);
   }

   private boolean hasRightClickOnTable(MouseEvent event) {
      return event.getEventType() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseButton.SECONDARY
            && event.getSource() instanceof TableView<?>;
   }

   @FXML
   private void onAction(ActionEvent actionEvent) {
      if (actionEvent.getSource() == clearButton) {
         mainWindowController.clearBusinessDayContents();
         mainWindowController.dispose();
      } else if (actionEvent.getSource() == bookButton) {
         bookerService.book();
      } else if (actionEvent.getSource() == exportButton) {
         timeRecordingTray.export();
      }
   }

   @Override
   public void handle(WindowEvent event) {
      if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
         dispose();
      }
   }

   private void dispose() {
      descAddHelper.onDispose();
   }

   @Override
   protected PageModelResolver<PageModel, OverviewPageModel> createPageModelResolver() {
      return new OverviewPageModelResolver();
   }

   @Override
   protected void setBinding(OverviewPageModel pageVO) {
      bookButton.disableProperty().bind(dataModel.getIsChargeButtonDisabled());
      clearButton.disableProperty().bind(dataModel.getIsClearButtonDisabled());
      exportButton.disableProperty().bind(dataModel.getIsExportButtonDisabled());

      bookButton.textProperty().bind(dataModel.getBookButtonLabel());
      clearButton.textProperty().bind(dataModel.getClearButtonLabel());
      exportButton.textProperty().bind(dataModel.getExportButtonLabel());

      totalAmountOfTimeLabel.textProperty().bind(dataModel.getTotalAmountOfTimeLabel());
      totalAmountOfTimeValue.textProperty().bind(dataModel.getTotalAmountOfTimeValue());
      bookerService.bind(progressIndicator);
   }

   private void initContextMenu() {
      contextMenu = new ContextMenu();
      RowDeleteHelper rowDeleteHelper = new RowDeleteHelper(action -> refreshUI(), tableView);
      descAddHelper = new DescriptionAddHelper(this::onDescriptionChangeFinish, contextMenu, tableView);
      contextMenu.getItems().add(rowDeleteHelper.getDeleteMenuItem());
      contextMenu.getItems().add(descAddHelper.getChangeDescriptionMenuItem());
   }

   private void initTable() {
      tableView.setOnMousePressed(this::handleMouseEvent);
      tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
   }

   private void onDescriptionChangeFinish(FinishAction finishAction) {
      switch (finishAction) {
         case FINISH:
            refreshUI();
            break;
         case ABORT:
            descAddHelper.setDisable(false);
            break;
         default:
            throw new IllegalStateException("Unsupported finish action '" + finishAction + "'!");
      }
   }

   private void refreshUI() {
      refresh();
      timeRecordingTray.updateUIStates();
   }

   public void setTimeRecordingTray(TimeRecordingTray timeRecordingTray) {
      this.timeRecordingTray = requireNonNull(timeRecordingTray);
   }

   public void setMainPanel(Stage stage) {
      this.stage = requireNonNull(stage);
   }

}
