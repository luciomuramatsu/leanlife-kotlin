package com.br.leanlife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.br.leanlife.models.Exercicio;
import com.br.leanlife.models.Exercise;
import com.br.leanlife.models.Group_exercise;
import com.br.leanlife.models.Muscular_group;
import com.br.leanlife.models.Treino;
import com.br.leanlife.models.Workout;

/**
 * Created by Raphael on 30/05/2016.
 */
public class FuncoesBasicas {
    public FuncoesBasicas() {
    }
    public void mensagemerro(String texto1, String texto2, Context ct1)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(ct1).create();
        alertDialog.setTitle(texto1);
        alertDialog.setMessage(texto2);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
    public String transfdata(String data)
    {
        String dataf = "";
        if(data.equals("")||data.equals(false)||data.equals("null")||data.isEmpty())        return dataf;
        String[] dataaux = data.split(" ");
        String[] dataaux2 = dataaux[0].split("-");
        dataf = dataaux2[2]+"-"+dataaux2[1]+"-"+dataaux2[0];
        return dataf;
    }
    public String hashinvertido(Map<String, Integer> itemsaux, Integer valor)
    {
        String retorno = "";
        if(valor.equals("")||valor.equals(false)||valor.equals(0))        return retorno;
        for (Map.Entry<String, Integer> e : itemsaux.entrySet()) {
            if( e.getValue().equals(valor) )
            {
                retorno = e.getKey();
            }
        }
        return retorno;
    }
    public String geraexpirationdata(Long miliini,String tipo)
    {
        Long soma = 0L;
        if(tipo.equals("leanlifeass1"))
        {
            soma = 2629746000L;
        }
        if(tipo.equals("leanlifeass2"))
        {
            soma = 2629746000L * 6;
        }
        if(tipo.equals("leanlifeass3"))
        {
            soma = 2629746000L * 12;
        }
        if(tipo.equals("leanlife.for.life"))
        {

        }
        //soma dias de atraso permitidos
        soma = soma + + (86400000*7);
        Long datafinal = soma + miliini;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(new Date(datafinal));
        return dateString;
    }
    public String convertplano(String tipo)
    {
        String strfinal = "";
        if(tipo.equals("leanlifeass1"))
        {
            strfinal = "PD52GNNZRR2";
        }
        if(tipo.equals("leanlifeass2"))
        {
            strfinal = "Leanlifeass2";
        }
        if(tipo.equals("leanlifeass3"))
        {
            strfinal = "Leanlifeass3";
        }
        if(tipo.equals("leanlife.for.life"))
        {
            strfinal = "leanlife.For.life";
        }
        return strfinal;
    }
    public String convertplanobonito(String tipo,Context ct1)
    {
        String strfinal = "";
        if(tipo.equals("PD52GNNZRR2"))
        {
            strfinal = ct1.getString(R.string.plano2_pos2);
        }
        if(tipo.equals("Leanlifeass2"))
        {
            strfinal = ct1.getString(R.string.plano2_pos3);
        }
        if(tipo.equals("Leanlifeass3"))
        {
            strfinal = ct1.getString(R.string.plano2_pos4);
        }
        if(tipo.equals("leanlife.For.life"))
        {
            strfinal = ct1.getString(R.string.plano2_pos5);
        }
        return strfinal;
    }
    public void substituitreino(List<Treino> treinos, Activity ct1)
    {
        DatabaseHandler db = new DatabaseHandler(ct1);
        //db.deleteTreinos();
        //db.deleteEx();
        //db.deleteWorkout();
        for(int i=0; i<treinos.size(); i++)
        {
            Treino treinof = treinos.get(i);

            db.addTreinos(new Treino(treinof.id,treinof.training_1_id,treinof.training_2_id,treinof.users_id,treinof.cycle_number,treinof.stage_number,treinof.session_number,treinof.flag_status,treinof.date_ini,treinof.date_final,treinof.free_or_paid,treinof.created_at,treinof.updated_at,treinof.train_type,treinof.max_exercises,null));
            List<Exercicio> exercicios = treinof.allexercise_routines;
            for(int j=0; j<exercicios.size(); j++)
            {
                Exercicio exerciciof = exercicios.get(j);
                db.addExercicio(new Exercicio(exerciciof.id,exerciciof.routine_id,exerciciof.group_exercise_id,exerciciof.exercise_id,exerciciof.rest_seconds,exerciciof.series,exerciciof.date_ini,exerciciof.date_final,exerciciof.flag_status,exerciciof.created_at,exerciciof.updated_at,exerciciof.train_type,exerciciof.estatreino1,exerciciof.estatreino2,exerciciof.estatreino3,exerciciof.estatreino4,null,null,null));
                List<Workout> allworkouts = exerciciof.allworkouts;
                for(int k=0; k<allworkouts.size(); k++)
                {
                    Workout workoutf = allworkouts.get(k);

                    db.addworkout(new Workout(workoutf.id,workoutf.exercise_routine_id,workoutf.reps_estimated,workoutf.reps_real,workoutf.velocity,workoutf.weight,workoutf.type_weight,workoutf.status,workoutf.date_ini,workoutf.date_final,workoutf.flag_status,workoutf.created_at,workoutf.updated_at));
                }
            }
        }
    }
    public void enviatoken(String token,Context ct1)
    {
        String urlf = "https://www.leanlifeapp.com/api/public/";
        SharedPreferences preferences = ct1.getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String tokenx = preferences.getString("token", "");
        if(!tokenx.equals("") && !tokenx.equals("NULL"))
        {
            RequestQueue requestQueue = Volley.newRequestQueue(ct1);
            Map<String, String> params = new HashMap<String, String>();
            params.put("meutoken", token);
            params.put("tipo", "0");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"criadevice?token=" + tokenx, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //sucesso
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //erro
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }

    }
    public void configuralinguaini(Context contf)
    {
        SharedPreferences preferences =
                contf.getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String language = preferences.getString("language", "");
        if(language.equals("") || language.equals("NULL") || language.equals("null"))
        {
            String valorf = "1";
            String lingua = Locale.getDefault().getLanguage();
            if(lingua.equals("pt"))
            {
                valorf = "3";
            }
            else if(lingua.equals("es"))
            {
                valorf = "2";
            }
            else if(lingua.equals("en"))
            {
                valorf = "1";
            }
            preferences =
                    contf.getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language", valorf );
            editor.commit();
        }
		preferences =
                contf.getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        language = preferences.getString("language", "");
        if(language.equals("1"))
        {
            setLocale("en",contf);
        }
        else if(language.equals("2"))
        {
            setLocale("es",contf);
        }
        else
        {
            setLocale("pt",contf);
        }
    }

    public void setLocale(String lang,Context contf) {
        Locale myLocale = new Locale(lang);
        Resources res = contf.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
