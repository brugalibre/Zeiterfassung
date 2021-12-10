package com.adcubum.timerecording.core.book.coolguys;

import com.adcubum.timerecording.core.book.abacus.DefaultServiceCodes;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.coolguys.exception.InvalidChargeTypeRepresentationException;
import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDto;
import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDtoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Dominic
 *
 */
public class ChargeType implements ServiceCodeAdapter {

   private static final Logger LOG = LoggerFactory.getLogger(ChargeType.class);

   public ChargeType() {
      // private 
   }

   private static final Map<Integer, String> LEISTUNGSARTEN_MAP;

   static {
      LEISTUNGSARTEN_MAP = new LinkedHashMap<>();
      try (InputStream inputStream = getInputStream()) {
         Properties chargeTypesPro = new Properties();
         chargeTypesPro.load(inputStream);

         // Make a list with integers for all numbers of different charge-types in order
         // to sort them
         List<Integer> leistungsartenNr = new ArrayList<>();
         for (Object propertyKey : chargeTypesPro.keySet()) {
            leistungsartenNr.add(Integer.valueOf((String) propertyKey));
         }
         Collections.sort(leistungsartenNr);

         // Now after sorting create for each a proper representation like 'number' -
         // description
         addDefaultLeistungsarten();
         for (Integer leistungsart : leistungsartenNr) {
            LEISTUNGSARTEN_MAP.put(leistungsart,
                  leistungsart + " - " + chargeTypesPro.get(String.valueOf(leistungsart)));
         }
      } catch (IOException e) {
         LOG.info("Kein 'leistungsarten.properties' vorhanden, verwende die Default-Leistungsarten");
         addDefaultLeistungsarten();
      }
   }

   private static InputStream getInputStream() throws FileNotFoundException {
      String fileName = "leistungsarten.properties";
      InputStream inputStream = ChargeType.class.getClassLoader().getResourceAsStream(fileName);
      if (inputStream == null) {
         inputStream = new FileInputStream(fileName);
      }
      return inputStream;
   }

   private static void addDefaultLeistungsarten() {
      LEISTUNGSARTEN_MAP.putAll(DefaultServiceCodes.getDefaultServiceCodes());
   }

   @Override
   public List<ServiceCodeDto> fetchServiceCodesForProjectNr(long projectNr) {
      return getAllServiceCodeDescriptions();// We can't distinguish here between different project-numbers
   }

   @Override
   public List<ServiceCodeDto> getAllServiceCodeDescriptions() {
      return getServiceCodeDtos();
   }

   @Override
   public ServiceCodeDto getServiceCode4Code(Integer serviceCode) {
      return new ServiceCodeDtoImpl(serviceCode, LEISTUNGSARTEN_MAP.get(serviceCode));
   }

   private static List<ServiceCodeDto> getServiceCodeDtos() {
      List<ServiceCodeDto> serviceCodeDtos = new ArrayList<>();
      for (Integer code : LEISTUNGSARTEN_MAP.keySet()) {
         serviceCodeDtos.add(new ServiceCodeDtoImpl(code, LEISTUNGSARTEN_MAP.get(code)));
      }
      return serviceCodeDtos;
   }

   private static int getLeistungsartForRep(String leistungsartRep) throws InvalidChargeTypeRepresentationException {
      for (Integer leistungsart : LEISTUNGSARTEN_MAP.keySet()) {
         if (leistungsartRep.equals(LEISTUNGSARTEN_MAP.get(leistungsart))) {
            return leistungsart;
         }
      }
      throw new InvalidChargeTypeRepresentationException(
            "No Leistungsart found for Description '" + leistungsartRep + "'");
   }
}
