package com.adcubum.timerecording.core.book.abacus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.adcubum.j2a.zeiterfassung.AbacusBookingConnector;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;

public class AbacusServiceCodeAdapter implements ServiceCodeAdapter {

   private static final Logger LOG = Logger.getLogger(AbacusServiceCodeAdapter.class);
   private AbacusBookingConnector abacusBookingConnector;
   private Map<Integer, String> allServiceCodes;

   AbacusServiceCodeAdapter(AbacusBookingConnector abacusBookingConnector) {
      this.abacusBookingConnector = abacusBookingConnector;
      this.allServiceCodes = DefaultServiceCodes.getDefaultServiceCodes();
   }

   public void init() {
      try {
         this.allServiceCodes = abacusBookingConnector.fetchAllServiceCodes();
      } catch (Exception e) {
         LOG.error("Error occurred while evaluating all service codes", e);
      }
   }

   @Override
   public List<String> fetchServiceCodesForProjectNr(long projectNr) {
      Map<Integer, String> serviceCodesForProject = abacusBookingConnector.fetchServiceCodesForProject(projectNr);
      return new ArrayList<>(serviceCodesForProject.values());
   }

   @Override
   public int getServiceCode4Description(String givenServiceCodeDesc) {
      for (Entry<Integer, String> serviceCode2Desc : allServiceCodes.entrySet()) {
         if (serviceCode2Desc.getValue().equals(givenServiceCodeDesc)) {
            return serviceCode2Desc.getKey();
         }
      }
      return -1;
   }

   @Override
   public List<String> getAllServiceCodes() {
      return new ArrayList<>(allServiceCodes.values());
   }

   @Override
   public String getServiceCodeDescription4ServiceCode(int serviceCode) {
      return allServiceCodes.get(serviceCode);
   }
}
