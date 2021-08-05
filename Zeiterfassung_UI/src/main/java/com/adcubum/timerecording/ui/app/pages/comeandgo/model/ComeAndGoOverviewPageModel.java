/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.comeandgo.model;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.impl.ComeAndGoesUpdaterImpl;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.table.ComeAndGoTableRowValue;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;

/**
 * @author Dominic
 *
 */
public class ComeAndGoOverviewPageModel implements PageModel, EventHandler<CellEditEvent<ComeAndGoTableRowValue, String>> {

   private static final String MNEMONIC_PREFIX = "_";
   private BusinessDayChangedCallbackHandler businessDayChangedCallbackHandler;
   private StringProperty startAddBDIncrementButtonProperty;
   private SimpleStringProperty clearAllComeAndGoesButtonProperty;
   private List<ComeAndGoDataModel> comeAndGoesDataModels;
   private boolean isStartAddBDIncrementButtonDisabled;
   private Time prevComeAndGoEnd;
   private int currentComeAndGoIndex;
   private boolean isClearAllComeAndGoesButtonDisabled;
   private SimpleListProperty<ComeAndGoTableRowValue> comeAndGoTableRowValuesProperty;

   private ComeAndGoOverviewPageModel(ComeAndGoes comeAndGoes) {
      this.startAddBDIncrementButtonProperty = new SimpleStringProperty(TextLabel.START_CREATING_BD_INC);
      this.clearAllComeAndGoesButtonProperty = new SimpleStringProperty(TextLabel.CLEAR_LABEL);
      this.comeAndGoesDataModels = getComeAndGoesDataModels(comeAndGoes);
      this.isStartAddBDIncrementButtonDisabled = evalIsStartAddBDIncrementButtonDisabled(comeAndGoesDataModels);
      this.isClearAllComeAndGoesButtonDisabled = evalIsClearAllComeAndGoesButtonDisabled(comeAndGoesDataModels);
      this.currentComeAndGoIndex = evalCurrentComeAndGoIndex(comeAndGoesDataModels);
      this.businessDayChangedCallbackHandler = new ComeAndGoBDChangedCallbackHandler();
      this.prevComeAndGoEnd = TimeFactory.createNew(0);
      this.comeAndGoTableRowValuesProperty = new SimpleListProperty<>(map2ComeAndGoTableRowValues(comeAndGoesDataModels));
   }

   public static ComeAndGoOverviewPageModel of(ComeAndGoes comeAndGoes) {
      return new ComeAndGoOverviewPageModel(comeAndGoes);
   }

   public static ComeAndGoOverviewPageModel of(ComeAndGoOverviewPageModel inPageModel, ComeAndGoes comeAndGoes) {
      inPageModel.startAddBDIncrementButtonProperty.setValue(MNEMONIC_PREFIX + TextLabel.START_CREATING_BD_INC);
      inPageModel.clearAllComeAndGoesButtonProperty = new SimpleStringProperty(TextLabel.CLEAR_LABEL);
      inPageModel.comeAndGoesDataModels = getComeAndGoesDataModels(comeAndGoes);
      List<ComeAndGoDataModel> comeAndGoesDataModels = inPageModel.comeAndGoesDataModels;
      inPageModel.isStartAddBDIncrementButtonDisabled = evalIsStartAddBDIncrementButtonDisabled(comeAndGoesDataModels);
      inPageModel.isClearAllComeAndGoesButtonDisabled = evalIsClearAllComeAndGoesButtonDisabled(comeAndGoesDataModels);
      inPageModel.currentComeAndGoIndex = evalCurrentComeAndGoIndex(comeAndGoesDataModels);
      inPageModel.prevComeAndGoEnd = TimeFactory.createNew(0);
      inPageModel.comeAndGoTableRowValuesProperty.setValue(map2ComeAndGoTableRowValues(comeAndGoesDataModels));
      return inPageModel;
   }

   public void addAllBusinessDayIncrements2BusinessDay() {
      ((ComeAndGoBDChangedCallbackHandler) businessDayChangedCallbackHandler).addAllBusinessDayIncrements();
   }

