package com.adcubum.timerecording.core.work.businessday.comeandgo.change;

import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;

/**
 * The {@link ComeAndGoesUpdater} is an interface for updating a {@link ComeAndGoes}
 * 
 * @author DStalder
 *
 */
public interface ComeAndGoesUpdater {

   /**
    * Updates a {@link ComeAndGo} for the given changed values
    * 
    * @param changedComeAndGoValue
    *        the {@link ChangedComeAndGoValue} define the changed values
    * @return changed {@link ComeAndGoes}
    */
   ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue);

   /**
    * removes all recorded {@link ComeAndGo}es
    */
   void clearComeAndGoes();
}
