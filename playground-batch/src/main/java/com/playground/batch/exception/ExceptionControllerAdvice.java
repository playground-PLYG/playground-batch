package com.playground.batch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.playground.batch.model.BaseResponse;

@RestControllerAdvice
public class ExceptionControllerAdvice {
  @ExceptionHandler(SecurityException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse<Void> signatureException(Exception e) {
    return new BaseResponse<>("SignatureException");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse<Void> methodArgumentValidException(MethodArgumentNotValidException e) {
    ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().orElse(new ObjectError("", "파라메터가 올바르지 않습니다."));

    return new BaseResponse<>(objectError.getDefaultMessage());
  }

  @ExceptionHandler(BizException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BaseResponse<Void> bizException(BizException e) {
    return new BaseResponse<>(e);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BaseResponse<Void> internalServerErrorException(Exception e) {
    return new BaseResponse<>("InternalServerError");
  }
}
