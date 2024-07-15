package com.playground.batch.config;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRunIdIncrementer extends RunIdIncrementer {
  private static final String RUN_ID_KEY = "run.id";

  @Override
  public JobParameters getNext(@Nullable JobParameters parameters) {
    JobParameters params = (parameters == null) ? new JobParameters() : parameters;
    JobParameter<?> runIdParameter = params.getParameters().get(CustomRunIdIncrementer.RUN_ID_KEY);
    long id = 1;
    if (runIdParameter != null) {
      try {
        id = Long.parseLong(runIdParameter.getValue().toString()) + 1;
      } catch (NumberFormatException exception) {
        throw new IllegalArgumentException("Invalid value for parameter " + CustomRunIdIncrementer.RUN_ID_KEY, exception);
      }
    }
    return new JobParametersBuilder(params).addLong(CustomRunIdIncrementer.RUN_ID_KEY, id).toJobParameters();
  }
}
