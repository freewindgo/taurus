package com.cf.taurus.base.business.impl;

import com.cf.taurus.base.business.UserBusiness;
import com.cf.taurus.base.dao.ActorMapper;
import com.cf.taurus.base.dao.FilmMapper;
import com.cf.taurus.base.dao.HintMapper;
import com.cf.taurus.base.dao.UserInfoMapper;
import com.cf.taurus.common.enums.CommonEnum;
import com.cf.taurus.common.message.UserMessage;
import com.cf.taurus.common.po.Actor;
import com.cf.taurus.common.po.Film;
import com.cf.taurus.common.po.Hint;
import com.cf.taurus.common.po.UserInfo;
import com.cf.taurus.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UserBusinessImpl
 *
 * @author 于文硕
 * @since 2018/5/11 14:01
 */

@Slf4j
@Service
public class UserBusinessImpl implements UserBusiness {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private FilmMapper filmMapper;

    @Autowired
    private HintMapper hintMapper;

    private final String OPENID_INTERFACE = "https://api.weixin.qq.com/sns/jscode2session";
    private final String APP_ID = "wx0c75033acddfca71";
    private final String APP_SECRET = "44fd872f6fa5a65ee422b8530f5a2ccb";

    @Transactional
    @Override
    public Response saveUserInfo(UserInfo userInfo) {
        //id为空说明为新用户登陆，或者页面缓存已失效
        if (EmptyUtils.isEmpty(userInfo.getId())){

            String openId = this.getOpenIdFromWx(userInfo.getCode());
            if(EmptyUtils.isEmpty(openId)){
                return Response.error(UserMessage.GET_OPENID_FAILED);
            }
            UserInfo userInfoFromDB = userInfoMapper.selectByOpenId(openId);

            //不为空说明是页面缓存失效,返回用户数据
            if(userInfoFromDB != null){
                return Response.success(userInfoFromDB);
            }

            //为空则进行新增数据
            userInfo.setOpenId(openId);
            userInfo.setCtime(DateUtils.formatDate(DateUtils.FORMAT_DEFAULT, new Date()));
            userInfo.setUtime(userInfo.getCtime());
            userInfo.setIsDelete(CommonEnum.NORMAL.getCode());

            //将记录得id进行赋值并返回
            Long id = userInfoMapper.insertSelective(userInfo);
            userInfo.setId(id);

        } else {
            userInfo.setUtime(DateUtils.formatDate(DateUtils.FORMAT_DEFAULT, new Date()));
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        }

        return Response.success(userInfo);
    }

    @Override
    public Response getUserInfoById(Long userId) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if (userInfo == null) {
            return Response.error(UserMessage.NO_USER);
        }
        return Response.success(userInfo);
    }

    @Override
    public Response checkPassword(Long userId, String password) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if (userInfo == null) {
            return Response.error(UserMessage.NO_USER);
        }
        if (!password.equals(userInfo.getPassword())) {
            return Response.success(false);
        }
        return Response.success(true);
    }

    @Override
    public Response getSummary() {
        int actorCount = actorMapper.countByCondition(new Actor());
        int filmCount = filmMapper.countByCondition(new Film());
        int hintCount = hintMapper.countByCondition(new Hint());
        long userCount = userInfoMapper.getMaxId();
        Map resMap = new HashMap();
        resMap.put("actorCount",actorCount);
        resMap.put("filmCount",filmCount);
        resMap.put("hintCount",hintCount);
        resMap.put("userCount",userCount);
        return Response.success(resMap);
    }

    /**
     * 根据code从微信后台获取openId
     * @param code
     * @return
     */
    private String getOpenIdFromWx(String code){

        Map<String, String> param = new HashMap();
        param.put("appid",APP_ID);
        param.put("secret", APP_SECRET);
        param.put("js_code", code);
        param.put("grant_type","authorization_code");
        String resultStr = HttpClientUtils.doGet(OPENID_INTERFACE, param);

        log.info("UserBusinessImpl.getOpenIdFromWx, code is {}, result is {}", code, resultStr);
        Map<String, Object> resultMap = JsonUtils.parseMap(resultStr);
        if(EmptyUtils.isEmpty(resultMap) || EmptyUtils.isEmpty(resultMap.get("openid"))){
            return null;
        }

        return resultMap.get("openid").toString();
    }


}
