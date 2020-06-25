package com.br.leanlife.models;

import java.util.List;

/**
 * Created by Raphael on 21/05/2016.
 */
public class Exercicio {
    public Integer id;
    public Integer routine_id;
    public Integer group_exercise_id;
    public Integer exercise_id;
    public Integer rest_seconds;
    public Integer series;
    public String date_ini;
    public String date_final;
    public Integer flag_status;
    public String created_at;
    public String updated_at;
    public String train_type;
    public String estatreino1;
    public String estatreino2;
    public String estatreino3;
    public String estatreino4;
    public List<Workout> allworkouts;
    public Group_exercise group_exercise_id__exercise_routine;
    public Exercise exercise_id__exercise_routine;

    public Exercicio() {
    }

    public Exercicio(Integer id,Integer routine_id,Integer group_exercise_id,Integer exercise_id,Integer rest_seconds,Integer series,String date_ini,String date_final,Integer flag_status,String created_at,String updated_at, String train_type, String estatreino1, String estatreino2, String estatreino3, String estatreino4, List<Workout> allworkouts,Group_exercise group_exercise_id__exercise_routine,Exercise exercise_id__exercise_routine) {
        this.id = id;
        this.routine_id = routine_id;
        this.group_exercise_id = group_exercise_id;
        this.exercise_id = exercise_id;
        this.rest_seconds = rest_seconds;
        this.series = series;
        this.flag_status = flag_status;
        this.date_ini = date_ini;
        this.date_final = date_final;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.allworkouts = allworkouts;
        this.group_exercise_id__exercise_routine = group_exercise_id__exercise_routine;
        this.exercise_id__exercise_routine = exercise_id__exercise_routine;
        this.train_type = train_type;
        this.estatreino1 = estatreino1;
        this.estatreino2 = estatreino2;
        this.estatreino3 = estatreino3;
        this.estatreino4 = estatreino4;
    }

}
