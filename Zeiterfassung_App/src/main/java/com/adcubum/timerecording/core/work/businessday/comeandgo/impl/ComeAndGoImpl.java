package com.adcubum.timerecording.core.work.businessday.comeandgo.impl;

import static java.util.Objects.isNull;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.settings.round.TimeRounder;
import com.adcubum.timerecording.work.date.Time;

/**
 * The class {@link ComeAndGoDto} keeps track of when a user comes and goes during the business day
 * 
 * @author DStalder
 *
 */
public class ComeAndGoImpl implements ComeAndGo {

   private TimeSnippet comeAndGoSnippet;
   private boolean isRecorded;

   private ComeAndGoImpl() {
      Time time = new Time(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
      this.comeAndGoSnippet = new TimeSnippet(time);
      this.isRecorded = false;
   }

   private ComeAndGoImpl(ComeAndGoImpl comeAndGoImpl) {
      this.comeAndGoSnippet = new TimeSnippet(comeAndGoImpl.comeAndGoSnippet);
      this.isRecorded = comeAndGoImpl.isRecorded;
   }

   @Override
   public ComeAndGo comeOrGo(Time time) {
      ComeAndGoImpl comeAndGoImpl = new ComeAndGoImpl(this);
      if (isNull(comeAndGoImpl.comeAndGoSnippet.getBeginTimeStamp())) {
         comeAndGoImpl.comeAndGoSnippet.setBeginTimeStamp(time);
      } else if (isNull(comeAndGoSnippet.getEndTimeStamp())) {
         comeAndGoImpl.comeAndGoSnippet.setEndTimeStamp(time);
      } else {
         throw new IllegalStateException("Come and go are already defined. Calling comeOrGo() is therefore not applicable!");
      }
      return comeAndGoImpl;
   }

   @Override
   public ComeAndGo flagAsRecorded() {
      ComeAndGoImpl comeAndGoImpl = new ComeAndGoImpl(this);
      comeAndGoImpl.isRecorded = true;
      return comeAndGoImpl;
   }

   @Override
   public ComeAndGo resume() {
      ComeAndGoImpl comeAndGoImpl = new ComeAndGoImpl(this);
      comeAndGoImpl.comeAndGoSnippet.setEndTimeStamp(null);
      return comeAndGoImpl;
   }

   @Override
   public TimeSnippet getComeAndGoTimeStamp() {
      return comeAndGoSnippet;
   }

   @Override
   public String getRepresentation() {
      String isRecordedRep = isRecorded ? " " + TextLabel.COME_OR_GO_RECORDED_TEXT : "";
      return comeAndGoSnippet.getBeginTimeStampRep() + " / " + getEndTimeStampRep() + isRecordedRep;
   }

   private String getEndTimeStampRep() {
      return isNull(comeAndGoSnippet.getEndTimeStamp()) ? TimeSnippet.NULL_TIME_REP : comeAndGoSnippet.getEndTimeStampRep();
   }

   @Override
   public boolean isNotDone() {
      return isNull(comeAndGoSnippet.getBeginTimeStamp())
            || isNull(comeAndGoSnippet.getEndTimeStamp());
   }

   @Override
   public boolean isNotRecorded() {
      return !isRecorded;
   }

   /**
    * Creates a new {@link ComeAndGoDto} with no entries
    * 
    * @return a new {@link ComeAndGoDto} with no entries
    */
   public static ComeAndGoImpl of() {
      return new ComeAndGoImpl();
   }
}
