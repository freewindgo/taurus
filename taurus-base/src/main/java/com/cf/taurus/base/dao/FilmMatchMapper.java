package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.FilmMatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "filmMatchMapper")
@Mapper
public interface FilmMatchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FilmMatch record);

    int insertSelective(FilmMatch record);

    FilmMatch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FilmMatch record);

    int updateByPrimaryKey(FilmMatch record);

    List<FilmMatch> getAllData();
}