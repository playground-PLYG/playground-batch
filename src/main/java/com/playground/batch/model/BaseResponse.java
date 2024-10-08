package com.playground.batch.model;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.playground.batch.constants.CommonConstants;
import com.playground.batch.exception.BizException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class BaseResponse<T> extends BaseDto {
  @Serial
  private static final long serialVersionUID = -5362679329439263059L;

  private String result;

  @JsonInclude(Include.NON_NULL)
  private String errorMessage;

  @JsonInclude(Include.NON_NULL)
  private String resultCode;

  private transient T data;

  public BaseResponse() {
    this.result = CommonConstants.SUCCESS;
    this.resultCode = CommonConstants.SUCCESS_CODE;
  }

  public BaseResponse(T data) {
    this.result = CommonConstants.SUCCESS;
    this.resultCode = CommonConstants.SUCCESS_CODE;
    this.data = data;
  }

  public BaseResponse(BizException e) {
    this.result = CommonConstants.FAIL;
    this.resultCode = e.getErrCode().getCode();
    this.errorMessage = e.getMessage();
  }

  public BaseResponse(String errorMessage) {
    this.result = CommonConstants.FAIL;
    this.resultCode = CommonConstants.FAIL_CODE;
    this.errorMessage = errorMessage;
  }
}
