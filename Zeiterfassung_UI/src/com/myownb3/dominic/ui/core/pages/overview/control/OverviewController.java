/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.core.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.core.pages.overview.model.resolver.OverviewPageModelResolver;
import com.myownb3.dominic.ui.core.pages.overview.model.table.BusinessDayIncTableCellValue;
import com.myownb3.dominic.ui.core.pages.overview.model.table.BusinessDayTableModelHelper;
import com.myownb3.dominic.ui.core.pages.overview.view.OverviewPage;
import com.myownb3.dominic.ui.core.view.Page;

import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * @author Dominic
 * 
 */
public class OverviewController extends BaseFXController<OverviewPageModel, OverviewPageModel>
	implements BusinessDayChangedCallbackHandler {

    /**
     * There are five fix headers: Number, Amount of Hours, Ticket, charge-Type &
     * is-charged
     */
    public static final int AMOUNT_OF_FIX_HEADERS = 5;

    private MainWindowController mainWindowController;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TableView<BusinessDayIncTableCellValue> tableView;

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

    private BusinessDayTableModelHelper businessDayTableModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
	initialize(new OverviewPage(this, url));
    }

    @Override
    public void initialize(Page<OverviewPageModel, OverviewPageModel> page) {
	super.initialize(page);
	ListChangeListener<? super BusinessDayIncTableCellValue> listener = businessDayCell -> BusinessDayIncTableCellValueChanged(
		businessDayCell);
	businessDayTableModel = new BusinessDayTableModelHelper(listener);
	setBinding(dataModel);
    }

    @Override
    public void show() {
	super.show();
	BusinessDay4Export businessDay4Export = BusinessDay4Export.of(TimeRecorder.getBussinessDay());
	businessDayTableModel.init(businessDay4Export, tableView);
    }

    public void init(MainWindowController mainWindowController) {
	this.mainWindowController = mainWindowController;
    }

    @Override
    public void handleBusinessDayChanged(ChangedValue changeValue) {
	BusinessDayChangedCallbackHandlerImpl businessDayChangedCallbackHandler = new BusinessDayChangedCallbackHandlerImpl(
		TimeRecorder.getBussinessDay());
	businessDayChangedCallbackHandler.handleBusinessDayChanged(changeValue);
	mainWindowController.show();
    }

    @FXML
    private void onAction(ActionEvent actionEvent) {

	if (actionEvent.getSource() == clearButton) {
	    mainWindowController.clearBusinessDayContents();
	    mainWindowController.dispose();
	} else if (actionEvent.getSource() == bookButton) {
	    mainWindowController.bookOff();
	} else if (actionEvent.getSource() == exportButton) {
	    mainWindowController.export();
	}
    }

    private void BusinessDayIncTableCellValueChanged(Change<? extends BusinessDayIncTableCellValue> businessDayCell) {

	// if (e.getType() == TableModelEvent.UPDATE) {
	// BusinessDayTableModel businessDayTableModel = (BusinessDayTableModel)
	// e.getSource();
	// TableCellValue tableCellValue =
	// businessDayTableModel.getCellAt(e.getFirstRow(), e.getColumn());
	// TableCellValue noTableCellValue =
	// businessDayTableModel.getCellAt(e.getFirstRow(), 0);
	// handler.handleBusinessDayChanged(ChangedValue.of(Integer.valueOf(noTableCellValue.getValue()),
	// tableCellValue.getValue(), tableCellValue.getValueType(),
	// getIndexForFromUpto(tableCellValue)));
	// }
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
}