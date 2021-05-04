/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.resolver;

import static java.util.Objects.nonNull;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.impl.AbstractPageModelResolver;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModelResolver extends AbstractPageModelResolver<PageModel, StopBusinessDayIncrementPageModel> {

   @Override
   protected StopBusinessDayIncrementPageModel resolveNewPageModel(PageModel inPageModel) {
      BusinessDayIncrementVO currentBussinessDayIncremental = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
      if (inPageModel instanceof StopBusinessDayIncrementPageModel) {
         return StopBusinessDayIncrementPageModel.of((StopBusinessDayIncrementPageModel) inPageModel, currentBussinessDayIncremental);
      } else if (nonNull(currentPageModel)) {
         return StopBusinessDayIncrementPageModel.of(currentPageModel, currentBussinessDayIncremental);
      }
      return new StopBusinessDayIncrementPageModel(currentBussinessDayIncremental);
   }
}
