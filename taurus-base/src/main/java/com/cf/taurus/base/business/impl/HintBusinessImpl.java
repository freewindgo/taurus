package com.cf.taurus.base.business.impl;

import com.cf.taurus.base.business.HintBusiness;
import com.cf.taurus.base.dao.HintMapper;
import com.cf.taurus.common.enums.CommonEnum;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Hint;
import com.cf.taurus.common.util.CommonUtils;
import com.cf.taurus.common.util.DateUtils;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * HintBusinessImpl
 *
 * @author 于文硕
 * @since 2018/5/16 11:41
 */
@Slf4j
@Service
public class HintBusinessImpl implements HintBusiness {
    
    @Autowired
    private HintMapper hintMapper;

    @Override
    public Response getHintById(Integer id) {
        Hint hint = hintMapper.selectByPrimaryKey(id);
        if (hint == null) {
            return Response.error(ResponseMessage.NO_DATA);
        }
        return Response.success(hint);
    }

    @Override
    public Response saveHint(Hint hint) {
        hint.setCtime(DateUtils.formatDate(DateUtils.FORMAT_DEFAULT, new Date()));
        //新录入数据设置为删除，需审核后可显示
        hint.setDeleted(2);
        hintMapper.insertSelective(hint);

        return Response.success();
    }

    @Override
    public Response getHints(Hint hint) {
        hint.setStart(CommonUtils.getPageStart(hint.getStart(), hint.getSize()));
        List<Hint> hintList = hintMapper.selectByCondition(hint);
        if (EmptyUtils.isEmpty(hintList)) {
            return Response.error(ResponseMessage.NO_DATA);
        }
        int total = 0;
        if (hint.getStart() != null && hint.getSize() != null && hint.getSize() > 0) {
            total = hintMapper.countByCondition(hint);
        }

        if (total != 0) {
            return Response.success(total, hintList);
        }
        return Response.success(hintList);
    }

    @Override
    public void deleteExpireData(Integer days) {
        String dateCondition = DateUtils.calDate(DateUtils.FORMAT_DEFAULT, new Date(), days);
        hintMapper.deleteExpireData(dateCondition);
    }
}
