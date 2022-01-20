package com.adcubum.timerecording.ui.app.pages.overview.book.service;

import com.adcubum.timerecording.app.TimeRecorder;

import com.adcubum.timerecording.app.book.TimeRecorderBookResult;
import javafx.concurrent.Task;

public class BookTask extends Task<TimeRecorderBookResult> {

   @Override
   protected TimeRecorderBookResult call() throws Exception {
      return TimeRecorder.INSTANCE.book();
   }
}
