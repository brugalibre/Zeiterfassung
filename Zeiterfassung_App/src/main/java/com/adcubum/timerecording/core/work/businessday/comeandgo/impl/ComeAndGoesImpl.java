package com.adcubum.timerecording.core.work.businessday.comeandgo.impl;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.settings.round.TimeRounder;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.util.utils.LogUtil;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The class {@link ComeAndGoesImpl} keeps track of each time a user comes and goes during the business day.
 * Such a coming or going is represented by a single {@link ComeAndGo} instance
 * 
 * @author DStalder
 *
 */
public class ComeAndGoesImpl implements ComeAndGoes {

   private UUID id;
   private LinkedList<ComeAndGo> comeAndGoEntries;

   private ComeAndGoesImpl() {
      this(null, new LinkedList<>());
   }

   private ComeAndGoesImpl(UUID id, List<ComeAndGo> comeAndGoEntries) {
      this.comeAndGoEntries = new LinkedList<>(comeAndGoEntries);
      this.id = id;
   }

   @Override
   public ComeAndGoes comeOrGo() {
      DateTime newTime = DateTimeFactory.createNew(System.currentTimeMillis(), TimeRounder.INSTANCE.getRoundMode());
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
      return new ComeAndGoesImpl(id, comeAndGoEntriesCopy);
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
      return comeAndGo.getId().equals(changedComeAndGoValue.getId());
   }

   @Override
   public ComeAndGoes comeOrGo(DateTime time) {
      List<ComeAndGo> changedComeAndGoEntries = comeOrGoInternal(time, new LinkedList<>(comeAndGoEntries));
      return new ComeAndGoesImpl(id, changedComeAndGoEntries);
   }

   private LinkedList<ComeAndGo> comeOrGoInternal(DateTime time, LinkedList<ComeAndGo> comeAndGoeEntriesCopy) {
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

   private static ComeAndGo handleNoCurrentComeOrGo(DateTime time, LinkedList<ComeAndGo> comeAndGoEntriesCopy) {
      if (comeAndGoEntriesCopy.isEmpty()) {
         return buildNewComeAndGo(time, UUID.randomUUID());
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
   private static ComeAndGo handleDoneComeAndGo(DateTime time, LinkedList<ComeAndGo> comeAndGoEntriesCopy) {
      ComeAndGo currentDoneComeAndGo = comeAndGoEntriesCopy.getLast();
      TimeSnippet comeAndGoTimeStamp = currentDoneComeAndGo.getComeAndGoTimeStamp();
      DateTime goTimeStamp = comeAndGoTimeStamp.getEndTimeStamp();
      if (goTimeStamp.compareTo(time) == 0) {
         comeAndGoEntriesCopy.remove(currentDoneComeAndGo);// remove it, since we have to add the resumed ComeAndGo later
         currentDoneComeAndGo = currentDoneComeAndGo.resume();
      } else {
         currentDoneComeAndGo = buildNewComeAndGo(time, UUID.randomUUID());
      }
      return currentDoneComeAndGo;
   }

   private static ComeAndGo buildNewComeAndGo(DateTime time, UUID id) {
      return ComeAndGoImpl.of(id)
            .comeOrGo(time);
   }

   @Override
   public Optional<ComeAndGo> getCurrentComeAndGo() {
      return getCurrentComeAndGo(comeAndGoEntries);
   }

   @Override
   public boolean hasUnfinishedComeAndGo() {
      return getCurrentComeAndGo().isPresent();
   }

   @Override
   public Optional<ComeAndGo> getComeAndGo4Id(UUID id) {
      return comeAndGoEntries.stream()
            .filter(comeAndGo -> comeAndGo.getId().equals(id))
            .findFirst();
   }

   private static Optional<ComeAndGo> getCurrentComeAndGo(LinkedList<ComeAndGo> comeAndGoeEntriesCopy) {
      return comeAndGoeEntriesCopy.stream()
            .filter(ComeAndGo::isNotDone)
            .findFirst();
   }

   @Override
   public ComeAndGoes clearDoneComeAndGoes() {
      List<ComeAndGo> doneComeAndGoes = evalAllDoneComeAndGoes();
      List<ComeAndGo> comeAndGoEntriesCopy = new ArrayList<>(comeAndGoEntries);
      comeAndGoEntriesCopy.removeAll(doneComeAndGoes);
      return new ComeAndGoesImpl(id, comeAndGoEntriesCopy);
   }

   private List<ComeAndGo> evalAllDoneComeAndGoes() {
      Predicate<ComeAndGo> isNotDone = ComeAndGo::isNotDone;
      return comeAndGoEntries.stream()
            .filter(isNotDone.negate())
            .collect(Collectors.toList());
   }

   @Override
   public ComeAndGoesImpl flagComeAndGoesAsRecorded() {
      return new ComeAndGoesImpl(id, comeAndGoEntries.stream()
            .map(ComeAndGo::flagAsRecorded)
            .collect(Collectors.toList()));
   }

   @Override
   public boolean hasComeAndGoesFromPrecedentDays() {
      DateTime now = DateTimeFactory.createNew();
      return comeAndGoEntries.stream()
            .map(ComeAndGo::getComeAndGoTimeStamp)
            .map(TimeSnippet::getBeginTimeStamp)
            .anyMatch(comeAndGoBegin -> comeAndGoBegin.isBefore(now));
   }

   @Override
   public boolean hasNotRecordedComeAndGoContent() {
      return evalAllDoneComeAndGoes()
            .stream()
            .anyMatch(ComeAndGo::isNotRecorded);
   }

   @Override
   public List<ComeAndGo> getComeAndGoEntries() {
      return Collections.unmodifiableList(comeAndGoEntries);
   }

   @Override
   public UUID getId() {
      return id;
   }

   @Override
   public String toString() {
      return "ComeAndGoesImpl{" +
              "\n\tid=" + id + "," +
              "\n\tcomeAndGoEntries=" + LogUtil.toLogString(comeAndGoEntries) + "," +
              "\n}";
   }

   /**
    * Creates a new {@link ComeAndGoesImpl} with no entries
    * 
    * @return a new {@link ComeAndGoesImpl} with no entries
    */
   public static ComeAndGoesImpl of() {
      return new ComeAndGoesImpl();
   }

   /**
    * Creates a new {@link ComeAndGoesImpl} with no entries
    * 
    * @param id
    *        the id of this {@link ComeAndGoesImpl}
    * @param comeAndGoEntries
    *        the list of {@link ComeAndGo} entries
    * @return a new {@link ComeAndGoesImpl} with the given id and {@link ComeAndGo} entries
    */
   public static ComeAndGoesImpl of(UUID id, List<ComeAndGo> comeAndGoEntries) {
      return new ComeAndGoesImpl(id, comeAndGoEntries);
   }
}
