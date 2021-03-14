/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.model.resolver;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.myownb3.dominic.ui.app.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;

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
