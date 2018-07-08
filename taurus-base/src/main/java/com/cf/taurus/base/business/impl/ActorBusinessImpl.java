package com.cf.taurus.base.business.impl;

import com.cf.taurus.base.business.ActorBusiness;
import com.cf.taurus.base.dao.ActorMapper;
import com.cf.taurus.base.dao.SkillMapper;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Actor;
import com.cf.taurus.common.po.Skill;
import com.cf.taurus.common.util.CommonUtils;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ActorBusinessImpl
 *
 * @author 于文硕
 * @since 2018/5/15 14:47
 */

@Slf4j
@Service
public class ActorBusinessImpl implements ActorBusiness {

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private SkillMapper skillMapper;

    @Override
    public Response getActorById(Integer id) {
        Actor actor = actorMapper.selectByPrimaryKey(id);
        if(actor == null){
            return Response.error(ResponseMessage.NO_DATA);
        }
        return Response.success(actor);
    }

    @Override
    public Response getActors(Actor actor) {
        actor.setStart(CommonUtils.getPageStart(actor.getStart(), actor.getSize()));
        List<Actor> actorList = actorMapper.selectByCondition(actor);
        if(EmptyUtils.isEmpty(actorList)){
            return Response.error(ResponseMessage.NO_DATA);
        }

        int total = 0;
        if (actor.getStart() != null && actor.getSize() != null && actor.getSize() > 0) {
            total = actorMapper.countByCondition(actor);
        }

        if (total != 0) {
            return Response.success(total, actorList);
        }
        return Response.success(actorList);
    }


    @Override
    public Response getSkillDetailByNames(String skillNames) {
        List<Skill> skillList = skillMapper.selectByNames(skillNames.split(","));
        if(EmptyUtils.isEmpty(skillList)){
            return Response.error(ResponseMessage.NO_DATA);
        }
        return Response.success(skillList);
    }

    @Override
    public Response getAllSkill() {
        List<Skill> skillList = skillMapper.selectAll();
        if(EmptyUtils.isEmpty(skillList)){
            return Response.error(ResponseMessage.NO_DATA);
        }
        return Response.success(skillList);
    }
}
