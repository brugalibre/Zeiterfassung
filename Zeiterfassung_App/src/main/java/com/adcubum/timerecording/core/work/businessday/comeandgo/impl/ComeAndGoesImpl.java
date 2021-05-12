package com.adcubum.timerecording.core.work.businessday.comeandgo.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.settings.round.TimeRounder;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.util.utils.StringUtil;

/**
 * The class {@link ComeAndGoesImpl} keeps track of each time a user comes and goes during the business day.
 * Such a coming or going is represented by a single {@link ComeAndGo} instance
 * 
 * @author DStalder
 *
 */
public class ComeAndGoesImpl implements ComeAndGoes {

   private LinkedList<ComeAndGo> comeAndGoEntries;

   private ComeAndGoesImpl() {
      this.comeAndGoEntries = new LinkedList<>();
   }

   private ComeAndGoesImpl(List<ComeAndGo> comeAndGoEntries) {
      this.comeAndGoEntries = new LinkedList<>(comeAndGoEntries);
   }

   /**
    * Either triggers a manually come or a go for this {@link ComeAndGoDto}
    */
   @Override
   public ComeAndGoes comeOrGo() {
      Time newTime = new Time(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
      return comeOrGo(newTime);
   }

   @Override
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      return evalComeAndGo2Change(changedComeAndGoValue)
            .map(comeAndGo2Change -> changeComeAndGoAndReturnNewComeAndGoes(changedComeAndGoValue, comeAndGo2Change))
            .orElse(this);
   }

   private ComeAndGoes changeComeAndGoAndReturnNewComeAndGoes(ChangedComeAndGoValue changedComeAndGoValue, ComeAndGo comeAndGo2Change) {
      LinkedList<ComeAndGo> comeAndGoEntriesCopy = new LinkedList<>(comeAndGoEntries);
      ComeAndGo changedComeAndGo = ComeAndGoImpl.of(comeAndGo2Change.getId())
            .comeOrGo(changedComeAndGoValue.getNewComeValue())
            .comeOrGo(changedComeAndGoValue.getNewGoValue());
      comeAndGoEntriesCopy.remove(comeAndGo2Change);
      comeAndGoEntriesCopy.add(changedComeAndGo);
      return new ComeAndGoesImpl(comeAndGoEntriesCopy);
   }

   private Optional<ComeAndGo> evalComeAndGo2Change(ChangedComeAndGoValue changedComeAndGoValue) {
      Optional<ComeAndGo> comeAndGo2Change = Optional.empty();
      for (ComeAndGo comeAndGo : comeAndGoEntries) {
         if (isChangedComeAndGo(comeAndGo, changedComeAndGoValue)) {
            comeAndGo2Change = Optional.of(comeAndGo);
            break;
         }
      }
      return comeAndGo2Change;
   }

   private static boolean isChangedComeAndGo(ComeAndGo comeAndGo, ChangedComeAndGoValue changedComeAndGoValue) {
      return StringUtil.isEqual(comeAndGo.getId(), changedComeAndGoValue.getId());
   }

   /**
    * Either triggers a manually come or a go for this {@link ComeAndGoDto}
    */
   @Override
   public ComeAndGoes comeOrGo(Time time) {
      List<ComeAndGo> changedComeAndGoEntries = comeOrGoInternal(time, new LinkedList<>(comeAndGoEntries));
      return new ComeAndGoesImpl(changedComeAndGoEntries);
   }

   private LinkedList<ComeAndGo> comeOrGoInternal(Time time, LinkedList<ComeAndGo> comeAndGoeEntriesCopy) {
      Optional<ComeAndGo> currentComeAndGoOpt = getCurrentComeAndGo(comeAndGoeEntriesCopy);
      ComeAndGo changedComeAndGo;
      if (currentComeAndGoOpt.isPresent()) {
         ComeAndGo comeAndGo = currentComeAndGoOpt.get();
         comeAndGoeEntriesCopy.remove(comeAndGo);// remove the current unfinished one and add the finished one later again
         changedComeAndGo = comeAndGo.comeOrGo(time);
      } else {
         changedComeAndGo = handleNoCurrentComeOrGo(time, comeAndGoeEntriesCopy);
      }
      comeAndGoeEntriesCopy.add(changedComeAndGo);
      return comeAndGoeEntriesCopy;
   }

