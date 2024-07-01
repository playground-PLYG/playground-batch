package com.playground.batch.api.test.controller;


import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.playground.batch.api.test.model.TestDto;
import com.playground.batch.api.test.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/playground")
public class TestController {
  private final TestService codeService;

  /**
   * 전체 코드 조회
   */
  @GetMapping("/public/code/getAllCodeList")
  public List<TestDto> getAllCodeList() {
    return codeService.getAllCodeList();
  }

  /**
   * 전체 코드 조회
   */
  @GetMapping("/public/code/getCodeList")
  public List<TestDto> getCodeList(@RequestParam String codeSn) {
    return codeService.getCodeList(codeSn);
  }
}


