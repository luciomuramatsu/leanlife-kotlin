package com.br.leanlife.models;

/**
 * Created by Raphael on 21/05/2016.
 */
public class Workout {
    public Integer id;
    public Integer exercise_routine_id;
    public String reps_estimated;
    public String reps_real;
    public Integer velocity;
    public Float weight;
    public Integer type_weight;
    public Integer status;
    public String date_ini;
    public String date_final;
    public Integer flag_status;
    public String created_at;
    public String updated_at;

    public Workout() {
    }

    public Workout(Integer id,Integer exercise_routine_id,String reps_estimated,String reps_real,Integer velocity,Float weight,Integer type_weight,Integer status,String date_ini,String date_final,Integer flag_status,String created_at,String updated_at) {
        this.id = id;
        this.exercise_routine_id = exercise_routine_id;
        this.reps_estimated = reps_estimated;
        this.reps_real = reps_real;
        this.velocity = velocity;
        this.weight = weight;
        this.type_weight = type_weight;
        this.flag_status = flag_status;
        this.date_ini = date_ini;
        this.date_final = date_final;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

}
