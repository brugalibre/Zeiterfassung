package com.adcubum.timerecording.ui.app.pages.overview.book.service;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

/**
 * The {@link BookerService} does the actually boking stuff like. It creates a {@link BookTask}
 * which will be automatically called when calling {@link BookerService#book()}
 * 
 * @author dominic
 *
 */
public class BookerService extends Service<Boolean> {

   /**
    * Does the actually booking process.
    * This calls {@link javafx.concurrent.Service#restart}
    */
   public void book() {
      this.restart();
   }

   @Override
   protected Task<Boolean> createTask() {
      return new BookTask();
   }

   /**
    * Creates the binding between the given ProgressIndicator, this {@link BookerService} and the actual
    * {@link BookerAdapter}
    * 
    * @param progressIndicator
    */
   public void bind(ProgressIndicator progressIndicator) {
      progressIndicator.visibleProperty().bind(runningProperty());
      progressIndicator.progressProperty().bind(progressProperty());
      BookerAdapterFactory.getAdapter();//TODO
   }
}
