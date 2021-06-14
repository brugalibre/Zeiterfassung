package com.adcubum.timerecording.work.businessday;

import java.util.ArrayList;
import java.util.List;

import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;

public class BusinessDayBuilder {

   private List<BusinessDayIncrementAdd> businessDayIncrementAdds;
   private BusinessDayImpl businessDay;

   private BusinessDayBuilder() {
      this.businessDay = new BusinessDayImpl();
      this.businessDayIncrementAdds = new ArrayList<>();
   }

   public BusinessDayBuilder withBusinessDayIncrement(BusinessDayIncrementAdd businessDayIncrementAdd) {
      this.businessDayIncrementAdds.add(businessDayIncrementAdd);
      return this;
   }

   public static BusinessDayBuilder of() {
      return new BusinessDayBuilder();
   }

   public BusinessDayImpl build() {
      for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
         businessDay.addBusinessIncrement(businessDayIncrementAdd);
      }
      return businessDay;
   }
}
