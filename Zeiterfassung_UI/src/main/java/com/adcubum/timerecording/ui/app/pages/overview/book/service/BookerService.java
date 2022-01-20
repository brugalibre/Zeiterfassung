package com.adcubum.timerecording.ui.app.pages.overview.book.service;


import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.app.book.TimeRecorderBookResult;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

/**
 * The {@link BookerService} calls the {@link TimeRecorder#book()} when executed. It creates a {@link BookTask}
 * which will be automatically called when calling {@link BookerService#book()}
 *
 * @author dominic
 */
public class BookerService extends Service<TimeRecorderBookResult> {

   /**
    * Does the actually booking process.
    * This calls {@link javafx.concurrent.Service#restart}
    */
   public void book() {
      this.restart();
   }

   @Override
   protected Task<TimeRecorderBookResult> createTask() {
      return new BookTask();
   }

   /**
    * Creates the binding between the given ProgressIndicator and this {@link BookerService}
    *
    * @param progressIndicator the {@link ProgressIndicator} bindet to the ui
    */
   public void bind(ProgressIndicator progressIndicator) {
      progressIndicator.visibleProperty().bind(runningProperty());
      progressIndicator.progressProperty().bind(progressProperty());
   }
}
