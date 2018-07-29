package com.cf.taurus.base.cron;

import com.cf.taurus.base.business.AdminBusiness;
import com.cf.taurus.base.business.FilmBusiness;
import com.cf.taurus.base.business.HintBusiness;
import com.cf.taurus.common.po.AdminConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


/**
 * CouponTask
 *
 * @author 于文硕
 * @since 2018/5/15 16:06
 */
@Slf4j
@Component
@RestController
public class CronTask {

    @Autowired
    private HintBusiness hintBusiness;

    @Autowired
    private FilmBusiness filmBusiness;

    @Autowired
    private AdminBusiness adminBusiness;

    private final Integer EXPIRE_DAY_CONFIG = 1;
    private final Integer MATCH_TAG_NUM_CONFIG = 2;
    private final Integer MATCH_SWITCH_CONFIG = 3;
    private final Integer SAVE_TAG_NUM_CONFIG = 4;

    private final Integer REAL_DELETE_DAYS_DEFAULT = 14;
    private final Integer MATCH_TAG_NUM_DEFAULT = 2;
    private final Integer SAVE_TAG_NUM_DEFAULT = 20;
    private final Integer MATCH_SWITCH_DEFAULT = 2;
/*    *//**
     * 每天1点清理hint数据库中deleted为2并且创建时间已超过realDeleteDays的数据
     *//*
    @Scheduled(cron = "0 1 0 * * ? ")
    public void deleteExpireData() {
        try {
            AdminConfig adminConfig = adminBusiness.getAdminConfigById(EXPIRE_DAY_CONFIG);
            int realDeleteDays = adminConfig == null ? REAL_DELETE_DAYS_DEFAULT : Integer.valueOf(adminConfig.getValue());
            hintBusiness.deleteExpireData(realDeleteDays);
        } catch (Exception e) {
            log.error("cron deleteExpireData error, e:{}", e);
        }
    }*/

    /**
     * 每天2点开始计算匹配
     */
    @Scheduled(cron = "0 2 0 * * ? ")
    public void getMatchActors(){
        try {
            AdminConfig adminConfig = adminBusiness.getAdminConfigById(MATCH_SWITCH_CONFIG);
            if(adminConfig == null || Integer.valueOf(adminConfig.getValue()) == MATCH_SWITCH_DEFAULT){
                log.info("Auto match actors has already closed");
                return;
            }
            adminConfig = adminBusiness.getAdminConfigById(MATCH_TAG_NUM_CONFIG);
            int countLimit = adminConfig == null ? MATCH_TAG_NUM_DEFAULT : Integer.valueOf(adminConfig.getValue());

            adminConfig = adminBusiness.getAdminConfigById(SAVE_TAG_NUM_CONFIG);
            int saveLimit = adminConfig == null ? SAVE_TAG_NUM_DEFAULT : Integer.valueOf(adminConfig.getValue());

            log.info("Auto match actors start working, match tag number is {}, save actor number is {}", countLimit, saveLimit);

            filmBusiness.getMatchActors(countLimit, saveLimit, null);
        } catch (Exception e) {
            log.error("cron getMatchActors error, e:{}", e);
        }
    }




}

