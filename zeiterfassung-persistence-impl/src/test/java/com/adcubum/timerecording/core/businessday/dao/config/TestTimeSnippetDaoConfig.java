package com.adcubum.timerecording.core.businessday.dao.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.adcubum.timerecording.core.businessday.comeandgo.dao.ComeAndGoesDao;
import com.adcubum.timerecording.core.businessday.dao.BusinessDayDao;
import com.zaxxer.hikari.HikariConfig;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = {BusinessDayDao.class, ComeAndGoesDao.class})
@EntityScan(basePackages = {"com.adcubum.timerecording.core.businessday"})
@ComponentScan(basePackages = {"com.adcubum.timerecording"})
@TestPropertySource("timesnippet_inmemory_repo_config.properties")
@EnableTransactionManagement
public class TestTimeSnippetDaoConfig extends HikariConfig {
   // no-op
}
