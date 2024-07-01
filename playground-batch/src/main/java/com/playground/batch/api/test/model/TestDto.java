package com.playground.batch.api.test.model;

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
  private Integer codeSn;

  /**
   * 코드id
   */
  private String codeId;

  /**
   * 코드명
   */
  private String codeNm;

  /**
   * 상위코드id
   */
  private String upperCodeId;

  /**
   * 그룹코드여부
   */
  private String groupCodeAt;

  /**
   * 정렬 순번
   */
  private Integer sortOrdr;

  private String registUsrId;

  private LocalDateTime registDt;

  private String updtUsrId;

  private LocalDateTime updtDt;
}
