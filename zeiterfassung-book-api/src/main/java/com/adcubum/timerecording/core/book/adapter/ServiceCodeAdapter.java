package com.adcubum.timerecording.core.book.adapter;


import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDto;

import java.util.List;

/**
 * The {@link ServiceCodeAdapter} wraps the actual implementation for handling the service codes, depending on the underlying booking
 * implementation
 * 
 * @author Dominic
 *
 */
public interface ServiceCodeAdapter {

   /**
    * Returns all {@link ServiceCodeDto}s for the given project-Nr
    * 
    * @param projectNr
    *        the number of a project
    * @return  all {@link ServiceCodeDto}s for the given project-NrNr
    */
   List<ServiceCodeDto> fetchServiceCodesForProjectNr(long projectNr);

   /**
    * @return all previously fetched {@link ServiceCodeDto}s
    */
   List<ServiceCodeDto> getAllServiceCodeDescriptions();

   /**
    * Returns a {@link ServiceCodeDto} for the given unique code value
    *
    * @param serviceCode
    *        the given unique code value
    * @return a {@link ServiceCodeDto} for the given unique code value
    */
   ServiceCodeDto getServiceCode4Code(Integer serviceCode);
}
