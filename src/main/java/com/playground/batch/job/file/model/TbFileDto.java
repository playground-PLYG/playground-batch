package com.playground.batch.job.file.model;

import java.io.Serial;
import java.time.LocalDateTime;
import com.playground.batch.model.BaseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class TbFileDto extends BaseDto {
  @Serial
  private static final long serialVersionUID = 7010489220108702398L;

  /**
   * 파일일련번호
   */
  private Integer fileSn;

  /**
   * 원본파일명
   */
  private String orginlFileNm;

  /**
   * 원본파일 확장자
   */
  private String orginlFileExtsnNm;

  /**
   * 저장파일명
   */
  private String streFileNm;

  /**
   * 컨텐츠타입내용
   */
  private String cntntsTyCn;

  /**
   * 파일용량
   */
  private Long fileCpcty;

  /**
   * 파일첫번째속성명
   */
  private String fileOneAtrbNm;

  /**
   * 파일두번째속성명
   */
  private String fileTwoAtrbNm;

  /**
   * 파일세번째속성명
   */
  private String fileThreeAtrbNm;

  private String registUsrId;

  private LocalDateTime registDt;

  private String updtUsrId;

  private LocalDateTime updtDt;
}
