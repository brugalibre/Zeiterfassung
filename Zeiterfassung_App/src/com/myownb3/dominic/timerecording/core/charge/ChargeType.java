/**
 * 
 */
package com.myownb3.dominic.timerecording.core.charge;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.myownb3.dominic.timerecording.core.charge.exception.InvalidChargeTypeRepresentationException;

/**
 * @author Dominic
 *
 */
public class ChargeType {

   private static final Logger LOG = Logger.getLogger(ChargeType.class);

   private ChargeType() {
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
      LEISTUNGSARTEN_MAP.put(100, "100 - Analyse");
      LEISTUNGSARTEN_MAP.put(111, "111 - Meeting");
      LEISTUNGSARTEN_MAP.put(113, "113 - Umsetzung/Dokumentation");
      LEISTUNGSARTEN_MAP.put(122, "122 - Qualtitätssicherung");
      LEISTUNGSARTEN_MAP.put(140, "140 - Allg. Verwaltungsarbeiten");
      LEISTUNGSARTEN_MAP.put(164, "164 - bezahlte Abwesenheiten");
   }

   public static String[] getLeistungsartenRepresentation() {
      List<Integer> leistungsartenIntList = new ArrayList<>(LEISTUNGSARTEN_MAP.keySet());
      List<String> leistungsartenStrings = leistungsartenIntList.stream()//
            .map(LEISTUNGSARTEN_MAP::get)//
            .collect(Collectors.toList());
      return leistungsartenStrings.toArray(new String[leistungsartenStrings.size()]);
   }

   public static int getLeistungsartForRep(String leistungsartRep) throws InvalidChargeTypeRepresentationException {
      for (Integer leistungsart : LEISTUNGSARTEN_MAP.keySet()) {
         if (leistungsartRep.equals(LEISTUNGSARTEN_MAP.get(leistungsart))) {
            return leistungsart;
         }
      }
      throw new InvalidChargeTypeRepresentationException(
            "No Leistungsart found for Description '" + leistungsartRep + "'");
   }

   /**
    * Returns the description of the given Charge-Type
    * 
    * @param chargeType
    * @return the description of the given Charge-Type
    */
   public static String getRepresentation(int chargeType) {
      return LEISTUNGSARTEN_MAP.get(chargeType);
   }
}
