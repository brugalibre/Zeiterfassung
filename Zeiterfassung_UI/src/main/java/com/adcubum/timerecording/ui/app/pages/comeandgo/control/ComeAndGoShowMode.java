package com.adcubum.timerecording.ui.app.pages.comeandgo.control;

import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.StopBusinessDayIncrementController;

/**
 * Defines the different show modes of the {@link ComeAndGoOverviewController}
 * 
 * @author dstalder
 *
 */
public enum ComeAndGoShowMode {

   /** the page displays the {@link ComeAndGoes} */
   COME_AND_GO_PAGE,

   /** the page displays the content of the {@link StopBusinessDayIncrementController} */
   STOP_BD_INC_PAGE,
}
