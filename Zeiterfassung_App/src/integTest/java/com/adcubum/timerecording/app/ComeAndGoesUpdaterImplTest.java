package com.adcubum.timerecording.app;

import static com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepositoryIntegMockUtil.mockBusinessDayRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ComeAndGoesUpdater;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayChangedCallbackHandlerImpl;
import com.adcubum.timerecording.integtest.TestChangedComeAndGoValueImpl;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.util.parser.DateParser;

class ComeAndGoesUpdaterImplTest {

   @Test
   void testChangeComeAndGoChangeCome() throws ParseException {
      // Given
      String comeTimeAsString1 = "10:00";
      String goTimeAsString1 = "11:00";
      String comeTimeAsString2 = "11:15";
      String goTimeAsString2 = "13:00";
      String newTimeAsString = "11:30";
      Time newComeTime = buildTime(newTimeAsString);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCome(buildTime(comeTimeAsString1))
            .withGo(buildTime(goTimeAsString1))
            .withCome(buildTime(comeTimeAsString2))
            .withGo(buildTime(goTimeAsString2))
            .withNewComeTime(newComeTime)
            .withNewGoTime(buildTime(goTimeAsString2))
            .build();

      // When
      ComeAndGoes actualChangedComeAndGoes = tcb.comeAndGoesUpdaterImpl.changeComeAndGo(tcb.changedComeAndGoValue);

      // Then
      Optional<ComeAndGo> comeAndGo4Id = actualChangedComeAndGoes.getComeAndGo4Id(tcb.idOfComeAndGo2Change);
      assertThat(comeAndGo4Id.isPresent(), is(true));
      assertThat(comeAndGo4Id.get().getComeAndGoTimeStamp().getBeginTimeStampRep(), is(newTimeAsString));
   }

   @Test
   void testChangeComeAndGoChangeGo() throws ParseException {
      // Given
      String comeTimeAsString1 = "10:00";
      String goTimeAsString1 = "11:00";
      String comeTimeAsString2 = "11:15";
      String goTimeAsString2 = "13:00";
      String newGoTimeAsString = "14:30";
      Time newGoTime = buildTime(newGoTimeAsString);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCome(buildTime(comeTimeAsString1))
            .withGo(buildTime(goTimeAsString1))
            .withCome(buildTime(comeTimeAsString2))
            .withGo(buildTime(goTimeAsString2))
            .withNewComeTime(buildTime(comeTimeAsString2))
            .withNewGoTime(newGoTime)
            .build();

      // When
      ComeAndGoes actualChangedComeAndGoes = tcb.comeAndGoesUpdaterImpl.changeComeAndGo(tcb.changedComeAndGoValue);

      // Then
      Optional<ComeAndGo> comeAndGo4Id = actualChangedComeAndGoes.getComeAndGo4Id(tcb.idOfComeAndGo2Change);
      assertThat(comeAndGo4Id.isPresent(), is(true));
      assertThat(comeAndGo4Id.get().getComeAndGoTimeStamp().getEndTimeStampRep(), is(newGoTimeAsString));
   }

   private Time buildTime(String comeTimeAsString) throws ParseException {
      return TimeFactory.createNew(DateParser.getTime(comeTimeAsString, TimeFactory.createNew()).getTime());
   }

   private static class TestCaseBuilder {

      private ComeAndGoesUpdater comeAndGoesUpdaterImpl;
      private ChangedComeAndGoValue changedComeAndGoValue;
      private ComeAndGoes comeAndGoes;
      private Time newComeTime;
      private Time newGoTime;
      private UUID idOfComeAndGo2Change;

      private TestCaseBuilder() {
         this.comeAndGoes = ComeAndGoesImpl.of();
      }

      private TestCaseBuilder withNewComeTime(Time newComeTime) {
         this.newComeTime = newComeTime;
         return this;
      }

      private TestCaseBuilder withNewGoTime(Time newGoTime) {
         this.newGoTime = newGoTime;
         return this;
      }

      private TestCaseBuilder withCome(Time comeTime) {
         this.comeAndGoes = comeAndGoes.comeOrGo(comeTime);
         return this;
      }

      private TestCaseBuilder withGo(Time goTime) {
         this.comeAndGoes = comeAndGoes.comeOrGo(goTime);
         return this;
      }

      private TestCaseBuilder build() {
         BusinessDayImpl businessDay = new BusinessDayImpl(comeAndGoes);
         BusinessDayRepository businessDayRepository = mockBusinessDayRepository(businessDay);
         TimeRecorder timeRecorder = new TimeRecorderImpl(mock(BookerAdapter.class), businessDayRepository);
         timeRecorder.init();
         this.idOfComeAndGo2Change = getIdFromRandomSelectedComeAndGo();
         this.comeAndGoesUpdaterImpl = new BusinessDayChangedCallbackHandlerImpl(timeRecorder);
         changedComeAndGoValue = new TestChangedComeAndGoValueImpl(idOfComeAndGo2Change, newComeTime, newGoTime);
         return this;
      }

      private UUID getIdFromRandomSelectedComeAndGo() {
         List<ComeAndGo> comeAndGoEntries = new ArrayList<>(comeAndGoes.getComeAndGoEntries());
         Collections.shuffle(comeAndGoEntries);
         return comeAndGoEntries.get(0).getId();
      }
   }
}
