package com.adcubum.timerecording.core.book.adapter.delegated.factory;

//import com.adcubum.timerecording.core.book.abacus.AbacusBookerAdapter;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.coolguys.BookerHelper;
import com.adcubum.timerecording.core.book.proles.ProlesBookerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import static java.util.Objects.requireNonNull;

public enum BookerAdapterType {

   /** A web based booker which books directly via a jira-ticket */
   JIRA_WEB("jira-web", BookerHelper.class),

   /** A booker which books directly via the abacus api */
   // ABACUS_API("abacus-api", AbacusBookerAdapter.class),

   /** A web based booker which books directly via the proles-website */
   PROLES_WEB("proles-web", ProlesBookerHelper.class);

   private String name;
   private Class<? extends BookerAdapter> bookerAdapterClass;
   private static final Logger LOG = LoggerFactory.getLogger(BookerAdapterType.class);

   BookerAdapterType(String name, Class<? extends BookerAdapter> bookerAdapterClass) {
      this.name = requireNonNull(name);
      this.bookerAdapterClass = requireNonNull(bookerAdapterClass);
   }

   public String getName() {
      return name;
   }

   public BookerAdapter createBookerAdapter() {
      try {
         return bookerAdapterClass.getDeclaredConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
         throw new IllegalArgumentException("Could not create new instance of BookingAdapter '" + bookerAdapterClass + "!'", e);
      }
   }

   public static BookerAdapterType getForName(String bookerName) {
      for (BookerAdapterType bookerAdapterType : BookerAdapterType.values()) {
         if (bookerAdapterType.name.equals(bookerName)) {
            return bookerAdapterType;
         }
      }
      LOG.warn("No BookerAdapterType found for name '" + bookerName + "'. Fallback to thefault '" + BookerAdapterType.JIRA_WEB.name + "'");
      // Fallback due to backwards compatibility
      return BookerAdapterType.JIRA_WEB;
   }
}
