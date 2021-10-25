/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.model.resolver;

import static java.util.Objects.nonNull;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.ui.app.pages.overview.model.OverviewPageModel;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.impl.AbstractPageModelResolver;

/**
 * @author Dominic
 *
 */
public class OverviewPageModelResolver extends AbstractPageModelResolver<PageModel, OverviewPageModel> {

   @Override
   protected OverviewPageModel resolveNewPageModel(PageModel inPageModel) {
      return resolvePageModelInternal(inPageModel);
   }

   private OverviewPageModel resolvePageModelInternal(PageModel inPageModel) {
      BusinessDay businessDay = TimeRecorder.INSTANCE.getBussinessDay();
      if (inPageModel instanceof OverviewPageModel) {
         return OverviewPageModel.of((OverviewPageModel) inPageModel, businessDay);
      } else if (nonNull(currentPageModel)) {
         return OverviewPageModel.of(currentPageModel, businessDay);
      }
      return new OverviewPageModel(businessDay);
   }
}
