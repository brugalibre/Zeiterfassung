package com.adcubum.timerecording.integtest.repo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.adcubum.timerecording.core.businessday.dao.BusinessDayDao;
import com.zaxxer.hikari.HikariConfig;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = {BusinessDayDao.class})
@EntityScan(basePackages = {"com.adcubum.timerecording.core.businessday.entity"})
@ComponentScan(basePackages = {"com.adcubum.timerecording"})
@TestPropertySource("businessday_inmemory_repo_config.properties")
@EnableTransactionManagement
public class TestBusinessDayRepoConfig extends HikariConfig {
   // no-op
}
