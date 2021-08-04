package com.adcubum.timerecording.ui.app.pages.comeandgo.model;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ComeAndGoesUpdater;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.impl.ChangedComeAndGoValueImpl;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.util.parser.DateParser;

public class ComeAndGoDataModel {

   private ComeAndGoesUpdater comeAndGoesUpdater;
   private UUID id;
   private TimeSnippet timeSnippet;
   private boolean isNotDone;
   private boolean isNotRecorded;

   private ComeAndGoDataModel(ComeAndGo comeAndGo, ComeAndGoesUpdater comeAndGoesUpdater) {
      this.timeSnippet = comeAndGo.getComeAndGoTimeStamp();
      this.isNotDone = comeAndGo.isNotDone();
      this.isNotRecorded = comeAndGo.isNotRecorded();
      this.id = comeAndGo.getId();
      this.comeAndGoesUpdater = comeAndGoesUpdater;
   }

   public void changeComeOrGo(boolean hasComeChanged, String newValue) {
      if (hasComeChanged) {
         Time newTimeValue = DateParser.getTime(newValue, timeSnippet.getBeginTimeStamp());
         changeComeAndGo(newTimeValue, timeSnippet.getEndTimeStamp());
      } else {
         Time newTimeValue = DateParser.getTime(newValue, timeSnippet.getEndTimeStamp());
         changeComeAndGo(timeSnippet.getBeginTimeStamp(), newTimeValue);
      }
   }

   /*
    * Update the ComeAndGo and our TimeSnippet 
    */
   private void changeComeAndGo(Time newComeValue, Time newGoValue) {
      ChangedComeAndGoValue changedComeAndGoValue = ChangedComeAndGoValueImpl.of(id, newComeValue, newGoValue);
      ComeAndGoes changedComeAndGoes = comeAndGoesUpdater.changeComeAndGo(changedComeAndGoValue);
      updateTimeStamp(changedComeAndGoes);
   }

   /*
    * Update our TimeSnippet according to the one we just changed
    */
   private void updateTimeStamp(ComeAndGoes changedComeAndGoes) {
      changedComeAndGoes.getComeAndGo4Id(id)
            .map(ComeAndGo::getComeAndGoTimeStamp)
            .ifPresent(this::setTimeSnippet);
   }

   public TimeSnippet getTimeSnippet() {
      return timeSnippet;
   }

   public boolean isNotDone() {
      return isNotDone;
   }

   public boolean isNotRecorded() {
      return isNotRecorded;
   }

   private void setTimeSnippet(TimeSnippet changedTimeStamp) {
      this.timeSnippet = changedTimeStamp;
   }

   /**
    * Creates a new {@link ComeAndGoDataModel} for the given {@link ComeAndGo}
    * 
    * @param comeAndGo
    *        the given {@link ComeAndGo}
    * @param comeAndGoesUpdater
    *        the {@link ComeAndGoesUpdater}
    * @return a new {@link ComeAndGoDataModel}
    */
   public static ComeAndGoDataModel of(ComeAndGo comeAndGo, ComeAndGoesUpdater comeAndGoesUpdater) {
      return new ComeAndGoDataModel(comeAndGo, comeAndGoesUpdater);
   }

}
