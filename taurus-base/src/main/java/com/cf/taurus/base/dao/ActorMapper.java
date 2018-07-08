package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.Actor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "actorMapper")
@Mapper
public interface ActorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Actor record);

    int insertSelective(Actor record);

    Actor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Actor record);

    int updateByPrimaryKey(Actor record);

    List<Actor> selectByCondition(Actor actor);

    int countByCondition(Actor actor);
}