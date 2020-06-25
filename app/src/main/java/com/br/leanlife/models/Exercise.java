package com.br.leanlife.models;

import java.util.List;

/**
 * Created by Raphael on 21/05/2016.
 */
public class Exercise {
    public Integer id;
    public Integer group_exercise_id;
    public String name_port;
    public String name_english;
    public String name_espanish;
    public String image_mini;
    public String image_medium;
    public String image_big;
    public String gif;
    public String nickname;
    public String created_at;
    public String updated_at;
    public String exists;
    public String gif2;
    public String gif3;
    public String ranking;
    public String efficiency;
    public String difficulty;
    public String find_exercise;

    public Exercise() {
    }

    public Exercise(Integer id,Integer group_exercise_id,String name_port,String name_english,String name_espanish,String image_mini,String image_medium,String image_big,String gif,String nickname,String created_at,String updated_at,String exists,String gif2,String gif3,String ranking,String efficiency,String difficulty,String find_exercise) {
        this.id = id;
        this.group_exercise_id = group_exercise_id;
        this.name_port = name_port;
        this.name_english = name_english;
        this.name_espanish = name_espanish;
        this.image_mini = image_mini;
        this.image_medium = image_medium;
        this.image_big = image_big;
        this.gif = gif;
        this.nickname = nickname;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.exists = exists;
        this.gif2 = gif2;
        this.gif3 = gif3;
        this.ranking = ranking;
        this.efficiency = efficiency;
        this.difficulty = difficulty;
        this.find_exercise = find_exercise;
    }

}
