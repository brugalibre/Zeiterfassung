package com.myownb3.dominic.ui.core.view.impl;

import java.awt.Panel;

import com.myownb3.dominic.ui.core.control.Controller;
import com.myownb3.dominic.ui.core.model.PageModel;
import com.myownb3.dominic.ui.core.view.Page;
import com.myownb3.dominic.ui.core.view.PageContent;

/**
 * An {@link AbstractPage} is a kind of huge {@link Panel} which may content
 * different sub-Panels. Those sub Panels can be added <br>
 * to a {@link Page} in order to display them.
 * 
 * @author Dominic Stalder
 * @param <O>
 *        - the outgoing data-model
 * @param <I>
 *        - the incoming data-model
 *
 */
public abstract class AbstractPage<I extends PageModel, O extends PageModel> implements Page<I, O> {
   private Controller<I, O> controller;
   protected PageContent pageContent;

   protected AbstractPage() {
      super();
      initialize();
   }

   protected abstract void initialize();

   /**
    * @return the controller
    */
   @Override
   public Controller<I, O> getController() {
      return controller;
   }

   /**
    * @param controller
    *        the controller to set
    */
   protected void setController(Controller<I, O> controller) {
      this.controller = controller;
   }

   protected void setContent(PageContent pageContent) {
      this.pageContent = pageContent;
   }

   @Override
   public PageContent getContent() {
      return pageContent;
   }
}
