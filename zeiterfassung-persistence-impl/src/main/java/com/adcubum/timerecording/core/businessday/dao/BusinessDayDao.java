package com.adcubum.timerecording.core.businessday.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;

/**
 * The {@link BusinessDayDao} as data-access-object in order to create, retrieve and change {@link BusinessDayEntity}
 * 
 * @author dstalder
 *
 */
public interface BusinessDayDao extends CrudRepository<BusinessDayEntity, UUID> {
   // no-op
}
