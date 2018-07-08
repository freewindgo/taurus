package com.cf.taurus.base.provider;

import com.cf.taurus.base.business.UserBusiness;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.UserInfo;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * UserProvider
 *
 * @author 于文硕
 * @since 2018/5/11 13:48
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserProvider {

    @Autowired
    private UserBusiness userBusiness;


    @GetMapping("/getUserInfoById")
    public Response getUserInfoById(@RequestParam Long id){
        if(EmptyUtils.isEmpty(id)){
            return Response.error(ResponseMessage.PARAM_ERROR);
        }
        try {
            return userBusiness.getUserInfoById(id);
        } catch (Exception e) {
            log.error("getUserInfoById error, error:{}",e);
            return Response.error();
        }
    }

    /**
     * 1、新用户登陆时存储用户信息  db is null，id is null
     * 2、用户缓存清空时获取缓存    db is not null，id is null
     * 3、更新用户信息             id is not null
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/saveUserInfo")
    public Response saveUserInfo(@RequestBody UserInfo userInfo){
        if(EmptyUtils.isEmpty(userInfo.getCode())){
            return Response.error(ResponseMessage.PARAM_ERROR);
        }
        try {
            return userBusiness.saveUserInfo(userInfo);
        } catch (Exception e) {
            log.error("saveUserInfo error, error:{}",e);
            return Response.error();
        }
    }

    @GetMapping("/checkPassword")
    public Response checkPassword(@RequestParam Long id, @RequestParam String password){
        if(EmptyUtils.isEmpty(id) && EmptyUtils.isEmpty(password)){
            return Response.error(ResponseMessage.PARAM_ERROR);
        }
        try {
            return userBusiness.checkPassword(id,password);
        } catch (Exception e) {
            log.error("checkPassword error, error:{}",e);
            return Response.error();
        }
    }

    @GetMapping("/getSummary")
    public Response getSummary(){
        try {
            return userBusiness.getSummary();
        } catch (Exception e) {
            log.error("getSummary error, error:{}",e);
            return Response.error();
        }
    }

}
