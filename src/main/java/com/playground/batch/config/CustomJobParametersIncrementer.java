package com.playground.batch.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomJobParametersIncrementer implements JobParametersIncrementer {
  private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss.SSS");

  @Override
  public JobParameters getNext(JobParameters jobParameters) {
    log.debug(">>> jobParameters : {}", jobParameters);
    log.debug(">>> jobParameters : {}", jobParameters.getParameter("run.id") + "_" + format.format(new Date())));
    return new JobParametersBuilder().addString("run.id", jobParameters.getParameter("run.id") + "_" + format.format(new Date())).toJobParameters();
  }
}
