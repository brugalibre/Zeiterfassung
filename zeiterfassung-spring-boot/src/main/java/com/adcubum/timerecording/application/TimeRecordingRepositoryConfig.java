package com.adcubum.timerecording.application;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.adcubum.timerecording.core.businessday.dao.BusinessDayDao;

@Configuration
@EnableJpaRepositories(basePackageClasses = {BusinessDayDao.class})
@EntityScan(basePackages = {"com.adcubum.timerecording.core.businessday.entity"})
@ComponentScan(basePackages = {"com.adcubum.timerecording"})
public class TimeRecordingRepositoryConfig {
   // no-op
}
