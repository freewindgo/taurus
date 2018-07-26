package com.cf.taurus.base.business;

import com.cf.taurus.common.po.Film;
import com.cf.taurus.common.po.FilmUcg;
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

    Response getFilmMatch(Integer filmId);

    Response getFilmUcgLimit(Integer userId, Integer filmId);

    Response saveFilmUcg(FilmUcg filmUcg);

    void getMatchActors(Integer countLimit, Integer saveLimit, Integer targetId);

    /**
     * 数据迁移调用方法，用于生成初始的filmType数据
     */
    void insertFilmTagToMatch();

}
