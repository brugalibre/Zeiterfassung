package com.adcubum.timerecording.ui.security.login.auth.model.resolver;

import static java.util.Objects.nonNull;

import com.adcubum.timerecording.ui.core.model.resolver.impl.AbstractPageModelResolver;
import com.adcubum.timerecording.ui.security.login.auth.model.LoginPageModel;

public class LoginPageModelResolver extends AbstractPageModelResolver<LoginPageModel, LoginPageModel> {

   @Override
   protected LoginPageModel resolveNewPageModel(LoginPageModel dataModelIn) {
      return nonNull(currentPageModel) ? currentPageModel : new LoginPageModel();
   }
}
