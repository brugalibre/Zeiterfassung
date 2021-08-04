package com.adcubum.timerecording.core.businessday.comeandgo.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoesEntity;

/**
 * The {@link ComeAndGoesDao} as data-access-object in order to create, retrieve and change {@link ComeAndGoesEntity}
 * 
 * @author dstalder
 *
 */
public interface ComeAndGoesDao extends CrudRepository<ComeAndGoesEntity, UUID> {
   // no-op
}
