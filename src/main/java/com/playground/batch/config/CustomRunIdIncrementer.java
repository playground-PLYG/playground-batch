package com.playground.batch.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh:mm:ss.SSS");

  @Override
  public JobParameters getNext(@Nullable JobParameters parameters) {
    JobParameters params = (parameters == null) ? new JobParameters() : parameters;

    JobParameter<?> profileParameter = params.getParameter(CustomRunIdIncrementer.RUN_PROFILE_KEY);
    String profile = "local";

    if (profileParameter != null && profileParameter.getValue() instanceof String profileParam && StringUtils.isNotBlank(profileParam)) {
      profile = profileParam;
    }

    LocalDateTime now = LocalDateTime.now();
    String runDate = now.format(dateTimeFormat);

    JobParameter<?> buildIdParameter = params.getParameter(CustomRunIdIncrementer.RUN_BUILD_ID_KEY);

    Long buildId = -1L;
    String runId = profile;

    if (buildIdParameter != null && buildIdParameter.getValue() instanceof Long buildIdParam) {
      buildId = buildIdParam;

      if (buildIdParam > 0) {
        runId += "_" + buildId;
      }
    }

    runId += "_" + runDate;

    return new JobParametersBuilder(params).addString(CustomRunIdIncrementer.RUN_ID_KEY, runId)
        .addLong(CustomRunIdIncrementer.RUN_BUILD_ID_KEY, buildId).addString(CustomRunIdIncrementer.RUN_PROFILE_KEY, profile)
        .addLocalDateTime(CustomRunIdIncrementer.RUN_DATE_KEY, now).toJobParameters();
  }
}
