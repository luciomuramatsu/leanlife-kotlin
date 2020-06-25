package com.br.leanlife.models;

import java.util.List;

/**
 * Created by Raphael on 21/05/2016.
 */
public class Muscular_group {
    public Integer id;
    public String name_port;
    public String name_english;
    public String name_espanish;
    public String nickname;
    public String exists;
    public String created_at;
    public String updated_at;
    public String genericmuscular;
    public String urlicon;

    public Muscular_group() {
    }

    public Muscular_group(Integer id,String name_port,String name_english,String name_espanish,String nickname,String exists,String created_at,String updated_at,String genericmuscular,String urlicon) {
        this.id = id;
        this.name_port = name_port;
        this.name_english = name_english;
        this.name_espanish = name_espanish;
        this.nickname = nickname;
        this.exists = exists;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.genericmuscular = genericmuscular;
        this.urlicon = urlicon;
    }

}
