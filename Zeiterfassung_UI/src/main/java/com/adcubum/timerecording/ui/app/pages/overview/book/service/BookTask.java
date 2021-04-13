package com.adcubum.timerecording.ui.app.pages.overview.book.service;

import com.adcubum.timerecording.app.TimeRecorder;

import javafx.concurrent.Task;

public class BookTask extends Task<Boolean> {

   @Override
   protected Boolean call() throws Exception {
      return TimeRecorder.INSTANCE.book();
   }
}
