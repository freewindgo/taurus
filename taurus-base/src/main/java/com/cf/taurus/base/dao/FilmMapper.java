package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.Film;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "filmMapper")
@Mapper
public interface FilmMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Film record);

    int insertSelective(Film record);

    Film selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Film record);

    int updateByPrimaryKey(Film record);

    List<Film> selectByCondition(Film film);

    int countByCondition(Film film);
}