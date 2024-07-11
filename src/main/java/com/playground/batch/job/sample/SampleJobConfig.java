package com.playground.batch.job.sample;

import java.util.Random;
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
import com.playground.batch.api.test.model.TestDto;
import com.playground.batch.config.CustomJobParametersIncrementer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = "sampleJob")
public class SampleJobConfig {
  public static final String JOB_NAME = "sampleJob";
  private Random random = new Random();

  @Bean(name = JOB_NAME)
  Job sampleJob(JobRepository jobRepository, Step sampleStep1, Step sampleStep2, Step sampleStep3) {
    log.info(">>> sampleJob1");
    return new JobBuilder(JOB_NAME, jobRepository).incrementer(new CustomJobParametersIncrementer()).start(sampleStep1).next(sampleStep2)
        .next(sampleStep3).build();
  }

  @Bean
  Step sampleStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    log.debug(">>> sampleStep1");
    return new StepBuilder("sampleStep1", jobRepository).tasklet(((contribution, chunkContext) -> {
      log.debug(">>>>> sampleStep1 - Tasklet");
      return RepeatStatus.FINISHED;
    }), platformTransactionManager).build();
  }

  @Bean
  Step sampleStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    log.debug(">>> sampleStep2");
    return new StepBuilder("sampleStep2", jobRepository).tasklet(((contribution, chunkContext) -> {
      log.debug(">>>>> sampleStep2 - Tasklet");
      return RepeatStatus.FINISHED;
    }), platformTransactionManager).build();
  }

  @Bean
  Step sampleStep3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, SqlSessionFactory sqlSessionFactory) {
    log.debug(">>> sampleStep3");
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
          item.setSmpleSeconCn(String.valueOf(random.nextInt(100)));
          item.setSmpleThrdCn(String.valueOf(random.nextInt(100)));

          return item;
        }).build();
  }
}
