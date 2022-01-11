package com.adcubum.timerecording.core.book.proles;

import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDto;

import java.util.Collections;
import java.util.List;

public class ProlesServiceCodeAdapter implements ServiceCodeAdapter {
   @Override
   public List<ServiceCodeDto> fetchServiceCodesForProjectNr(long projectNr) {
      return Collections.emptyList();
   }

   @Override
   public List<ServiceCodeDto> getAllServiceCodeDescriptions() {
      return Collections.emptyList();
   }

   @Override
   public ServiceCodeDto getServiceCode4Code(Integer serviceCode) {
      return null;
   }
}
