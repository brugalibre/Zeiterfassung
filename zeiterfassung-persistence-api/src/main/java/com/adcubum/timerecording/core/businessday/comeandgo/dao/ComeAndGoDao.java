package com.adcubum.timerecording.core.businessday.comeandgo.dao;

import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * The {@link ComeAndGoDao} as data-access-object in order to create, retrieve and change {@link ComeAndGoEntity}
 * 
 * @author dstalder
 *
 */
public interface ComeAndGoDao extends CrudRepository<ComeAndGoEntity, UUID> {
   // no-op
}
