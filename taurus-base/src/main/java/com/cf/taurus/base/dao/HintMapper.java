package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.Hint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "hintMapper")
@Mapper
public interface HintMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Hint record);

    int insertSelective(Hint record);

    Hint selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hint record);

    int updateByPrimaryKey(Hint record);

    List<Hint> selectByCondition(Hint hint);

    int countByCondition(Hint hint);

    int deleteExpireData(@Param("dateCondition") String dateCondition);
}