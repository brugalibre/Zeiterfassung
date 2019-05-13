/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.core.control.impl.BaseFXController;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.pages.mainpage.control.MainWindowController;
import com.myownb3.dominic.ui.core.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.core.pages.overview.model.resolver.OverviewPageModelResolver;
import com.myownb3.dominic.ui.core.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.myownb3.dominic.ui.core.pages.overview.model.table.BusinessDayTableModelHelper;
import com.myownb3.dominic.ui.core.pages.overview.model.table.TimeSnippetCellValue;
import com.myownb3.dominic.ui.core.pages.overview.view.OverviewPage;
import com.myownb3.dominic.ui.core.styles.Styles;
import com.myownb3.dominic.ui.core.view.Page;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
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

    private BusinessDayChangedCallbackHandlerImpl handler;
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

    /**
     * @param businessDayIncTableCellValue
     * @param tablePosition
     * @return
     */
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
	    TimeRecorder.book();
	    show();
	} else if (actionEvent.getSource() == exportButton) {
	    TimeRecorder.export();
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
}