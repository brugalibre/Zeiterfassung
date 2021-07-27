package com.adcubum.timerecording.core.work.businessday.repository;

import java.util.UUID;

import com.adcubum.timerecording.core.businessday.common.repository.CommonRepository;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;

public interface BusinessDayRepository extends CommonRepository<BusinessDay, UUID> {
   // no-op
}
