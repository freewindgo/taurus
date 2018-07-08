package com.cf.taurus.base.business;


import com.cf.taurus.common.po.Hint;
import com.cf.taurus.common.util.Response;

/**
 * HintBusiness
 *
 * @author 于文硕
 * @since 2018/5/15 16:09
 */
public interface HintBusiness {

    Response saveHint(Hint hint);

    Response getHintById(Integer id);

    Response getHints(Hint hint);

    void deleteExpireData(Integer days);

}
