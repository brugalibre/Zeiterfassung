package com.adcubum.timerecording.core.factory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.factory.testimpl.TestImpl;

class StaticFactoryTest {

   @Test
   void testCreateSingleton() {

      // Given
      String beanXml = "test_spring.xml";
      String beanName = "testimpl";

      // When
      TestImpl actualTestImpl = StaticFactory.createNewObject(beanName, beanXml);
      TestImpl actualTestImpl2 = StaticFactory.createNewObject(beanName, beanXml);

      // Then
      assertThat(actualTestImpl, is(not(nullValue())));
      assertThat(actualTestImpl2, is(actualTestImpl2));
   }

}
