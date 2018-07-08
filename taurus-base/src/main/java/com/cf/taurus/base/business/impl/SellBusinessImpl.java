package com.cf.taurus.base.business.impl;

import com.cf.taurus.base.business.SellBusiness;
import com.cf.taurus.base.dao.SellMapper;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Sell;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SellBusinessImpl
 *
 * @author 于文硕
 * @since 2018/5/15 16:13
 */
@Slf4j
@Service
public class SellBusinessImpl implements SellBusiness {

    @Autowired
    private SellMapper sellMapper;

    @Override
    public Response getAllSell() {
        List<Sell> sellList = sellMapper.getAllSell();
        if (EmptyUtils.isEmpty(sellList)) {
            return Response.error(ResponseMessage.NO_DATA);
        }
        return Response.success(sellList);

    }
}
