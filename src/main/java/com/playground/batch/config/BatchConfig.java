package com.playground.batch.config;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(BatchProperties.class)
public class BatchConfig {
  @Value("${spring.datasource.hikari.driver-class-name}")
  private String dbDriverClassName;

  @Value("${spring.datasource.hikari.username}")
  private String dbUser;

  @Value("${sm://db-pwd}")
  private String dbPwd;

  @Value("${sm://db-host}")
  private String dbHost;

  @Value("${spring.profiles.active}")
  private String activeProfile;
  /*
   * @Bean static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() { return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor"); }
   *
   * @Bean BeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) { JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor(); postProcessor.setJobRegistry(jobRegistry); return postProcessor; }
   */

  // batchDatasource 사용을 위한 수동 빈 등록
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
  JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository,
      BatchProperties properties, ApplicationContext context) {
    JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
    String jobNames = properties.getJob().getName();

    Map<String, Object> beans = context.getBeansOfType(Object.class);

    for (String beanName : beans.keySet()) {
      log.debug(beanName);
    }

    if (StringUtils.hasText(jobNames)) {
      runner.setJobName(jobNames);
    }

    return runner;
  }

  // batchDatasource 사용을 위한 수동 빈 등록
  @Bean
  @ConditionalOnMissingBean(BatchDataSourceScriptDatabaseInitializer.class)
  BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(DataSource dataSource,
      @BatchDataSource ObjectProvider<DataSource> batchDataSource, BatchProperties properties) {
    return new BatchDataSourceScriptDatabaseInitializer(batchDataSource.getIfAvailable(() -> dataSource), properties.getJdbc());
  }

  @BatchDataSource
  @Bean("batchDataSource")
  DataSource batchDataSource() {
    String hostUrl = "jdbc:";

    if ("local".equals(activeProfile)) {
      hostUrl += "log4jdbc:";
    }

    hostUrl += "postgresql://" + dbHost + ":5432/playground";

    return DataSourceBuilder.create().type(HikariDataSource.class).password(dbPwd).url(hostUrl).driverClassName(dbDriverClassName).username(dbUser)
        .build();
  }

  @Bean
  PlatformTransactionManager batchTransactionManager(@Qualifier("batchDataSource") DataSource batchDataSource) {
    return new DataSourceTransactionManager(batchDataSource);
  }

}
