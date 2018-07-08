package com.cf.taurus.base.provider;

import com.cf.taurus.base.business.FilmBusiness;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Film;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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



}
