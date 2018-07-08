package com.cf.taurus.base.business;

import com.cf.taurus.common.po.UserInfo;
import com.cf.taurus.common.util.Response;

/**
 * UserBusiness
 *
 * @author 于文硕
 * @since 2018/5/11 13:49
 */

public interface UserBusiness {

    Response saveUserInfo(UserInfo userInfo);

    Response getUserInfoById(Long userId);

    Response checkPassword(Long userId, String password);

    Response getSummary();

}
