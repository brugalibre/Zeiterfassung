/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.control;

import com.adcubum.timerecording.app.book.TimeRecorderBookResult;
import com.adcubum.timerecording.ui.app.pages.mainpage.control.callback.MainWindowCallbackHandler;
import com.adcubum.timerecording.ui.app.pages.overview.book.service.BookerService;
import com.adcubum.timerecording.ui.app.pages.overview.control.businessdaychange.BusinessDayChangeHelperGrouper;
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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * @author Dominic
 * 
 */
public class OverviewController extends BaseFXController<PageModel, OverviewPageModel>
      implements EventHandler<WindowEvent> {

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

   private MainWindowCallbackHandler windowCallbackHandler;
   private Stage stage;

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      initialize(new OverviewPage(this));
   }

   @Override
   public void initialize(Page<PageModel, OverviewPageModel> page) {
      createBookerService();
      super.initialize(page);
      businessDayTableModel = new BusinessDayTableModelHelper(new BusinessDayChangeHelperGrouper(finishAction -> refreshUI()));
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
         TimeRecorderBookResult res = bookerService.getValue();
         if (nonNull(res) && res.hasBooked()) {
            refreshUI();
         }
      };
   }

   @Override
   public void show(PageModel dataModelIn) {
      super.show(dataModelIn);
      businessDayTableModel.init(dataModel.getBusinessDay(), tableView);
      descAddHelper.setDisable(dataModel.getHasBusinessDayDescription().getValue());
      TableUtil.autoResizeTable(tableView);
      Dimension newDimension = new DimensionImpl(tableView.getPrefWidth(), getDimension().getPrefHeight());
      initStage4NewComponent(stage, newDimension);
   }

   private void handleMouseEvent(MouseEvent event) {
      if (businessDayTableModel.hasRightClickOnTable(event)) {
         businessDayTableModel.selectAndSetFocusToRowOfSelectedCell();
         contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
         event.consume();
      } else {
         contextMenu.hide();
      }
   }

   @FXML
   private void onAction(ActionEvent actionEvent) {
      if (actionEvent.getSource() == clearButton) {
         windowCallbackHandler.clearBusinessDayContentsAndDispose();
      } else if (actionEvent.getSource() == bookButton) {
         bookerService.book();
      } else if (actionEvent.getSource() == exportButton) {
         windowCallbackHandler.export();
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
      windowCallbackHandler.updateUIStates();
   }

   public void init(MainWindowCallbackHandler windowCallbackHandler, Stage stage) {
      this.windowCallbackHandler = requireNonNull(windowCallbackHandler);
      this.stage = stage;
   }
}