   private static ComeAndGo handleNoCurrentComeOrGo(Time time, LinkedList<ComeAndGo> comeAndGoEntriesCopy) {
      if (comeAndGoEntriesCopy.isEmpty()) {
         return buildNewComeAndGo(time, "0");
      }
      return handleDoneComeAndGo(time, comeAndGoEntriesCopy);
   }

   /*
    * Handles a done ComeAndGo.
    * 
   
    * 
    * 1) So either we reopen this existing and already done ComeAndGo
    * if the new begin is exactly the same then the last end
    * Existing ComeAndGo  |----|
    * New ComeAndGo            |----|
    * After:              |---------|
    * 
    * 2) Or we create a new one
    * Existing ComeAndGo  |----|
    * New ComeAndGo               |----|
    * After:              |----|  |----|
    * 
    * 
    */
   private static ComeAndGo handleDoneComeAndGo(Time time, LinkedList<ComeAndGo> comeAndGoEntriesCopy) {
      ComeAndGo currentDoneComeAndGo = comeAndGoEntriesCopy.getLast();
      TimeSnippet comeAndGoTimeStamp = currentDoneComeAndGo.getComeAndGoTimeStamp();
      Time goTimeStamp = comeAndGoTimeStamp.getEndTimeStamp();
      if (goTimeStamp.compareTo(time) == 0) {
         comeAndGoEntriesCopy.remove(currentDoneComeAndGo);// remove it, since we have to add the resumed ComeAndGo later
         currentDoneComeAndGo = currentDoneComeAndGo.resume();
      } else {
         currentDoneComeAndGo = buildNewComeAndGo(time, String.valueOf(comeAndGoEntriesCopy.size()));
      }
      return currentDoneComeAndGo;
   }

   private static ComeAndGo buildNewComeAndGo(Time time, String id) {
      return ComeAndGoImpl.of(id)
            .comeOrGo(time);
   }

   @Override
   public Optional<ComeAndGo> getCurrentComeAndGo() {
      return getCurrentComeAndGo(comeAndGoEntries);
   }

   @Override
   public Optional<ComeAndGo> getComeAndGo4Id(String id) {
      return comeAndGoEntries.stream()
            .filter(comeAndGo -> StringUtil.isEqual(id, comeAndGo.getId()))
            .findFirst();
   }

   private static Optional<ComeAndGo> getCurrentComeAndGo(LinkedList<ComeAndGo> comeAndGoeEntriesCopy) {
      return comeAndGoeEntriesCopy.stream()
            .filter(ComeAndGo::isNotDone)
            .findFirst();
   }

   /**
    * Removes all {@link ComeAndGoDto} of this {@link ComeAndGoesDto} which are done and returns a
    * new instance in which the done elements are removed
    * 
    * @return a new instance in which the done elements are removed
    */
   @Override
   public ComeAndGoes clearDoneComeAndGoes() {
      List<ComeAndGo> doneComeAndGoes = evalAllDoneComeAndGoes();
      List<ComeAndGo> comeAndGoEntriesCopy = new ArrayList<>(comeAndGoEntries);
      comeAndGoEntriesCopy.removeAll(doneComeAndGoes);
      return new ComeAndGoesImpl(comeAndGoEntriesCopy);
   }

   private List<ComeAndGo> evalAllDoneComeAndGoes() {
      Predicate<ComeAndGo> isNotDone = ComeAndGo::isNotDone;
      return comeAndGoEntries.stream()
            .filter(isNotDone.negate())
            .collect(Collectors.toList());
   }

   @Override
   public ComeAndGoesImpl flagComeAndGoesAsRecorded() {
      return new ComeAndGoesImpl(comeAndGoEntries.stream()
            .map(ComeAndGo::flagAsRecorded)
            .collect(Collectors.toList()));
   }

   @Override
   public boolean hasComeAndGoesFromPrecedentDays() {
      Time now = new Time();
      return comeAndGoEntries.stream()
            .map(ComeAndGo::getComeAndGoTimeStamp)
            .map(TimeSnippet::getBeginTimeStamp)
            .anyMatch(comeAndGoBegin -> comeAndGoBegin.isBefore(now));
   }

   /**
    * @return a unmodifiable list of it's come's and goes
    */
   @Override
   public List<ComeAndGo> getComeAndGoEntries() {
      return Collections.unmodifiableList(comeAndGoEntries);
   }

   /**
    * Creates a new {@link ComeAndGoesDto} with no entries
    * 
    * @return a new {@link ComeAndGoesDto} with no entries
    */
   public static ComeAndGoesImpl of() {
      return new ComeAndGoesImpl();
   }
}
