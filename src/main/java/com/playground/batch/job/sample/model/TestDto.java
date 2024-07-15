package com.playground.batch.job.sample.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class TestDto {
  private Integer smpleSn;

  private String smpleFirstCn;

  private String smpleSeconCn;

  private String smpleThrdCn;

  private String registUsrId;

  private LocalDateTime registDt;

  private String updtUsrId;

  private LocalDateTime updtDt;
}
