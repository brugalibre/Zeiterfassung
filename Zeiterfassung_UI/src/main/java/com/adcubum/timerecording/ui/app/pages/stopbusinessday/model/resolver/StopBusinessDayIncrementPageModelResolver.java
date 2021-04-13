/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.resolver;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModelResolver
      implements PageModelResolver<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel> {

   @Override
   public StopBusinessDayIncrementPageModel resolvePageModel(StopBusinessDayIncrementPageModel inPageModel) {

      BusinessDayIncrementVO currentBussinessDayIncremental = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
      if (inPageModel == null) {
         return new StopBusinessDayIncrementPageModel(currentBussinessDayIncremental);
      }
      return StopBusinessDayIncrementPageModel.of(inPageModel, currentBussinessDayIncremental);
   }
}
