package com.cf.taurus.base.business;

import com.cf.taurus.common.po.Film;
import com.cf.taurus.common.util.Response;

/**
 * FilmBusiness
 *
 * @author 于文硕
 * @since 2018/5/16 11:40
 */
public interface FilmBusiness {

    Response getFilmById(Integer id);

    Response getFilms(Film film);

}
