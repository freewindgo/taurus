package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.AdminNotice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component(value = "adminNoticeMapper")
@Mapper
public interface AdminNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminNotice record);

    int insertSelective(AdminNotice record);

    AdminNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminNotice record);

    int updateByPrimaryKey(AdminNotice record);
}