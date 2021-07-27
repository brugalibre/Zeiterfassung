package com.adcubum.timerecording.core.businessday.common.repository;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.adcubum.timerecording.core.repository.ObjectNotFoundException;

/**
 * Common repository definition for all repositories dealing with any kind of a storable object
 * 
 * @author dstalder
 *
 * @param <T>
 *        the specific type of a storable object
 * @param <I>
 *        the type of the id
 */
public interface CommonRepository<T, I> {

   /**
    * Evaluates a specific type of a storable object for the given id which is managed by this {@link CommonRepository}
    * Note that this method throws an {@link ObjectNotFoundException} if there is entity associated with the given id
    * 
    * @param objectId
    *        the id of the storable object to load
    * @return the storable object
    * @throws ObjectNotFoundException
    *         if there is no latest
    */
   @NonNull
   T findById(@NonNull I objectId) throws ObjectNotFoundException;

   /**
    * @return the first found object or create a new one, if there is none.
    */
   @Nullable
   T findFirstOrCreateNew();

   /**
    * Saves the given type of storable object and return the changed entity
    * 
    * @param businessDay
    *        the storable object to store
    * @return the changed entity
    */
   @NonNull
   T save(@NonNull T storableObject);

   /**
    * Removes all persisted entities managed by this {@link CommonRepository}
    */
   void deleteAll();
}
