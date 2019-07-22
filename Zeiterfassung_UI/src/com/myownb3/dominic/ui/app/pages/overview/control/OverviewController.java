/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.control;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.app.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.app.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.app.pages.overview.model.resolver.OverviewPageModelResolver;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.myownb3.dominic.ui.app.pages.overview.model.table.BusinessDayTableModelHelper;
import com.myownb3.dominic.ui.app.pages.overview.model.table.TimeSnippetCellValue;
import com.myownb3.dominic.ui.app.pages.overview.view.OverviewPage;
import com.myownb3.dominic.ui.app.styles.Styles;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.view.Page;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * @author Dominic
 * 
 */
public class OverviewController extends BaseFXController<OverviewPageModel, OverviewPageModel>
	implements EventHandler<CellEditEvent<BusinessDayIncTableRowValue, String>> {

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

    private BusinessDayChangedCallbackHandler handler;
    private BusinessDayTableModelHelper businessDayTableModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
	initialize(new OverviewPage(this, url));
    }

    @Override
    public void initialize(Page<OverviewPageModel, OverviewPageModel> page) {
	super.initialize(page);
	businessDayTableModel = new BusinessDayTableModelHelper(this);
	handler = new BusinessDayChangedCallbackHandlerImpl();
	setBinding(dataModel);

	totalAmountOfTimeLabel.getStyleClass().add(Styles.BOLD_LABEL_12);
	totalAmountOfTimeValue.getStyleClass().add(Styles.BOLD_LABEL_12);

	initContextMenu();
	initTable();
    }

    private void deleteRow(ActionEvent event) {

	Optional<BusinessDayIncTableRowValue> optionalBusinessDayIncTableRowValue = Optional
		.ofNullable(tableView.getSelectionModel().getSelectedItem());
	optionalBusinessDayIncTableRowValue.ifPresent(businessDayIncTableRowValue -> {
	    handler.handleBusinessDayIncrementDeleted(businessDayIncTableRowValue.getNumberAsInt() - 1);
	    afterDelete(event);
	});
    }

    private void afterDelete(ActionEvent event) {
	event.consume();
	show();
    }

    @Override
    public void show() {
	super.show();
	BusinessDay4Export businessDay4Export = getDataModel().getBusinessDay4Export();
	businessDayTableModel.init(businessDay4Export, tableView);
    }

    public void init(MainWindowController mainWindowController) {
	this.mainWindowController = mainWindowController;
    }

    private void handleMouseEvent(MouseEvent event) {
	if (hasRightClickOnTable(event)) {
	    TableView<?> tableView = (TableView<?>) event.getSource();
	    BusinessDayIncTableRowValue businessDayIncTableRowValue = (BusinessDayIncTableRowValue) tableView
		    .getSelectionModel().getSelectedItem();
	    setFocusToRow(tableView, businessDayIncTableRowValue.getNumberAsInt() - 1);
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

    @Override
    public void handle(CellEditEvent<BusinessDayIncTableRowValue, String> event) {

	BusinessDayIncTableRowValue businessDayIncTableCellValue = event.getRowValue();
	TablePosition<BusinessDayIncTableRowValue, String> tablePosition = event.getTablePosition();
	String newValue = event.getNewValue();

	ValueTypes valueType = businessDayIncTableCellValue.getChangeValueTypeForIndex(tablePosition.getColumn());
	// XXX Ugly hack. Since we had to make present values editable
	if (valueType == ValueTypes.NONE) {
	    show();
	    return;
	}
	int fromUptoSequence = getBeginEndSequence(businessDayIncTableCellValue, tablePosition);
	int orderNumber = Integer.valueOf(businessDayIncTableCellValue.getNumber());
	handler.handleBusinessDayChanged(ChangedValue.of(orderNumber, newValue, valueType, fromUptoSequence));

	show();
    }

    private int getBeginEndSequence(BusinessDayIncTableRowValue businessDayIncTableCellValue,
	    TablePosition<BusinessDayIncTableRowValue, String> tablePosition) {
	TimeSnippetCellValue timeSnippet4Index = businessDayIncTableCellValue
		.getTimeSnippe4RowIndex(tablePosition.getColumn());
	return timeSnippet4Index != null ? timeSnippet4Index.getSequence() : -1;
    }

    @FXML
    private void onAction(ActionEvent actionEvent) {

	if (actionEvent.getSource() == clearButton) {
	    mainWindowController.clearBusinessDayContents();
	    mainWindowController.dispose();
	} else if (actionEvent.getSource() == bookButton) {
	    TimeRecorder.INSTANCE.book();
	    show();
	} else if (actionEvent.getSource() == exportButton) {
	    TimeRecorder.INSTANCE.export();
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
	MenuItem deleteMenue = new MenuItem(TextLabel.DELETE_ROW);
	deleteMenue.setOnAction(event -> deleteRow(event));
	contextMenu = new ContextMenu();
	contextMenu.getItems().add(deleteMenue);
    }

    private void initTable() {
	tableView.setOnMousePressed(event -> handleMouseEvent(event));
	tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}