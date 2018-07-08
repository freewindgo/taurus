package com.cf.taurus.base.cron;

import com.cf.taurus.base.business.HintBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;


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

    private static final Integer REAL_DELETE_DAYS = 14;

    /**
     * 每天1点清理hint数据库中deleted为2并且创建时间已超过REAL_DELETE_DAYS的数据
     */
    @Scheduled(cron = "0 1 0 * * ? ")
    public void deleteExpireData() {
        try {
            hintBusiness.deleteExpireData(REAL_DELETE_DAYS);
        } catch (Exception e) {
            log.error("cron deleteExpireData error, e:{}", e);
        }
    }




}

