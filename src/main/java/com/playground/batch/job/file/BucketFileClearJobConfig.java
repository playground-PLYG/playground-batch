package com.playground.batch.job.file;

import java.util.ArrayList;
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
import org.springframework.transaction.PlatformTransactionManager;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.common.collect.Maps;
import com.playground.batch.config.CustomRunIdIncrementer;
import com.playground.batch.job.file.dao.FileDao;
import com.playground.batch.job.file.model.TbFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = "bucketFileClear")
public class BucketFileClearJobConfig {
  public static final String JOB_NAME = "bucketFileClear";

  private final FileDao fileDao;
  private final Storage storage;

  @Value("${spring.cloud.gcp.storage.bucket}")
  private String bucketName;

  @Bean(name = JOB_NAME)
  Job bucketFileClear(JobRepository jobRepository, Step bucketFileClearStep1) {
    return new JobBuilder(JOB_NAME, jobRepository).incrementer(new CustomRunIdIncrementer()).start(bucketFileClearStep1).build();
  }

  @Bean
  Step bucketFileClearStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder("bucketFileClearStep1", jobRepository).tasklet(((contribution, chunkContext) -> {
      Page<Blob> blobs = storage.list(bucketName);
      Iterator<Blob> blobIterator = blobs.iterateAll().iterator();

      if (blobIterator.hasNext()) {
        List<TbFileDto> fileList = fileDao.selectAll();
        Map<String, TbFileDto> fileMap = Maps.uniqueIndex(fileList, TbFileDto::getStreFileNm);
        List<BlobId> deleteFileList = new ArrayList<>();

        while (blobIterator.hasNext()) {
          Blob blob = blobIterator.next();
          TbFileDto fileDto = fileMap.get(blob.getName());

          if (fileDto == null) {
            deleteFileList.add(blob.getBlobId());
          }
        }

        if (!deleteFileList.isEmpty()) {
          storage.delete(deleteFileList);
        }

      }

      return RepeatStatus.FINISHED;
    }), platformTransactionManager).build();
  }
}