   /**
    * Prepares this {@link ComeAndGoOverviewPageModel} for the next iteration of creating a {@link BusinessDayIncrement}
    * This means it compares the end {@link TimeSnippet} of the {@link ComeAndGoDataModel} for which we are currently creating a
    * {@link BusinessDayIncrement} with the end {@link TimeSnippet} of last created BusinessDayIncrement. If those are equal, the current
    * {@link ComeAndGoDataModel} is considered as 'done' and we increment a index indicating the current {@link ComeAndGoDataModel}
    * In either case we remember the last entered end {@link TimeSnippet}
    * 
    * @param currentIterationTimeSnippet
    */
   public void prepareForNextIteration(TimeSnippet currentIterationTimeSnippet) {
      Time currentBDIncrementEnd = currentIterationTimeSnippet.getEndTimeStamp();
      ComeAndGoDataModel comeAndGo = getCurrentComeAndGo();
      if (comeAndGo.getTimeSnippet().getEndTimeStamp().equals(currentBDIncrementEnd)) {
         currentComeAndGoIndex++;
      }
      this.prevComeAndGoEnd = currentBDIncrementEnd;
   }

   /**
    * Returns <code>true</code> if there is at least one {@link ComeAndGoDataModel} which is not 'done' yet. Means there is a
    * {@link ComeAndGoDataModel} which is not entirely covered by {@link BusinessDayIncrement}s. Otherwise this method returns
    * <code>false</code>
    * 
    * @return <code>true</code> if there is at least one {@link ComeAndGoDataModel} which is not 'done' yet. Otherwise returns
    *         <code>false</code>
    */
   public boolean hasUnfinishedComeAndGoes() {
      if (nonNull(getCurrentBDIncTimeSnippet())) {
         Time currentBDIncEndTimeStamp = getCurrentBDIncTimeSnippet().getEndTimeStamp();
         TimeSnippet lastComeAndGoTimeSnippet = getLastCurrentComeAndGo().getTimeSnippet();
         return !currentBDIncEndTimeStamp.equals(lastComeAndGoTimeSnippet.getEndTimeStamp());
      }
      return currentComeAndGoIndex != comeAndGoesDataModels.size();
   }

   /**
    * @return the last {@link ComeAndGoDataModel}
    */
   public ComeAndGoDataModel getLastCurrentComeAndGo() {
      return comeAndGoesDataModels.get(comeAndGoesDataModels.size() - 1);
   }

   private static boolean evalIsStartAddBDIncrementButtonDisabled(List<ComeAndGoDataModel> comeAndGoesDataModels) {
      List<ComeAndGoDataModel> doneAndNotRecordedEntries = evalNotRecordedComeAndGoes(comeAndGoesDataModels);
      return doneAndNotRecordedEntries.isEmpty()
            || getLastComeAndGo(doneAndNotRecordedEntries).isNotDone();
   }

   /*
    * We can't press 'delete all' if there is only one element which is not done yet
    * As soon as there are more then one elements (and only one is not done) we can delete all the done ones
    */
   private static boolean evalIsClearAllComeAndGoesButtonDisabled(List<ComeAndGoDataModel> comeAndGoesDataModels) {
      List<ComeAndGoDataModel> notDoneComeAndGoes = evalNotDoneComeAndGoes(comeAndGoesDataModels);
      return !notDoneComeAndGoes.isEmpty()
            && comeAndGoesDataModels.size() == 1;
   }

   private static List<ComeAndGoDataModel> evalNotDoneComeAndGoes(List<ComeAndGoDataModel> comeAndGoesDataModels) {
      return comeAndGoesDataModels.stream()
            .filter(ComeAndGoDataModel::isNotDone)
            .collect(Collectors.toList());
   }

   private static List<ComeAndGoDataModel> evalNotRecordedComeAndGoes(List<ComeAndGoDataModel> comeAndGoesDataModels) {
      return comeAndGoesDataModels.stream()
            .filter(ComeAndGoDataModel::isNotRecorded)
            .collect(Collectors.toList());
   }

   public boolean isStartAddBDIncrementButtonDisabled() {
      return isStartAddBDIncrementButtonDisabled;
   }

   public boolean isClearAllComeAndGoesButtonDisabled() {
      return isClearAllComeAndGoesButtonDisabled;
   }

