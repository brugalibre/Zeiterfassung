/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.comeandgo.model.resolver;

import static java.util.Objects.nonNull;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoOverviewPageModel;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.model.resolver.impl.AbstractPageModelResolver;

/**
 * @author Dominic
 *
 */
public class ComeAndGoPageModelResolver extends AbstractPageModelResolver<PageModel, ComeAndGoOverviewPageModel> {

   @Override
   protected ComeAndGoOverviewPageModel resolveNewPageModel(PageModel inPageModel) {
      return resolvePageModelInternal(inPageModel);
   }

   private ComeAndGoOverviewPageModel resolvePageModelInternal(PageModel inPageModel) {
      ComeAndGoes comeAndGoes = TimeRecorder.INSTANCE.getBussinessDay().getComeAndGoes();
      if (inPageModel instanceof ComeAndGoOverviewPageModel) {
         return ComeAndGoOverviewPageModel.of((ComeAndGoOverviewPageModel) inPageModel, comeAndGoes);
      } else if (nonNull(currentPageModel)) {
         return ComeAndGoOverviewPageModel.of(currentPageModel, comeAndGoes);
      }
      return ComeAndGoOverviewPageModel.of(comeAndGoes);
   }
}
