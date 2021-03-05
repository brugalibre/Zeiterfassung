/**
 * 
 */
package com.myownb3.dominic.timerecording.core.book.coolguys;

import java.util.List;

import com.coolguys.turbo.Booker;
import com.myownb3.dominic.timerecording.core.book.adapter.BookerAdapter;
import com.myownb3.dominic.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.myownb3.dominic.timerecording.core.book.coolguys.exception.ChargeException;
import com.myownb3.dominic.timerecording.core.book.result.BookerResult;
import com.myownb3.dominic.timerecording.core.importexport.out.businessday.BusinessDayExporter;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;

/**
 * Is responsible for booking the given {@link BusinessDay}.
 * 
 * @author Dominic
 */
public class BookerHelper implements BookerAdapter {

   private ChargeType chargeType;

   public BookerHelper() {
      this.chargeType = new ChargeType();
   }

   @Override
   public ServiceCodeAdapter getServiceCodeAdapter() {
      return chargeType;
   }

   /**
    * Collects from each {@link BusinessDayIncrement} the content to book and calls
    * finally the {@link Booker#bookList(List)}. Additionally all booked
    * {@link BusinessDayIncrement} are flagged as charged
    * 
    * @see Booker#bookList(List)
    */
   @Override
   public BookerResult book(BusinessDay businessDay) {
      List<String> content2Charge = createBookContent(businessDay);
      bookInternal(content2Charge);
      flagBusinessDayAsCharged(businessDay);
      return new BookerHelperResult();
   }

   private void flagBusinessDayAsCharged(BusinessDay businessDay) {
      businessDay.flagBusinessDayAsCharged();
   }

   /*
    * Does the actual charging
    */
   private void bookInternal(List<String> content) {
      try {
         Booker booker = new Booker();
         booker.bookList(content);
      } catch (Exception e) {
         e.printStackTrace();
         throw new ChargeException(e);
      }
   }

   /*
    * Collects the data which has to be charged and exports it into a file which is
    * later used by the Turbo-Bucher
    */
   private List<String> createBookContent(BusinessDay businessDay) {
      synchronized (businessDay) {
         BusinessDayVO businessDayVO = BusinessDayVO.of(businessDay);
         return BusinessDayExporter.INSTANCE.collectContent4TurboBucher(businessDayVO);
      }
   }
}
