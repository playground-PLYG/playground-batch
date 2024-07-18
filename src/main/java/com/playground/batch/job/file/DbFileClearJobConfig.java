package com.playground.batch.job.file;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.CollectionUtils;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.playground.batch.config.CustomRunIdIncrementer;
import com.playground.batch.job.file.dao.FileDao;
import com.playground.batch.job.file.model.TbFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = "dbFileClear")
public class DbFileClearJobConfig {
  public static final String JOB_NAME = "dbFileClear";

  private final FileDao fileDao;
  private final Storage storage;
  private final RedisTemplate<String, Object> redisTemplate;
  private static final String TEMPLATE_KEY = "images::";

  @Value("${spring.cloud.gcp.storage.bucket}")
  private String bucketName;

  @Bean(name = JOB_NAME)
  Job dbFileClear(JobRepository jobRepository, Step dbFileClearStep1) {
    return new JobBuilder(JOB_NAME, jobRepository).incrementer(new CustomRunIdIncrementer()).start(dbFileClearStep1).build();
  }

  @Bean
  Step dbFileClearStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder("dbFileClearStep1", jobRepository).tasklet(((contribution, chunkContext) -> {
      List<TbFileDto> fileList = fileDao.selectAll();

      if (!CollectionUtils.isEmpty(fileList)) {
        Page<Blob> blobs = storage.list(bucketName);
        Iterator<Blob> blobIterator = blobs.iterateAll().iterator();

        if (blobIterator.hasNext()) {
          Map<String, Blob> fileMap = new HashMap<>();

          while (blobIterator.hasNext()) {
            Blob blob = blobIterator.next();

            fileMap.put(blob.getName(), blob);
          }

          List<Integer> deleteFileList =
              fileList.stream().filter(fileDto -> fileMap.get(fileDto.getStreFileNm()) == null).map(TbFileDto::getFileSn).toList();

          if (!CollectionUtils.isEmpty(deleteFileList)) {
            int resultCnt = fileDao.deleteInFileSn(deleteFileList);

            deleteFileList.forEach(key -> redisTemplate.delete(TEMPLATE_KEY + key));

            log.debug(">>> resultCnt : {}", resultCnt);
          }
        }
      }

      return RepeatStatus.FINISHED;
    }), platformTransactionManager).build();
  }
}
