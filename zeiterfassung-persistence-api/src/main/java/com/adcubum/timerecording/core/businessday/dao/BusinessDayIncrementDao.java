package com.adcubum.timerecording.core.businessday.dao;

import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayIncrementEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * The {@link BusinessDayIncrementDao} as data-access-object in order to create, retrieve and change {@link BusinessDayEntity}
 *
 * @author dstalder
 */
public interface BusinessDayIncrementDao extends CrudRepository<BusinessDayIncrementEntity, UUID> {
   // no-op
}