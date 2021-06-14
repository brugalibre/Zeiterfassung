package com.adcubum.timerecording.core.work.businessday.comeandgo.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.TestChangedComeAndGoValueImpl;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.test.BaseTestWithSettings;
import com.adcubum.timerecording.work.businessday.BusinessDayBuilder;
import com.adcubum.timerecording.work.businessday.TimeSnippetBuilder;
import com.adcubum.timerecording.work.date.TimeFactory;

class ComeAndGoesImplTest extends BaseTestWithSettings {

   @Test
   void testComeOrGo_ManualComeOrGoeAndThenApplyFromBusinessDay() {
      // Given
      long begin1 = System.currentTimeMillis() - 200_000;
      long end1 = begin1 + 200_000;
      BusinessDayImpl businessDay = BusinessDayBuilder.of()
            .withBusinessDayIncrement(new BusinessDayIncrementAddBuilder()
                  .withTimeSnippet(TimeSnippetBuilder.of()
                        .withStartAndStopTime(begin1, end1)
                        .build())
                  .withDescription("Default Description")
                  .withTicket(Ticket.dummy("6848"))
                  .build())
            .build();
      businessDay.comeOrGo();

      // When
      ComeAndGoes comeAndGoes = businessDay.getComeAndGoes();
      List<ComeAndGo> comeAndGoesEntries = comeAndGoes.getComeAndGoEntries();

      // Then
      assertThat(comeAndGoesEntries.size(), is(1));
      ComeAndGo comeAndGo = comeAndGoesEntries.get(0);
      assertThat(comeAndGo.isNotDone(), is(true));
   }

   @Test
   void testComeOrGoes_ChangeComeAndGoesWithoutAnyEntries() {
      // Given
      ComeAndGoes comeAndGoes = ComeAndGoesImpl.of();
      ChangedComeAndGoValue value = new TestChangedComeAndGoValueImpl("1", TimeFactory.createNew(), TimeFactory.createNew());

      // When
      ComeAndGoes changeComeAndGo = comeAndGoes.changeComeAndGo(value);

      // Then
      assertThat(changeComeAndGo, is(comeAndGoes));
   }

   @Test
   void testComeOrGo_Start2ndComeWithBeginSameThanEnd() {
      // Given
      long begin = System.currentTimeMillis() - 10_000;
      long end = System.currentTimeMillis() - 10_000;
      ComeAndGoes comeAndGoes = ComeAndGoesImpl.of()
            .comeOrGo(TimeFactory.createNew(begin))
            .comeOrGo(TimeFactory.createNew(end));// this comeAndGo is done by now

      // When
      ComeAndGoes changedComeAndGo = comeAndGoes.comeOrGo(TimeFactory.createNew(end));// start a new one, with the same begin than the previous' end
      List<ComeAndGo> comeAndGoesEntries = changedComeAndGo.getComeAndGoEntries();

      // Then
      assertThat(comeAndGoesEntries.size(), is(1));
   }

   @Test
   void testNewComeOrGo_initialStart() {
      // Given 
      ComeAndGoesImpl comeAndGos = ComeAndGoesImpl.of();

      // When
      ComeAndGoes changedComeAndGoes = comeAndGos.comeOrGo();
      List<ComeAndGo> actualComeAndGoes = changedComeAndGoes.getComeAndGoEntries();

      // Then
      assertThat(actualComeAndGoes.size(), is(1));
      ComeAndGo comeAndGo = actualComeAndGoes.get(0);
      assertThat(comeAndGo.isNotDone(), is(true));
   }

   @Test
   void testNewComeOrGo_comeAndGo() {
      // Given 
      ComeAndGoes comeAndGoes = ComeAndGoesImpl.of();

      // When
      ComeAndGoes changedComeAndGoes = comeAndGoes.comeOrGo()
            .comeOrGo();
      List<ComeAndGo> actualComeAndGoes = changedComeAndGoes.getComeAndGoEntries();
      List<ComeAndGo> unchangedComeAndGoes = comeAndGoes.getComeAndGoEntries();

      // Then
      assertThat(actualComeAndGoes.size(), is(1));
      ComeAndGo comeAndGo = actualComeAndGoes.get(0);
      assertThat(comeAndGo.isNotDone(), is(false));
      assertThat(unchangedComeAndGoes.isEmpty(), is(true));
   }

   @Test
   void testNewComeOrGo_comeAndGoAndClear() {
      // Given 

      // When
      ComeAndGoes comeAndGoes = ComeAndGoesImpl.of()
            .comeOrGo()
            .comeOrGo();
      ComeAndGoes clearedComeAndGo = comeAndGoes.clearDoneComeAndGoes();
      List<ComeAndGo> actualComeAndGoes = clearedComeAndGo.getComeAndGoEntries();
      List<ComeAndGo> existingComeAndGoes = comeAndGoes.getComeAndGoEntries();

      // Then
      assertThat(actualComeAndGoes.isEmpty(), is(true));
      assertThat(existingComeAndGoes.size(), is(1));
   }

   @Test
   void testNewComeOrGo_comeAndGoAndComeAgain() {
      // Given 
      ComeAndGoes comeAndGos = ComeAndGoesImpl.of();
      long begin1 = System.currentTimeMillis() - 200_000;
      long end1 = begin1 + 200_000;
      long begin2 = end1 + 300_000;

      // When
      ComeAndGoes changedComeAndGos = comeAndGos.comeOrGo(TimeFactory.createNew(begin1))
            .comeOrGo(TimeFactory.createNew(end1))
            .comeOrGo(TimeFactory.createNew(begin2));
      List<ComeAndGo> actualComeAndGoes = changedComeAndGos.getComeAndGoEntries();

      // Then
      assertThat(comeAndGos.getComeAndGoEntries().isEmpty(), is(true));
      assertThat(actualComeAndGoes.size(), is(2));
      ComeAndGo firstComeAndGo = actualComeAndGoes.get(0);
      ComeAndGo secondComeAndGo = actualComeAndGoes.get(1);
      assertThat(firstComeAndGo.isNotDone(), is(false));
      assertThat(secondComeAndGo.isNotDone(), is(true));
   }
}
