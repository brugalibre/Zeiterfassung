package com.adcubum.timerecording.core.businessday.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import com.adcubum.timerecording.core.businessday.entity.TimeSnippetEntity;

/**
 * The {@link TimeSnippetDao} is used within the persistence-layer in order to manage
 * {@link TimeSnippetEntity} aggregate
 * 
 * @author dstalder
 *
 */
public interface TimeSnippetDao extends CrudRepository<TimeSnippetEntity, UUID> {

   /**
    * Selects all {@link TimeSnippetEntity} where the {@link TimeSnippetEntity#getBeginTimestamp()} is within
    * or equal the given {@link Timestamp} bounds
    * 
    * @param lowerBounds
    *        the lower bound
    * @param upperBounds
    *        the upper bound
    * @return a list of all selected {@link TimeSnippetEntity}
    */
   @NonNull
   @Query("SELECT t FROM TimeSnippetEntity t WHERE t.beginTimestamp >= ?1 AND t.beginTimestamp <= ?2")
   List<TimeSnippetEntity> findAllTimeSnippetsWithinRange(Timestamp lowerBounds, Timestamp upperBounds);
}

