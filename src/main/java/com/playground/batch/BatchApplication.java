package com.playground.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import com.playground.batch.config.BatchConfig;

@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
@Import({BatchConfig.class})
public class BatchApplication {
  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplicationBuilder(BatchApplication.class).web(WebApplicationType.NONE).build();

    springApplication.run(args);
  }
}
