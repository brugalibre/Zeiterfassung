package com.adcubum.timerecording.core.work.businessday.comeandgo.impl;

import static java.util.Objects.isNull;

import java.util.UUID;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.work.date.Time;

/**
 * The class {@link ComeAndGo} keeps track of when a user comes and goes during the business day
 * 
 * @author DStalder
 *
 */
public class ComeAndGoImpl implements ComeAndGo {

   private UUID id;
   private TimeSnippet comeAndGoSnippet;
   private boolean isRecorded;

   private ComeAndGoImpl() {
      this((UUID) null);
   }

   private ComeAndGoImpl(UUID id) {
      this(id, TimeSnippetFactory.createNew(), false);
   }

   private ComeAndGoImpl(UUID id, TimeSnippet timeSnippet, boolean isRecorded) {
      this.comeAndGoSnippet = timeSnippet;
      this.isRecorded = isRecorded;
      this.id = id;
   }

   private ComeAndGoImpl(ComeAndGoImpl comeAndGoImpl) {
      this.comeAndGoSnippet = TimeSnippetFactory.createNew(comeAndGoImpl.comeAndGoSnippet);
      this.isRecorded = comeAndGoImpl.isRecorded;
      this.id = comeAndGoImpl.id;
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
   public String toString() {
      String isRecordedRep = isRecorded ? " " + TextLabel.EXISTS_RECORD_FOR_COME_AND_GO : "";
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

   @Override
   public UUID getId() {
      return id;
   }

   /**
    * Creates a new {@link ComeAndGo} with no entries
    * 
    * @param id
    *        the id
    * @return a new {@link ComeAndGo} with no entries
    */
   public static ComeAndGoImpl of(UUID id) {
      return new ComeAndGoImpl(id);
   }

   /**
    * Creates a new {@link ComeAndGo} with no entries and a random id
    * 
    * @return a new {@link ComeAndGo} with no entries and a random id
    */
   public static ComeAndGoImpl of() {
      return new ComeAndGoImpl();
   }

   /**
    * Creates a new {@link ComeAndGo} with the given id, {@link TimeSnippet} and the boolean which defines, if this {@link ComeAndGo} is
    * already recorded
    * 
    * @param id
    *        the id
    * @param timeSnippet
    *        the {@link TimeSnippet}
    * @param isRecorded
    *        <code>true</code> if this {@link ComeAndGo} is already recorded or <code>false</code> if not
    * @return a new {@link ComeAndGo}
    */
   public static ComeAndGoImpl of(UUID id, TimeSnippet timeSnippet, boolean isRecorded) {
      return new ComeAndGoImpl(id, timeSnippet, isRecorded);
   }
}
