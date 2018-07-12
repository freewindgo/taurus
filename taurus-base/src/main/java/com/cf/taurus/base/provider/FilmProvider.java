package com.cf.taurus.base.provider;

import com.cf.taurus.base.business.FilmBusiness;
import com.cf.taurus.base.interceptor.TimeCost;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Film;
import com.cf.taurus.common.util.DateUtils;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * FilmProvider
 *
 * @author 于文硕
 * @since 2018/5/16 11:46
 */

@Slf4j
@RestController
@RequestMapping("/film")
public class FilmProvider {
    
    @Autowired
    private FilmBusiness filmBusiness;

    @GetMapping("/getFilmById")
    public Response getFilmById(@RequestParam Integer id){
        if(EmptyUtils.isEmpty(id)){
            return Response.error(ResponseMessage.PARAM_ERROR);
        }
        try {
            return filmBusiness.getFilmById(id);
        } catch (Exception e) {
            log.error("getFilmById error, error:{}",e);
            return Response.error();
        }
    }

    @PostMapping("/getFilms")
    public Response getFilms(@RequestBody Film film){
        try {
            return filmBusiness.getFilms(film);
        } catch (Exception e) {
            log.error("getFilms error, error:{}",e);
            return Response.error();
        }
    }

    /**
     * 手动调用方法,迁移film数据进film_match
     * @return
     */
    @GetMapping("/transData")
    @TimeCost
    public Response transFilmDataToFilmMatch(String password){
        try {
            if(!this.checkPassword(password)){
                return Response.error(ResponseMessage.PARAM_ERROR);
            }
            filmBusiness.insertFilmTagToMatch();
        } catch (Exception e) {
            log.error("getFilms error, error:{}",e);
            return Response.error();
        }
        return Response.success();
    }

    /**
     * 手动调用方法,匹配数据
     * @return
     */
    @GetMapping("/matchActorData")
    @TimeCost
    public Response matchActorData(@RequestParam("password") String password, @RequestParam("countLimit") Integer countLimit){
        try {
            if(!this.checkPassword(password)){
                return Response.error(ResponseMessage.PARAM_ERROR);
            }
            filmBusiness.getMatchActors(countLimit);
        } catch (Exception e) {
            log.error("getFilms error, error:{}",e);
            return Response.error();
        }

        return Response.success();
    }

    private Boolean checkPassword(String password){
        String today = DateUtils.formatDate(DateUtils.FORMAT_FOR_DAY, new Date());
        return today.equals(password);
    }


}
