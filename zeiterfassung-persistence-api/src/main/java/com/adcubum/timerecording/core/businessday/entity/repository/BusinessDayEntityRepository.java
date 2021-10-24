package com.adcubum.timerecording.core.businessday.entity.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.adcubum.timerecording.core.businessday.common.repository.CommonBusinessDayRepository;
import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.work.date.DateTime;

/**
 * The {@link BusinessDayEntityRepository} is used within the persistence-layer in order to manage
 * {@link BusinessDayEntity} aggregate
 * 
 * @author dstalder
 *
 */
public interface BusinessDayEntityRepository extends CommonBusinessDayRepository<BusinessDayEntity, UUID> {

   /**
    * Selects all booked {@link BusinessDayEntity} which are within or equal the given {@link DateTime} bounds
    * 
    * @param lowerBounds
    *        the lower bound
    * @param upperBounds
    *        the upper bound
    * @return a list of all selected {@link BusinessDayEntity}
    */
   @NonNull
   List<BusinessDayEntity> findAllBookedBusinessDayEntitiesWithinRange(@NonNull DateTime lowerBounds, @NonNull DateTime upperBounds);

   /**
    * Selects the first {@link BusinessDayEntity} which is within or equal the given {@link DateTime} bounds
    * 
    * @param lowerBounds
    *        the lower bound
    * @param upperBounds
    *        the upper bound
    * @return an optional {@link BusinessDayEntity}
    */
   @NonNull
   Optional<BusinessDayEntity> findBookedBusinessDayEntityWithinRange(@NonNull DateTime lowerBounds, @NonNull DateTime upperBounds);
}
