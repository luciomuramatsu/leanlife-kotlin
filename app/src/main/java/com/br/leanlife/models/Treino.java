package com.br.leanlife.models;

import java.util.List;

/**
 * Created by Raphael on 21/05/2016.
 */
public class Treino {
    public Integer id;
    public Integer training_1_id;
    public Integer training_2_id;
    public Integer users_id;
    public Integer cycle_number;
    public Integer stage_number;
    public Integer session_number;
    public Integer flag_status;
    public String date_ini;
    public String date_final;
    public String free_or_paid;
    public String created_at;
    public String updated_at;
    public String train_type;
    public Integer max_exercises;
    public List<Exercicio> allexercise_routines;

    public Treino() {
    }

    public Treino(Integer id,Integer training_1_id,Integer training_2_id,Integer users_id,Integer cycle_number,Integer stage_number,Integer session_number,Integer flag_status,String date_ini,String date_final,String free_or_paid,String created_at,String updated_at,String train_type, Integer max_exercises, List<Exercicio> allexercise_routines) {
        this.id = id;
        this.training_1_id = training_1_id;
        this.training_2_id = training_2_id;
        this.users_id = users_id;
        this.cycle_number = cycle_number;
        this.stage_number = stage_number;
        this.session_number = session_number;
        this.flag_status = flag_status;
        this.date_ini = date_ini;
        this.date_final = date_final;
        this.free_or_paid = free_or_paid;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.train_type = train_type;
        this.allexercise_routines = allexercise_routines;
        this.max_exercises = max_exercises;
    }

}
