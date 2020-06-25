package com.br.leanlife.models;

import java.util.List;

/**
 * Created by Raphael on 21/05/2016.
 */
public class Group_exercise {
    public Integer id;
    public Integer muscular_group_id;
    public String name_port;
    public String name_english;
    public String name_espanish;
    public String nickname;
    public String ranking;
    public String repititions_session;
    public String exists;
    public String created_at;
    public String updated_at;
    public Muscular_group muscular_group_id__group_exercise;

    public Group_exercise() {
    }

    public Group_exercise(Integer id,Integer muscular_group_id,String name_port,String name_english,String name_espanish,String nickname,String ranking,String repititions_session,String exists,String created_at,String updated_at,Muscular_group muscular_group_id__group_exercise) {
        this.id = id;
        this.muscular_group_id = muscular_group_id;
        this.name_port = name_port;
        this.name_english = name_english;
        this.name_espanish = name_espanish;
        this.nickname = nickname;
        this.ranking = ranking;
        this.repititions_session = repititions_session;
        this.exists = exists;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.muscular_group_id__group_exercise = muscular_group_id__group_exercise;
    }

}
