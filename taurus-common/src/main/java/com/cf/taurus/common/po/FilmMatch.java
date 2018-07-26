package com.cf.taurus.common.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilmMatch {
    private Integer id;

    private Integer filmId;

    private String filmName;

    private Integer actorIndex;

    private String actorName;

    private Integer actorSex;

    private String actorType1;

    private String actorType2;

    private String actorCountry;

    private String actorProperty;

    private String tagFilm;

    private String tagCharacter;

    private String actors;

}