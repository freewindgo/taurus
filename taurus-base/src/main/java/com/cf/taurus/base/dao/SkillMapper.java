package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.Skill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "skillMapper")
@Mapper
public interface SkillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Skill record);

    int insertSelective(Skill record);

    Skill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Skill record);

    int updateByPrimaryKey(Skill record);

    List<Skill> selectByNames(@Param("skillNames") String[] skillNames);

    List<Skill> selectAll();

}