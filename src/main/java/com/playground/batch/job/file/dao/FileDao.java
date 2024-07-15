package com.playground.batch.job.file.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.playground.batch.job.file.model.TbFileDto;

@Mapper
public interface FileDao {
  List<TbFileDto> selectAll();

  int deleteInFileSn(List<Integer> fileSnList);
}
