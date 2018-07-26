package com.cf.taurus.base.business.impl;

import com.cf.taurus.base.business.FilmBusiness;
import com.cf.taurus.base.dao.ActorMapper;
import com.cf.taurus.base.dao.FilmMapper;
import com.cf.taurus.base.dao.FilmMatchMapper;
import com.cf.taurus.base.dao.FilmUcgMapper;
import com.cf.taurus.common.message.ResponseMessage;
import com.cf.taurus.common.po.Actor;
import com.cf.taurus.common.po.Film;
import com.cf.taurus.common.po.FilmMatch;
import com.cf.taurus.common.po.FilmUcg;
import com.cf.taurus.common.util.CommonUtils;
import com.cf.taurus.common.util.DateUtils;
import com.cf.taurus.common.util.EmptyUtils;
import com.cf.taurus.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private FilmMatchMapper filmMatchMapper;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private FilmUcgMapper filmUcgMapper;

    //默认每个用户对每个影片最多提供10条ucg内容
    private final int DEFAULT_UCG_LIMIT = 10;

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

    @Override
    public Response getFilmMatch(Integer filmId) {
        List<FilmMatch> filmMatchList = filmMatchMapper.getByFilmId(filmId);
        if (EmptyUtils.isEmpty(filmMatchList)) {
            return Response.error(ResponseMessage.NO_DATA);
        }
        return Response.success(filmMatchList);
    }

    @Override
    public Response getFilmUcgLimit(Integer userId, Integer filmId) {
        int count = filmUcgMapper.countFilmUcgByUserIdAndFilmId(userId, filmId);
        if(count > DEFAULT_UCG_LIMIT){
            return Response.success(false);
        }
        return Response.success(true);
    }

    @Transactional
    @Override
    public Response saveFilmUcg(FilmUcg filmUcg) {
        filmUcg.setCtime(DateUtils.formatDate(DateUtils.FORMAT_DEFAULT, new Date()));
        filmUcgMapper.insertSelective(filmUcg);
        return Response.success();
    }

    @Transactional
    @Override
    public void insertFilmTagToMatch() {
        List<Film> allFilmList = filmMapper.selectByCondition(new Film());
        for (Film film : allFilmList) {
            Integer actorNum = film.getActorNum();
            if (EmptyUtils.isEmpty(actorNum)) {
                log.info("Film actorNum has no value, film is {}", film.getId());
                continue;
            }
            for (int i = 0; i < actorNum; i++) {
                FilmMatch filmMatch = this.generateFilmMatch(film, i);
                filmMatchMapper.insertSelective(filmMatch);
            }
        }

    }

    @Override
    public void getMatchActors(Integer countLimit, Integer saveLimit, Integer targetId) {
        List<FilmMatch> filmMatchList;
        if (targetId == null || targetId.equals(-1)) {
            filmMatchList = filmMatchMapper.getAllData();
        } else {
            filmMatchList = filmMatchMapper.getByFilmId(targetId);
        }


        for (FilmMatch filmMatch : filmMatchList) {
            String tagFilm = filmMatch.getTagFilm();
            String tagCharacter = filmMatch.getTagCharacter();

            if (EmptyUtils.isEmpty(filmMatch.getActorName()) || EmptyUtils.isEmpty(tagFilm) || EmptyUtils.isEmpty(tagCharacter)) {
                continue;
            }

            //初步匹配列表
            List<Actor> preMatchActorList = this.getPreMatchActors(filmMatch);

            //匹配标签
            if (EmptyUtils.isNotEmpty(preMatchActorList)) {

                Map<String, Integer> resMap = new HashMap<>();
                int initCount = 0;

                //循环actor进行匹配
                for (Actor preMatchActor : preMatchActorList) {
                    if (EmptyUtils.isEmpty(preMatchActor.getTagCharacter()) || EmptyUtils.isEmpty(preMatchActor.getTagFilm())) {
                        continue;
                    }

                    String mapKey = preMatchActor.getName();
                    if(mapKey.contains("·")){
                        mapKey = mapKey.substring(0, mapKey.lastIndexOf("·"));
                    }
                    String[] tagFilmArr = tagFilm.split(",");
                    String[] tagCharacterArr = tagCharacter.split(",");

                    resMap.put(mapKey, initCount);

                    String tagFilmOfActor = preMatchActor.getTagFilm();
                    String tagCharacterOfActor = preMatchActor.getTagCharacter();

                    //tagFilm匹配
                    for (String tempTag : tagFilmArr) {
                        if (tagFilmOfActor.contains(tempTag)) {
                            int count = resMap.get(mapKey) + 1;
                            resMap.put(mapKey, count);
                        }
                    }
                    //tagCharacter匹配
                    for (String tempTag : tagCharacterArr) {
                        if (tagCharacterOfActor.contains(tempTag)) {
                            int count = resMap.get(mapKey) + 1;
                            resMap.put(mapKey, count);
                        }
                    }
                }

                //匹配排序和过滤
                String matchActors = this.sortResMap(resMap, countLimit, saveLimit);
                filmMatch.setActors(matchActors);

                //数据保存
                filmMatchMapper.updateByPrimaryKey(filmMatch);
            }

        }

    }


    private FilmMatch generateFilmMatch(Film film, int actorIndex) {
        FilmMatch filmMatch = new FilmMatch();

        filmMatch.setFilmId(film.getId());
        filmMatch.setFilmName(film.getFilm());
        filmMatch.setActorIndex(actorIndex);
        String tagFilm = film.getType() + "片," + film.getTag();
        filmMatch.setTagFilm(tagFilm);

        return filmMatch;
    }

    /**
     * 初步匹配Actor列表
     *
     * @param filmMatch
     * @return
     */
    private List<Actor> getPreMatchActors(FilmMatch filmMatch) {
        Actor actorCondition = new Actor();
        actorCondition.setSex(this.dealString(filmMatch.getActorSex().toString()));
        actorCondition.setCountry(this.dealString(filmMatch.getActorCountry()));
        actorCondition.setType1(this.dealString(filmMatch.getActorType1()));
        actorCondition.setType2(this.dealString(filmMatch.getActorType2()));
        actorCondition.setProperty(this.dealString(filmMatch.getActorProperty()));
        return actorMapper.selectByCondition(actorCondition);
    }

    private String dealString(String input) {
        String outPut = input == null || input.equals("-1") || input.equals("0") ? null : input;
        return outPut;
    }

    /**
     * 对value进行逆序排序和过滤后输出需要的字符串
     *
     * @param resMap     过滤的map
     * @param matchCount 过滤的数量阈值
     * @param saveCount  最终存储的数量值
     * @return
     */
    private String sortResMap(Map<String, Integer> resMap, int matchCount, int saveCount) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        list.addAll(resMap.entrySet());
        Collections.sort(list, (m1, m2) -> m2.getValue() - m1.getValue());
        List<String> dealList = new ArrayList<>();
        list.forEach(entry -> {
            if (entry.getValue() >= matchCount) {
                dealList.add(entry.getKey() + entry.getValue());
            }
        });

        List<String> resList = dealList;
        if (resList.size() > saveCount) {
            resList = resList.subList(0, saveCount);
        }

        return StringUtils.strip(resList.toString(), "[]");
    }


}
