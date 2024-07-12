package com.playground.batch.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRunIdIncrementer extends RunIdIncrementer {
  private static final String RUN_ID_KEY = "run.id";
  private static final String RUN_BUILD_ID_KEY = "run.build_id";
  private static final String RUN_PROFILE_KEY = "run.profile";
  private static final String RUN_DATE_KEY = "run.date";

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss.SSS");

  @Override
  public JobParameters getNext(@Nullable JobParameters parameters) {
    JobParameters params = (parameters == null) ? new JobParameters() : parameters;

    String profile = System.getProperty("spring.profiles.active");

    if (StringUtils.isBlank(profile)) {
      profile = "local";
    }

    Date now = new Date();
    String runDate = dateFormat.format(now);

    JobParameter<?> buildIdParameter = params.getParameters().get(CustomRunIdIncrementer.RUN_BUILD_ID_KEY);

    String buildId = "";
    String runId = profile;

    if (buildIdParameter != null) {
      buildId = String.valueOf(buildIdParameter.getValue());

      if (StringUtils.isNotBlank(buildId)) {
        runId += "_" + buildId;
      }
    }

    runId += "_" + runDate;

    return new JobParametersBuilder(params).addString(CustomRunIdIncrementer.RUN_ID_KEY, runId)
        .addString(CustomRunIdIncrementer.RUN_BUILD_ID_KEY, buildId, false).addString(CustomRunIdIncrementer.RUN_PROFILE_KEY, profile, false)
        .addDate(CustomRunIdIncrementer.RUN_DATE_KEY, now).toJobParameters();
  }
}
