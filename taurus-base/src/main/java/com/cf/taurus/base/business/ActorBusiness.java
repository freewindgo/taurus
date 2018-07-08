package com.cf.taurus.base.business;

import com.cf.taurus.common.po.Actor;
import com.cf.taurus.common.util.Response;

/**
 * ActorBusiness
 *
 * @author 于文硕
 * @since 2018/5/15 14:43
 */
public interface ActorBusiness {

    Response getActorById(Integer id);

    Response getActors(Actor actor);

    Response getSkillDetailByNames(String skillNames);

    Response getAllSkill();

}
