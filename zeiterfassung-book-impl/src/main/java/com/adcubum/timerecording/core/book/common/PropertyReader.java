package com.adcubum.timerecording.core.book.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * The {@link PropertyReader} is used to open and read a properties-file
 */
public class PropertyReader {

   private static final Logger LOG = LoggerFactory.getLogger(PropertyReader.class);
   private String propertiesName;

   public PropertyReader(String propertiesName) {
      this.propertiesName = requireNonNull(propertiesName);
   }

   private List<SimpleEntry<String, String>> readValues(String propFile) {
      try (InputStream resourceStream = getInputStream(propFile)) {
         return readValues(resourceStream);
      } catch (IOException e) {
         LOG.error("Unable to read values in property file '" + propFile + "'", e);
      }
      throw new IllegalStateException("No values read in property file '" + propFile + "'");
   }

   private static List<SimpleEntry<String, String>> readValues(InputStream resourceStream) throws IOException {
      Properties prop = new Properties();
      prop.load(resourceStream);
      List<SimpleEntry<String, String>> values = new ArrayList<>();
      for (Map.Entry<Object, Object> objectObjectEntry : prop.entrySet()) {
         values.add(new SimpleEntry<>((String) objectObjectEntry.getKey(), (String) objectObjectEntry.getValue()));
      }
      return values;
   }

   private static InputStream getInputStream(String propertiesName) throws FileNotFoundException {
      InputStream resourceStreamFromResource = PropertyReader.class.getClassLoader().getResourceAsStream(propertiesName);
      if (isNull(resourceStreamFromResource)) {
         return new FileInputStream(propertiesName);
      }
      return resourceStreamFromResource;
   }

   /**
    * Reads the value for the given properties-key
    *
    * @return the value for the given properties-key
    */
   public List<SimpleEntry<String, String>> readAllKeyValuePairs() {
      return readValues(propertiesName);
   }
}
