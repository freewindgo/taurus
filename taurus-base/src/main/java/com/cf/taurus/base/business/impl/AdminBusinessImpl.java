package com.cf.taurus.base.business.impl;

import com.cf.taurus.base.business.AdminBusiness;
import com.cf.taurus.base.dao.AdminConfigMapper;
import com.cf.taurus.common.po.Actor;
import com.cf.taurus.common.po.AdminConfig;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AdminBusinessImpl
 *
 * @author 于文硕
 * @since 2018/5/15 14:47
 */

@Slf4j
@Service
public class AdminBusinessImpl implements AdminBusiness {

    @Autowired
    private AdminConfigMapper adminConfigMapper;

    @Override
    public AdminConfig getAdminConfigById(Integer id) {
        return adminConfigMapper.selectByPrimaryKey(id);
    }
}
