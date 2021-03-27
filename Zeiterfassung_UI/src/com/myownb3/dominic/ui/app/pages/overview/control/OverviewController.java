/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.control;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.ResourceBundle;

import com.adcubum.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.app.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.app.pages.overview.book.service.BookerService;
import com.myownb3.dominic.ui.app.pages.overview.control.businessdaychange.StringBusinessDayChangeHelper;
import com.myownb3.dominic.ui.app.pages.overview.control.businessdaychange.TicketBusinessDayChangeHelper;
import com.myownb3.dominic.ui.app.pages.overview.control.descriptionchange.DescriptionAddHelper;
import com.myownb3.dominic.ui.app.pages.overview.control.rowdeleter.RowDeleteHelper;
import com.myownb3.dominic.ui.app.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.app.pages.overview.model.resolver.OverviewPageModelResolver;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayTableModelHelper;
import com.myownb3.dominic.ui.app.pages.overview.view.OverviewPage;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.control.FinishAction;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.view.Page;
import com.myownb3.dominic.ui.core.view.table.TableUtil;
import com.myownb3.dominic.ui.util.ExceptionUtil;

import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Dominic
 * 
 */
public class OverviewController extends BaseFXController<OverviewPageModel, OverviewPageModel>
      implements EventHandler<WindowEvent> {

   private MainWindowController mainWindowController;

   @FXML
   private ProgressIndicator progressIndicator;

   @FXML
   private Pane mainPane;

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

   private MenuItem changeDescriptionMenue;

   private Stage stage;

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      initialize(new OverviewPage(this));
   }

   @Override
   public void initialize(Page<OverviewPageModel, OverviewPageModel> page) {
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
   public void show() {
      super.show();
      BusinessDayVO businessDayVO = getDataModel().getBusinessDayVO();
      businessDayTableModel.init(businessDayVO, tableView);
      changeDescriptionMenue.setDisable(TimeRecorder.INSTANCE.hasBusinessDayDescription());
      TableUtil.autoResizeTable(tableView);
      stage.setWidth(tableView.getPrefWidth());
      mainPane.setPrefWidth(tableView.getPrefWidth());
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
   protected PageModelResolver<OverviewPageModel, OverviewPageModel> createPageModelResolver() {
      return new OverviewPageModelResolver();
   }

   @Override
   protected void setBinding(OverviewPageModel pageVO) {
      bookButton.disableProperty().bind(getDataModel().getIsChargeButtonDisabled());
      clearButton.disableProperty().bind(getDataModel().getIsClearButtonDisabled());
      exportButton.disableProperty().bind(getDataModel().getIsExportButtonDisabled());

      bookButton.textProperty().bind(getDataModel().getBookButtonLabel());
      clearButton.textProperty().bind(getDataModel().getClearButtonLabel());
      exportButton.textProperty().bind(getDataModel().getExportButtonLabel());

      totalAmountOfTimeLabel.textProperty().bind(getDataModel().getTotalAmountOfTimeLabel());
      totalAmountOfTimeValue.textProperty().bind(getDataModel().getTotalAmountOfTimeValue());
      bookerService.bind(progressIndicator);
   }

   private void initContextMenu() {

      RowDeleteHelper rowDeleteHelper = new RowDeleteHelper(action -> refreshUI());
      descAddHelper = new DescriptionAddHelper(this::onDescriptionChangeFinish);
      MenuItem deleteMenue = new MenuItem(TextLabel.DELETE_ROW);
      deleteMenue.setOnAction(event -> rowDeleteHelper.deleteRow(event, tableView));
      changeDescriptionMenue = new MenuItem(TextLabel.CHANGE_DESCRIPTION);
      changeDescriptionMenue.setOnAction(onDescriptionChange());
      contextMenu = new ContextMenu();
      contextMenu.getItems().add(deleteMenue);
      contextMenu.getItems().add(changeDescriptionMenue);
   }

   private void initTable() {
      tableView.setOnMousePressed(this::handleMouseEvent);
      tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
   }

   private EventHandler<ActionEvent> onDescriptionChange() {
      return event -> {
         changeDescriptionMenue.setDisable(true);
         descAddHelper.showInputField(event, contextMenu.getX(), contextMenu.getY() + 20, tableView);
      };
   }

   private void onDescriptionChangeFinish(FinishAction finishAction) {
      switch (finishAction) {
         case FINISH:
            refreshUI();
            break;
         case ABORT:
            changeDescriptionMenue.setDisable(false);
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
