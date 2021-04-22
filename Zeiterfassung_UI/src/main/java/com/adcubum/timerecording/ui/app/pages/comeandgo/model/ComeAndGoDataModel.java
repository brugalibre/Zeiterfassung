package com.adcubum.timerecording.ui.app.pages.comeandgo.model;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;

public class ComeAndGoDataModel {

   private String representation;
   private TimeSnippet timeSnippet;
   private boolean isNotDone;
   private boolean isNotRecorded;

   private ComeAndGoDataModel(ComeAndGo comeAndGo) {
      this.representation = comeAndGo.getRepresentation();
      this.timeSnippet = comeAndGo.getComeAndGoTimeStamp();
      this.isNotDone = comeAndGo.isNotDone();
      this.isNotRecorded = comeAndGo.isNotRecorded();
   }

   public String getRepresentation() {
      return representation;
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

   /**
    * Creates a new {@link ComeAndGoDataModel} for the given {@link ComeAndGo}
    * 
    * @param comeAndGo
    *        the given {@link ComeAndGo}
    * @return a new {@link ComeAndGoDataModel}
    */
   public static ComeAndGoDataModel of(ComeAndGo comeAndGo) {
      return new ComeAndGoDataModel(comeAndGo);
   }
}