   public StringProperty getStartAddBDIncrementButtonProperty() {
      return startAddBDIncrementButtonProperty;
   }

   public SimpleStringProperty getClearAllComeAndGoesButtonProperty() {
      return clearAllComeAndGoesButtonProperty;
   }

   public List<ComeAndGoDataModel> getComeAndGoesDataModels() {
      return comeAndGoesDataModels;
   }

   public Time getPrevComeAndGoEnd() {
      return prevComeAndGoEnd;
   }

   /**
    * Verifies if the current {@link ComeAndGo} is the last one
    * 
    * @return <code>true</code> if the current {@link ComeAndGo} is the last one or <code>false</code> if there is at least one more element
    *         to proceed
    */
   public boolean isLastIncrementAmongOthers() {
      return comeAndGoesDataModels.size() == currentComeAndGoIndex;
   }

   /**
    * @return the {@link TimeSnippet} from the current {@link ComeAndGo}
    */
   public TimeSnippet getComeAndGoTimeSnippet() {
      ComeAndGoDataModel comeAndGoDataModel = getCurrentComeAndGo();
      return comeAndGoDataModel.getTimeSnippet();
   }

   /**
    * @return the {@link TimeSnippet} from the last {@link BusinessDayIncrementAdd} we added
    */
   public TimeSnippet getCurrentBDIncTimeSnippet() {
      return ((ComeAndGoBDChangedCallbackHandler) businessDayChangedCallbackHandler).getCurrentTimeSnippet();
   }

   /**
    * @return the ticket nr which was entered for the previous {@link ComeAndGo}
    */
   public String getTicketNrFromPrevAddedBDInc() {
      return ((ComeAndGoBDChangedCallbackHandler) businessDayChangedCallbackHandler).getTicketNrFromPrevAddedBDInc();
   }

   public BusinessDayChangedCallbackHandler getBusinessDayChangedCallbackHandler() {
      return businessDayChangedCallbackHandler;
   }

   private static int evalCurrentComeAndGoIndex(List<ComeAndGoDataModel> comeAndGoesDataModels) {
      int currentComeAndGoIndex = 1;
      for (int i = 0; i < comeAndGoesDataModels.size(); i++) {
         if (comeAndGoesDataModels.get(i).isNotRecorded()) {
            currentComeAndGoIndex = i + 1;
            break;
         }
      }
      return currentComeAndGoIndex;
   }

   /*
    * Returns the current, finished and not yet recorded, ComeAndGo-Model
    */
   private ComeAndGoDataModel getCurrentComeAndGo() {
      return comeAndGoesDataModels.get(currentComeAndGoIndex - 1);
   }

   private static List<ComeAndGoDataModel> getComeAndGoesDataModels(ComeAndGoes comeAndGoes) {
      return isNull(comeAndGoes) ? Collections.emptyList()
            : comeAndGoes.getComeAndGoEntries()
                  .stream()
                  .map(ComeAndGoOverviewPageModel::createComeAndGoDataModel)
                  .collect(Collectors.toList());
   }

   private static ComeAndGoDataModel createComeAndGoDataModel(ComeAndGo comeAndGo) {
      return ComeAndGoDataModel.of(comeAndGo, new ComeAndGoesUpdaterImpl());
   }

   private static ComeAndGoDataModel getLastComeAndGo(List<ComeAndGoDataModel> comeAndGoesDataModels) {
      return comeAndGoesDataModels.get(comeAndGoesDataModels.size() - 1);
   }

   @Override
   public void handle(CellEditEvent<ComeAndGoTableRowValue, String> event) {
      ComeAndGoTableRowValue businessDayIncTableCellValue = event.getRowValue();
      businessDayIncTableCellValue.handle(event);
   }

   public ListProperty<ComeAndGoTableRowValue> getComeAndGoTableRowValuesProperty() {
      return comeAndGoTableRowValuesProperty;
   }

   private static ObservableList<ComeAndGoTableRowValue> map2ComeAndGoTableRowValues(List<ComeAndGoDataModel> comeAndGoDataModels) {
      return FXCollections.observableList(comeAndGoDataModels
            .stream()
            .map(ComeAndGoTableRowValue::of)
            .collect(Collectors.toList()));
   }
}
