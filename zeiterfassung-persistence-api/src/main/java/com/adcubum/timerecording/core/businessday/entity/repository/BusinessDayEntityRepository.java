package com.adcubum.timerecording.core.businessday.entity.repository;

import java.util.UUID;

import com.adcubum.timerecording.core.businessday.common.repository.CommonRepository;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;

/**
 * The {@link BusinessDayEntityRepository} is used within the persistence-layer in order to manage
 * {@link BusinessDayEntity} aggregate
 * 
 * @author dstalder
 *
 */
public interface BusinessDayEntityRepository extends CommonRepository<BusinessDayEntity, UUID> {
   // no-op
}
