/**
* 
*/
package com.adcubum.timerecording.ui.app.pages.overview.control.descriptionchange;

import static com.adcubum.timerecording.core.work.businessday.ValueTypes.DESCRIPTION;

import java.util.Optional;
import java.util.UUID;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.ui.app.pages.overview.control.callback.BDChangedUiCallbackHandler;
import com.adcubum.timerecording.ui.app.pages.overview.model.table.BusinessDayIncTableRowValue;
import com.adcubum.timerecording.ui.app.pages.overview.view.OverviewPage;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * The {@link DescriptionAddHelper} helps to add a new description to a row on
 * the {@link OverviewPage}
 * 
 * @author Dominic
 * 
 */
public class DescriptionAddHelper {

   private BDChangedUiCallbackHandler callbackHandler;
   private BusinessDayChangedCallbackHandler handler;
   private MenuItem addDescriptionMenuItem;
   private Stage stage;

   public DescriptionAddHelper(BDChangedUiCallbackHandler uiRefresher, ContextMenu contextMenu,
         TableView<BusinessDayIncTableRowValue> tableView) {
      this.callbackHandler = uiRefresher;
      this.handler = BusinessDayChangedCallbackHandlerFactory.createNew();
      this.stage = new Stage(StageStyle.UNDECORATED);
      createDescriptionChangeMenueItem(contextMenu, tableView);
   }

   private void createDescriptionChangeMenueItem(ContextMenu contextMenu, TableView<BusinessDayIncTableRowValue> tableView) {
      this.addDescriptionMenuItem = new MenuItem(TextLabel.CHANGE_DESCRIPTION);
      this.addDescriptionMenuItem.setOnAction(onDescriptionChange(contextMenu, tableView));
   }

   private EventHandler<ActionEvent> onDescriptionChange(ContextMenu contextMenu, TableView<BusinessDayIncTableRowValue> tableView) {
      return event -> {
         setDisable(true);
         showInputField(event, contextMenu.getX(), contextMenu.getY() + 20, tableView);
      };
   }

   /**
    * This {@link DescriptionAddHelper} will close it's {@link Stage}s
    * which was may be opened earlier in order to add a description
    */
   public void onDispose() {
      if (stage.isShowing()) {
         stage.close();
      }
   }

   /*
    * Shows an input field in order to enter the new description
    */
   private void showInputField(ActionEvent event, double x, double y, TableView<BusinessDayIncTableRowValue> tableView) {
      Optional<BusinessDayIncTableRowValue> optionalBusinessDayIncTableRowValue =
            Optional.ofNullable(tableView.getSelectionModel().getSelectedItem());
      optionalBusinessDayIncTableRowValue.ifPresent(businessDayIncTableRowValue -> showInputField(businessDayIncTableRowValue, x, y));
      event.consume();
   }

   private void showInputField(BusinessDayIncTableRowValue businessDayIncTableRowValue, double x, double y) {

      TextField field = new TextField();
      EventHandler<? super KeyEvent> keyEventHandler =
            keyEvent -> handleKeyPressed(keyEvent, field, stage, businessDayIncTableRowValue.getId());
      initContent(stage, field, keyEventHandler, x, y);
      stage.show();
   }

   private void initContent(Stage stage, TextField field, EventHandler<? super KeyEvent> keyEventHandler, double x, double y) {
      Pane borderPane = new Pane();
      borderPane.getChildren().add(field);
      Scene scene = new Scene(borderPane);
      scene.setOnKeyPressed(keyEventHandler);

      stage.setScene(scene);
      stage.setAlwaysOnTop(true);
      stage.setX(x);
      stage.setY(y);
   }

   private void handleKeyPressed(KeyEvent keyEvent, TextField textField, Stage stage, UUID id) {
      if (keyEvent.getCode() == KeyCode.ESCAPE) {
         stage.close();
         closeStageAndRefreshUI(stage, FinishAction.ABORT);
      } else if (keyEvent.getCode() == KeyCode.ENTER) {
         handler.handleBusinessDayChanged(ChangedValue.of(id, textField.getText(), DESCRIPTION));
         closeStageAndRefreshUI(stage, FinishAction.FINISH);
      }
      keyEvent.consume();
   }

   private void closeStageAndRefreshUI(Stage stage, FinishAction finishAction) {
      stage.close();
      callbackHandler.onFinish(finishAction);
   }

   /**
    * Disables or enables the menu-item to add a description
    * 
    * @param isDisabled
    *        <code>true</code> if the menu-item is going to be disabled or <code>false</code> if not
    */
   public void setDisable(boolean isDisabled) {
      addDescriptionMenuItem.setDisable(isDisabled);
   }

   /**
    * @return the menu-time to start a TextField for entering a new description
    */
   public MenuItem getChangeDescriptionMenuItem() {
      return addDescriptionMenuItem;
   }
}
