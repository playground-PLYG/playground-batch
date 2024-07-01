package com.playground.batch.api.test.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.playground.batch.api.test.model.TestDto;

@Mapper
public interface TestMapper {
  List<TestDto> selectAll();

  List<TestDto> selectByCodeId(String codeSn);
}
