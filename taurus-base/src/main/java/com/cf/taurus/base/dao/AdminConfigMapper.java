package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.AdminConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component(value = "adminConfigMapper")
@Mapper
public interface AdminConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminConfig record);

    int insertSelective(AdminConfig record);

    AdminConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminConfig record);

    int updateByPrimaryKey(AdminConfig record);
}