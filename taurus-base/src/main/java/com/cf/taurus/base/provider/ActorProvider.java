package com.cf.taurus.base.provider;

import com.cf.taurus.base.business.ActorBusiness;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Actor;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ActorProvider
 *
 * @author 于文硕
 * @since 2018/5/15 15:04
 */
@Slf4j
@RestController
@RequestMapping("/actor")
public class ActorProvider {

    @Autowired
    private ActorBusiness actorBusiness;

    @GetMapping("/getActorById")
    public Response getActorById(@RequestParam Integer id){
        if(EmptyUtils.isEmpty(id)){
            return Response.error(ResponseMessage.PARAM_ERROR);
        }
        try {
            return actorBusiness.getActorById(id);
        } catch (Exception e) {
            log.error("getActorById error, error:{}",e);
            return Response.error();
        }
    }

    @PostMapping("/getActors")
    public Response getActors(@RequestBody Actor actor){
        try {
            return actorBusiness.getActors(actor);
        } catch (Exception e) {
            log.error("getActors error, error:{}",e);
            return Response.error();
        }
    }


    @GetMapping("/getSkillDetailByName")
    public Response getSkillDetailByName(@RequestParam String skillNames){
        if(EmptyUtils.isEmpty(skillNames)){
            return Response.error(ResponseMessage.PARAM_ERROR);
        }
        try {
            return actorBusiness.getSkillDetailByNames(skillNames);
        } catch (Exception e) {
            log.error("getSkillDetailByName error, error:{}",e);
            return Response.error();
        }
    }

    @GetMapping("/getAllSkill")
    public Response getAllSkill(){
        try {
            return actorBusiness.getAllSkill();
        } catch (Exception e) {
            log.error("getAllSkill error, error:{}",e);
            return Response.error();
        }
    }



}
