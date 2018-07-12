package com.cf.taurus.base.business;

import com.cf.taurus.common.po.Actor;
import com.cf.taurus.common.po.AdminConfig;
import com.cf.taurus.common.util.Response;

/**
 * AdminBusiness
 *
 * @author 于文硕
 * @since 2018/5/15 14:43
 */
public interface AdminBusiness {

    AdminConfig getAdminConfigById(Integer id);
}
