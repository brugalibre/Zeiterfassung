/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.model.resolver;

import com.adcubum.timerecording.ui.app.pages.overview.model.OverviewPageModel;
import com.adcubum.timerecording.ui.core.model.resolver.PageModelResolver;

import src.com.myownb3.dominic.timerecording.app.TimeRecorder;
import src.com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;

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
