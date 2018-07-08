package com.cf.taurus.base.business.impl;

import com.cf.taurus.base.business.FilmBusiness;
import com.cf.taurus.base.dao.FilmMapper;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Film;
import com.cf.taurus.common.util.CommonUtils;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FilmBusinessImpl
 *
 * @author 于文硕
 * @since 2018/5/16 11:41
 */
@Slf4j
@Service
public class FilmBusinessImpl implements FilmBusiness {
    
    @Autowired
    private FilmMapper filmMapper;

    @Override
    public Response getFilmById(Integer id) {
        Film film = filmMapper.selectByPrimaryKey(id);
        if (film == null) {
            return Response.error(ResponseMessage.NO_DATA);
        }
        return Response.success(film);
    }

    @Override
    public Response getFilms(Film film) {
        List<Film> filmList = filmMapper.selectByCondition(film);
        if (EmptyUtils.isEmpty(filmList)) {
            return Response.error(ResponseMessage.NO_DATA);
        }
        int total = 0;
        if (film.getStart() != null && film.getSize() != null && film.getSize() > 0) {
            total = filmMapper.countByCondition(film);
        }

        if (total != 0) {
            return Response.success(total, filmList);
        }
        return Response.success(filmList);
    }


}
