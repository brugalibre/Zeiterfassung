/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.work.businessday.extern.BusinessDay4Export;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.app.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.app.pages.overview.control.businessdaychange.BusinessDayChangeHelper;
import com.myownb3.dominic.ui.app.pages.overview.control.descriptionchange.DescriptionAddHelper;
import com.myownb3.dominic.ui.app.pages.overview.control.rowdeleter.RowDeleteHelper;
import com.myownb3.dominic.ui.app.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.app.pages.overview.model.resolver.OverviewPageModelResolver;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayTableModelHelper;
import com.myownb3.dominic.ui.app.pages.overview.view.OverviewPage;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.view.Page;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * @author Dominic
 * 
 */
public class OverviewController extends BaseFXController<OverviewPageModel, OverviewPageModel> {

    private MainWindowController mainWindowController;

    @FXML
    private BorderPane borderPane;

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

    private RowDeleteHelper rowDeleteHelper;
    private DescriptionAddHelper descAddHelper;
    private BusinessDayTableModelHelper businessDayTableModel;
    private TimeRecordingTray timeRecordingTray;

    private MenuItem changeDescriptionMenue;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
	initialize(new OverviewPage(this, url));
    }

    @Override
    public void initialize(Page<OverviewPageModel, OverviewPageModel> page) {
	super.initialize(page);
	businessDayTableModel = new BusinessDayTableModelHelper(new BusinessDayChangeHelper(() -> show()));
	setBinding(dataModel);

	initContextMenu();
	initTable();
    }

    @Override
    public void show() {
	super.show();
	BusinessDay4Export businessDay4Export = getDataModel().getBusinessDay4Export();
	businessDayTableModel.init(businessDay4Export, tableView);
	addOrRemoveDescriptionChangeMenue();
    }

    public void init(MainWindowController mainWindowController) {
	this.mainWindowController = mainWindowController;
    }

    private void handleMouseEvent(MouseEvent event) {
	if (hasRightClickOnTable(event) && !tableView.getSelectionModel().isEmpty()) {
	    BusinessDayIncTableRowValue businessDayIncTableRowValue = (BusinessDayIncTableRowValue) tableView
		    .getSelectionModel().getSelectedItem();
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
	    timeRecordingTray.book();
	    show();
	} else if (actionEvent.getSource() == exportButton) {
	    timeRecordingTray.export();
	}
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
    }

    private void initContextMenu() {

	rowDeleteHelper = new RowDeleteHelper(() -> refreshUI());
	descAddHelper = new DescriptionAddHelper(() -> refreshUI());
	MenuItem deleteMenue = new MenuItem(TextLabel.DELETE_ROW);
	deleteMenue.setOnAction(event -> rowDeleteHelper.deleteRow(event, tableView));
	changeDescriptionMenue = new MenuItem(TextLabel.CHANGE_DESCRIPTION);
	changeDescriptionMenue.setOnAction(event -> descAddHelper.showInputField(event, contextMenu.getX(), contextMenu.getY() + 20, tableView));
	contextMenu = new ContextMenu();
	contextMenu.getItems().add(deleteMenue);
    }

    private void addOrRemoveDescriptionChangeMenue() {
	if (TimeRecorder.INSTANCE.getBussinessDay().hasDescription()) {
	    contextMenu.getItems().remove(changeDescriptionMenue);
	} else if (!contextMenu.getItems().contains(changeDescriptionMenue)) {
	    contextMenu.getItems().add(changeDescriptionMenue);
	}
    }

    private void initTable() {
	tableView.setOnMousePressed(event -> handleMouseEvent(event));
	tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setTimeRecordingTray(TimeRecordingTray timeRecordingTray) {
	this.timeRecordingTray = timeRecordingTray;
    }

    private void refreshUI() {
	show();
	timeRecordingTray.updateUIStates();
    }
}