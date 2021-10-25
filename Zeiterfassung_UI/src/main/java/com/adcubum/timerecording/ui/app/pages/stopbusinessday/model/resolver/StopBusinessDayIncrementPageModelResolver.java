/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.resolver;

import static java.util.Objects.nonNull;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoOverviewPageModel;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModelConstructorInfo;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.impl.AbstractPageModelResolver;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModelResolver extends AbstractPageModelResolver<PageModel, StopBusinessDayIncrementPageModel> {

   @Override
   protected StopBusinessDayIncrementPageModel resolveNewPageModel(PageModel inPageModel) {
      return buildNewStopBusinessDayIncrementPageModel(inPageModel);
   }

   private StopBusinessDayIncrementPageModel buildNewStopBusinessDayIncrementPageModel(PageModel inPageModel) {
      if (nonNull(inPageModel)) {
         return buildPageModel4IncomingPageModel(inPageModel);
      }
      StopBusinessDayIncrementPageModelConstructorInfo pageModelConstructorInfo = buildNewStopBDayIncPageModelConstructorInfo();
      return new StopBusinessDayIncrementPageModel(pageModelConstructorInfo);
   }

   private StopBusinessDayIncrementPageModel buildPageModel4IncomingPageModel(PageModel inPageModel) {
      if (inPageModel instanceof StopBusinessDayIncrementPageModel) {
         StopBusinessDayIncrementPageModelConstructorInfo pageModelConstructorInfo =
               buildStopBDayIncPageModelConstructorInfoFromPageModel((StopBusinessDayIncrementPageModel) inPageModel);
         return StopBusinessDayIncrementPageModel.of((StopBusinessDayIncrementPageModel) inPageModel, pageModelConstructorInfo);
      } else if (inPageModel instanceof ComeAndGoOverviewPageModel) {
         StopBusinessDayIncrementPageModelConstructorInfo pageModelConstructorInfo =
               StopBusinessDayIncrementPageModelConstructorInfo.of((ComeAndGoOverviewPageModel) inPageModel);
         return StopBusinessDayIncrementPageModel.of(currentPageModel, pageModelConstructorInfo);
      } else {
         // unknown incoming data-model -> build fresh/re-initialised StopBusinessDayIncrementPageModelConstructorInfo and use the existing Page-Model in order to keep data-binding
         StopBusinessDayIncrementPageModelConstructorInfo pageModelConstructorInfo = buildNewStopBDayIncPageModelConstructorInfo();
         return StopBusinessDayIncrementPageModel.of(currentPageModel, pageModelConstructorInfo);
      }
   }

   private static StopBusinessDayIncrementPageModelConstructorInfo buildStopBDayIncPageModelConstructorInfoFromPageModel(
         StopBusinessDayIncrementPageModel inPageModel) {
      BusinessDayIncrement currentBussinessDayIncremental = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
      TimeSnippet actualTimeSnippet = evalCurrentTimeSnippet(inPageModel, currentBussinessDayIncremental);
      return StopBusinessDayIncrementPageModelConstructorInfo.of(inPageModel, actualTimeSnippet);
   }

   private static StopBusinessDayIncrementPageModelConstructorInfo buildNewStopBDayIncPageModelConstructorInfo() {
      BusinessDayIncrement currentBussinessDayIncremental = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
      return StopBusinessDayIncrementPageModelConstructorInfo.of(currentBussinessDayIncremental,
            currentBussinessDayIncremental.getCurrentTimeSnippet());
   }

   /*
    * This may seem a little bit fuzzy, but 
    * - if the StopBusinessDayIncrementPage has been shown as a subpage there may be already a TimeSnippet on the existing Page-Model we want to re-use.
    * - and if the page is shown after stopping a recording, then we want to use the TimeSnippet on the given BusinessDayIncrement
    */
   private static TimeSnippet evalCurrentTimeSnippet(StopBusinessDayIncrementPageModel inPageModel,
         BusinessDayIncrement currentBussinessDayIncremental) {
      if (nonNull(inPageModel.getTimeSnippet())) {
         return inPageModel.getTimeSnippet();
      }
      return nonNull(currentBussinessDayIncremental.getCurrentTimeSnippet()) ? currentBussinessDayIncremental.getCurrentTimeSnippet()
            : TimeSnippetFactory.createNew();
   }
}
