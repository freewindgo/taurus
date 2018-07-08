package com.cf.taurus.base.provider;

import com.cf.taurus.base.business.SellBusiness;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SellProvider
 *
 * @author 于文硕
 * @since 2018/5/16 11:46
 */

@Slf4j
@RestController
@RequestMapping("/sell")
public class SellProvider {
    
    @Autowired
    private SellBusiness sellBusiness;

    @GetMapping("/getAllSell")
    public Response getAllSell(){
        try {
            return sellBusiness.getAllSell();
        } catch (Exception e) {
            log.error("getAllSell error, error:{}",e);
            return Response.error();
        }
    }



}
