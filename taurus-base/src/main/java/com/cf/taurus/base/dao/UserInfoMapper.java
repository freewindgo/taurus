package com.cf.taurus.base.dao;

import com.cf.taurus.common.po.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component(value = "userInfoMapper")
@Mapper
public interface UserInfoMapper {
    int deleteByPrimaryKey(Long id);

    long insert(UserInfo record);

    long insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    long updateByPrimaryKeySelective(UserInfo record);

    long updateByPrimaryKey(UserInfo record);

    UserInfo selectByOpenId(String openId);

    long getMaxId();
}