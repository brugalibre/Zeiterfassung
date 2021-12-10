//package com.adcubum.timerecording.core.book.abacus;
//
//import com.adcubum.j2a.zeiterfassung.AbacusBookingConnector;
//import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
//import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDto;
//import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDtoImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//public class AbacusServiceCodeAdapter implements ServiceCodeAdapter {
//
//   private static final Logger LOG = LoggerFactory.getLogger(AbacusServiceCodeAdapter.class);
//   private AbacusBookingConnector abacusBookingConnector;
//   private Map<Integer, String> allServiceCodes;
//
//   AbacusServiceCodeAdapter(AbacusBookingConnector abacusBookingConnector) {
//      this.abacusBookingConnector = abacusBookingConnector;
//      this.allServiceCodes = DefaultServiceCodes.getDefaultServiceCodes();
//   }
//
//   public void init() {
//      try {
//         this.allServiceCodes = abacusBookingConnector.fetchAllServiceCodes();
//      } catch (Exception e) {
//         LOG.error("Error occurred while evaluating all service codes", e);
//      }
//   }
//
//   @Override
//   public List<ServiceCodeDto> fetchServiceCodesForProjectNr(long projectNr) {
//      Map<Integer, String> serviceCodesForProject = abacusBookingConnector.fetchServiceCodesForProject(projectNr);
//      return serviceCodesForProject.keySet()
//             .stream()
//             .map(toServiceCodeDto(serviceCodesForProject))
//             .collect(Collectors.toList());
//   }
//
//    @Override
//    public List<ServiceCodeDto> getAllServiceCodeDescriptions() {
//        return null;
//    }
//
//    private static Function<Integer, ServiceCodeDtoImpl> toServiceCodeDto(Map<Integer, String> serviceCodesForProject) {
//        return serviceCode -> new ServiceCodeDtoImpl(serviceCode, serviceCodesForProject.get(serviceCode));
//    }
//
//    @Override
//    public ServiceCodeDto getServiceCode4Description(String givenServiceCodeDesc) {
//        for (Entry<Integer, String> serviceCode2Desc : allServiceCodes.entrySet()) {
//            if (serviceCode2Desc.getValue().equals(givenServiceCodeDesc)) {
//                return new ServiceCodeDtoImpl(serviceCode2Desc.getKey(), givenServiceCodeDesc);
//            }
//        }
//        return null;
//    }
//}
