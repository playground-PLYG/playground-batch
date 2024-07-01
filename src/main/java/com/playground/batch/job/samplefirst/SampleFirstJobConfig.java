package com.playground.batch.job.samplefirst;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleFirstJobConfig {
  @Bean
  Job sampleFirstJob1(JobRepository jobRepository, Step sampleStep1, Step sampleStep2, Step sampleStep3) {
    log.debug(">>> sampleFirstJob1");
    return new JobBuilder("sampleFirstJob1", jobRepository).start(sampleStep1).next(sampleStep2).next(sampleStep3).build();
  }

  @Bean
  Step sampleFirstStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    log.debug(">>> sampleStep1");
    return new StepBuilder("sampleFirstStep1", jobRepository).tasklet(((contribution, chunkContext) -> {
      log.debug(">>>>> sampleFirstStep1 - Tasklet");
      return RepeatStatus.FINISHED;
    }), platformTransactionManager).build();
  }

  @Bean
  Step sampleFirstStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    log.debug(">>> sampleFirstStep2");
    return new StepBuilder("sampleFirstStep2", jobRepository).tasklet(((contribution, chunkContext) -> {
      log.debug(">>>>> sampleFirstStep2 - Tasklet");
      return RepeatStatus.FINISHED;
    }), platformTransactionManager).build();
  }

  @Bean
  Step sampleFirstStep3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    log.debug(">>> sampleFirstStep3");
    return new StepBuilder("sampleFirstStep3", jobRepository).tasklet(((contribution, chunkContext) -> {
      log.debug(">>>>> sampleFirstStep3 - Tasklet");
      return RepeatStatus.FINISHED;
    }), platformTransactionManager).build();
  }
}
