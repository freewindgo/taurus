package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.Sell;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "sellMapper")
@Mapper
public interface SellMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sell record);

    int insertSelective(Sell record);

    Sell selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sell record);

    int updateByPrimaryKey(Sell record);

    List<Sell> getAllSell();
}