package com.adcubum.scheduler;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.util.parser.DateParser;

class SchedulerContainerTest {

   @Test
   void testAddMockedBeginAndEndSchedulerListener() throws InterruptedException {
      // Given
      SchedulerContainerImpl schedulerListener = spy(new SchedulerContainerImpl());
      SchedulerImpl beginScheduler = mock(SchedulerImpl.class);
      SchedulerImpl endScheduler = mock(SchedulerImpl.class);
      Runnable runnable = mock(Runnable.class);

      String begin = "10:00";
      String end = null;

      doReturn(beginScheduler).when(schedulerListener).createScheduler(eq(begin), any());
      doReturn(endScheduler).when(schedulerListener).createScheduler(eq(end), any());
      schedulerListener.addScheduler(runnable, begin);
      schedulerListener.addScheduler(runnable, end);

      // When
      schedulerListener.start();

      // Then
      verify(beginScheduler).start();
      verify(endScheduler, never()).start();
   }

   @Test
   void testStartRealSchedulerListener() throws InterruptedException {

      // Given
      AtomicBoolean wasInvoked = new AtomicBoolean();
      SchedulerContainerImpl schedulerListener = new SchedulerContainerImpl(TimeUnit.SECONDS, 1);
      Time now = TimeFactory.createNew(System.currentTimeMillis(), RoundMode.SEC);
      String timeValueAsString = DateParser.parse2String(now.addSeconds(2), DateParser.HOUR_MIN_SEC_PATTERN);
      schedulerListener.addScheduler(() -> wasInvoked.set(true), timeValueAsString);

      // When
      schedulerListener.start();
      Awaitility.await().atLeast(900, TimeUnit.MILLISECONDS).until(() -> wasInvoked.get());

      // Then
      assertThat(wasInvoked.get(), is(true));
      assertThat(schedulerListener.isStoped(), is(false));
   }

   @Test
   void testStartRealSchedulerListenerButDontRemind() throws InterruptedException {

      // Given
      AtomicBoolean wasInvoked = new AtomicBoolean();
      SchedulerContainerImpl schedulerListener = new SchedulerContainerImpl(TimeUnit.SECONDS, 1);
      Time beforNow = TimeFactory.createNew(System.currentTimeMillis() - 1000);// -> accuracy 'MINUTES' in order to create a negativ wait duration
      String timeValueAsString = DateParser.parse2String(beforNow, DateParser.HOUR_MIN_SEC_PATTERN);
      schedulerListener.addScheduler(() -> wasInvoked.set(true), timeValueAsString);

      // When
      schedulerListener.start();
      TimeUnit.MILLISECONDS.sleep(1500);

      // Then
      assertThat(wasInvoked.get(), is(false));
   }

   @Test
   void testStartAndStopRealSchedulerListener() throws InterruptedException {

      // Given
      AtomicBoolean wasInvoked = spy(new AtomicBoolean());
      SchedulerContainerImpl schedulerListener = new SchedulerContainerImpl(TimeUnit.SECONDS, 1);
      Time now = TimeFactory.createNew(System.currentTimeMillis(), RoundMode.SEC);
      String timeValueAsString = DateParser.parse2String(now.addSeconds(1), DateParser.HOUR_MIN_SEC_PATTERN);
      schedulerListener.addScheduler(() -> wasInvoked.set(true), timeValueAsString);

      // When
      schedulerListener.start();
      Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> wasInvoked.get());
      schedulerListener.stop();
      TimeUnit.SECONDS.sleep(2);

      // Then
      assertThat(wasInvoked.get(), is(true));
      assertThat(schedulerListener.isStoped(), is(true));
   }
}
