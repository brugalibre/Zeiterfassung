package com.adcubum.timerecording.core.book.adapter;

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
    * Returns all service codes description for the given project-Nr
    * 
    * @param projectNr
    *        the number of a project
    * @return all service codes description for the given project-Nr
    */
   public List<String> fetchServiceCodesForProjectNr(long projectNr);

   /**
    * @return all previously fetched service codes descriptions
    */
   public List<String> getAllServiceCodes();

   /**
    * @param serviceCodeDesc
    *        the description for a service code
    * @return the actual service code for a description
    */
   public int getServiceCode4Description(String serviceCodeDesc);

   /**
    * Return the description for the given service code
    * 
    * @param serviceCode
    *        the service code
    * @return the description for the given service code
    */
   public String getServiceCodeDescription4ServiceCode(int serviceCode);
}
