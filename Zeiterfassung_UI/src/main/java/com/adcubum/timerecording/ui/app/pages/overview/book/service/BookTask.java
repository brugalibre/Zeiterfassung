package com.adcubum.timerecording.ui.app.pages.overview.book.service;

import javafx.concurrent.Task;
import src.com.myownb3.dominic.timerecording.app.TimeRecorder;

public class BookTask extends Task<Boolean> {

   @Override
   protected Boolean call() throws Exception {
      return TimeRecorder.INSTANCE.book();
   }
}
