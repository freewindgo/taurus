package com.cf.taurus.base.provider;

import com.cf.taurus.base.business.HintBusiness;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Hint;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * HintProvider
 *
 * @author 于文硕
 * @since 2018/5/15 16:00
 */
@Slf4j
@RestController
@RequestMapping("/hint")
public class HintProvider {
    @Autowired
    private HintBusiness hintBusiness;

    /**
     * 按id获取hint
     *
     * @param id
     * @return
     */
    @GetMapping("/getHintById")
    public Response getHintById(@RequestParam Integer id) {
        if (EmptyUtils.isEmpty(id)) {
            return Response.error(ResponseMessage.PARAM_ERROR);
        }
        try {
            return hintBusiness.getHintById(id);
        } catch (Exception e) {
            log.error("getHintById error, error:{}", e);
            return Response.error();
        }
    }

    /**
     * 获取hintInfo列表 分页
     *
     * @param hint
     * @return
     */
    @PostMapping("/getHints")
    public Response getHints(@RequestBody Hint hint) {
        try {
            return hintBusiness.getHints(hint);
        } catch (Exception e) {
            log.error("getHints error, error:{}", e);
            return Response.error();
        }
    }

    /**
     * 保存hint
     *
     * @param hint
     * @return
     */
    @PostMapping("/saveHint")
    public Response saveHint(@RequestBody Hint hint) {
        try {
            return hintBusiness.saveHint(hint);
        } catch (Exception e) {
            log.error("saveHint error, error:{}", e);
            return Response.error();
        }
    }


}
