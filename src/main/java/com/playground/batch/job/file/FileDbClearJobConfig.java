package com.playground.batch.job.file;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.playground.batch.config.CustomRunIdIncrementer;
import com.playground.batch.job.sample.model.TestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = "fileDbClear")
public class FileDbClearJobConfig {
  public static final String JOB_NAME = "fileDbClear";

  @Bean(name = JOB_NAME)
  Job fileDbClearJob(JobRepository jobRepository, Step sampleStep1, Step sampleStep2, Step sampleStep3) {
    return new JobBuilder(JOB_NAME, jobRepository).incrementer(new CustomRunIdIncrementer()).start(sampleStep1).next(sampleStep2).next(sampleStep3)
        .build();
  }

  @Bean
  Step sampleStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder("sampleStep1", jobRepository).tasklet(((contribution, chunkContext) -> RepeatStatus.FINISHED), platformTransactionManager)
        .build();
  }

  @Bean
  Step sampleStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder("sampleStep2", jobRepository).tasklet(((contribution, chunkContext) -> RepeatStatus.FINISHED), platformTransactionManager)
        .build();
  }

  @Bean
  Step sampleStep3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, SqlSessionFactory sqlSessionFactory) {
    return new StepBuilder("sampleStep3", jobRepository).<TestDto, TestDto>chunk(10000, platformTransactionManager).reader(reader(sqlSessionFactory))
        .writer(writer(sqlSessionFactory)).build();
  }

  @Bean
  MyBatisCursorItemReader<TestDto> reader(SqlSessionFactory sqlSessionFactory) {
    return new MyBatisCursorItemReaderBuilder<TestDto>().sqlSessionFactory(sqlSessionFactory).queryId("TestMapper.selectOne").build();
  }

  @Bean
  MyBatisBatchItemWriter<TestDto> writer(SqlSessionFactory sqlSessionFactory) {
    return new MyBatisBatchItemWriterBuilder<TestDto>().sqlSessionFactory(sqlSessionFactory).statementId("TestMapper.insert")
        .itemToParameterConverter(item -> {
          return item;
        }).build();
  }
}
