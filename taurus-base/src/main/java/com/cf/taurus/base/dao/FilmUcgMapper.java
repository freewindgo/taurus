package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.FilmUcg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component(value = "filmUcgMapper")
@Mapper
public interface FilmUcgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FilmUcg record);

    int insertSelective(FilmUcg record);

    FilmUcg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FilmUcg record);

    int updateByPrimaryKey(FilmUcg record);

    int countFilmUcgByUserIdAndFilmId(@Param("userId") Integer userId, @Param("filmId") Integer filmId);
}