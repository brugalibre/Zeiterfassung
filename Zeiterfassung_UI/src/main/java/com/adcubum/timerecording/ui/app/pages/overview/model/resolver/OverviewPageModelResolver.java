/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.model.resolver;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.adcubum.timerecording.ui.app.pages.overview.model.OverviewPageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;

/**
 * @author Dominic
 *
 */
public class OverviewPageModelResolver implements PageModelResolver<OverviewPageModel, OverviewPageModel> {

   @Override
   public OverviewPageModel resolvePageModel(OverviewPageModel inPageModel) {
      BusinessDayVO businessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      if (inPageModel == null) {
         return new OverviewPageModel(businessDayVO);
      }
      return OverviewPageModel.of(inPageModel, businessDayVO);
   }
}
