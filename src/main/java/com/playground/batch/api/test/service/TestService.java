package com.playground.batch.api.test.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.playground.batch.api.test.mapper.TestMapper;
import com.playground.batch.api.test.model.TestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
  private final TestMapper testMapper;


  @Transactional(readOnly = true)
  public List<TestDto> getAllCodeList() {
    return testMapper.selectAll();
  }

  @Transactional(readOnly = true)
  public List<TestDto> getCodeList(String codeSn) {
    return testMapper.selectByCodeId(codeSn);
  }

}
