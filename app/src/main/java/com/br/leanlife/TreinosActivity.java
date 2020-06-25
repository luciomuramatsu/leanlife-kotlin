package com.br.leanlife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.br.leanlife.adapters.TreinoAdapter;
import com.br.leanlife.models.Exercicio;
import com.br.leanlife.models.Exercise;
import com.br.leanlife.models.Group_exercise;
import com.br.leanlife.models.Muscular_group;
import com.br.leanlife.models.Treino;

/**
 * Created by Raphael on 04/05/2016.
 */
public class TreinosActivity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Activity ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private List<Exercicio> msglista4 = new ArrayList<>();
    private RecyclerView recyclerView4;
    private TreinoAdapter mAdapter4;

    private FirebaseAnalytics mFirebaseAnalytics;

    CharSequence items[];
    Map<String,Integer> itemsmap = new HashMap<String,Integer>();

    String value = "-1";
    String podemodificar = "-1";
    String abreespecial = "-1";
    List<Treino> treinos = new ArrayList<Treino>();
    int primeiravez = 1;
    int tipotreino = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finalizar tela ao clicar no voltar
                finish();

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ct1 = this;
        /*TextView fab = (TextView) findViewById(R.id.link_signup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(myIntent);
            }
        });*/
        SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String tipotreinox = preferences.getString("tipotreino", "");
        if(!tipotreinox.equals("") && !tipotreinox.equals("null"))
        {
            tipotreino = Integer.parseInt(tipotreinox);
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenView = new Bundle();
        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                TreinosActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_treinos",
                screenView
        );


        Bundle b = this.getIntent().getExtras();
        value = "-1"; // or other values
        if(b != null)
        {
            value = b.getString("ulttreinoclick");
            podemodificar = b.getString("podemodificar");
            abreespecial = b.getString("abreespecial");
        }

        itemsmap.put(getString(R.string.localnew1),1);
        itemsmap.put(getString(R.string.localnew2),2);
        itemsmap.put(getString(R.string.localnew3),3);
        itemsmap.put(getString(R.string.localnew4),4);
        List<String> listItemsest = new ArrayList<String>();
        listItemsest.add(getString(R.string.localnew1));
        listItemsest.add(getString(R.string.localnew2));
        listItemsest.add(getString(R.string.localnew3));
        listItemsest.add(getString(R.string.localnew4));
        items = listItemsest.toArray(new CharSequence[listItemsest.size()]);

        ImageView img1 = (ImageView) findViewById(R.id.trofeuf);
        img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Intent myIntent = new Intent(ct1, TodosVideosActivity.class);
                //startActivity(myIntent);

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ct1);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Log.d("OPCAO SELECTED",""+which);
                        tipotreino = which;

                        SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("tipotreino", ""+tipotreino);
                        editor.commit();

                        inicia2();
                    }
                });
                builder.show();

            }
        });

        LinearLayout aviso1 = (LinearLayout) findViewById(R.id.paginaaviso);
        aviso1.setVisibility(View.GONE);
        //inicia();
    }
    @Override
    public void onResume() {
        super.onResume();
        inicia();
    }
    public void inicia2()
    {
        recyclerView4 = (RecyclerView) findViewById(R.id.lista1);

        recyclerView4.removeAllViews();
        msglista4.clear();

        mAdapter4 = new TreinoAdapter(msglista4,ct1,value,podemodificar,abreespecial);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ct1);
        recyclerView4.setLayoutManager(mLayoutManager);
        recyclerView4.setItemAnimator(new DefaultItemAnimator());
        recyclerView4.setAdapter(mAdapter4);

        String primeiro = "1";
        int contadormax = 0;
        for(int i1 =0; i1<treinos.size(); i1++)
        {
            Treino treinof = treinos.get(i1);
            int idf = treinof.id;
            int status = treinof.flag_status;
            int itemciclo = treinof.cycle_number;
            int itemstage = treinof.stage_number;
            int itemsession = treinof.session_number;

            if(idf == Integer.parseInt(value))
            {
                int trainingnumber = ( ( itemciclo - 1) * 16 ) + ( ( itemstage - 1 ) *4) + itemsession;
                String tipocasa = "";
                if(tipotreino == 0)
                {
                    tipocasa = getString(R.string.localnew1b);
                }
                else if(tipotreino == 1)
                {
                    tipocasa = getString(R.string.localnew2b);
                }
                else if(tipotreino == 2)
                {
                    tipocasa = getString(R.string.localnew3b);
                }
                else if(tipotreino == 3)
                {
                    tipocasa = getString(R.string.localnew4b);
                }
                setTitle(getString(R.string.exercicios_1)+" "+trainingnumber + " (" + tipocasa + ")");

                for(int j=0; j<treinof.allexercise_routines.size(); j++)
                {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciox = exercisef.exercise_id__exercise_routine;

                    //Log.d("AAAAAAAA",exerciciox.find_exercise);
                    //Log.d("BBBBBBBB",""+treinof.max_exercises);
                    if((tipotreino == 0 && exercisef.estatreino1.equals("1")) || (tipotreino == 1 && exercisef.estatreino2.equals("1")) || (tipotreino == 2 && exercisef.estatreino3.equals("1")) || (tipotreino == 3 && exercisef.estatreino4.equals("1")) )
                    {
                        int statusf = exercisef.flag_status;
                        if(primeiro.equals("1") && statusf == 0)
                        {
                            primeiro = "0";
                            statusf = 2;
                        }
                        int idaux = exercisef.id;
                        msglista4.add(exercisef);
                        contadormax++;
                    }


                }
            }
        }
        mAdapter4.notifyDataSetChanged();
    }
    public void inicia()
    {
        SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String modificoutreino = preferences.getString("modificoutreino", "");
        if(modificoutreino.equals("1") || primeiravez == 1)
        {
            DatabaseHandler db = new DatabaseHandler(ct1);
            treinos = db.getAlltreinosimple2(value);
            primeiravez = 0;
        }

        /*DatabaseHandler db = new DatabaseHandler(ct1);
        List<Treino> treinos = db.getAlltreinosimple2(value);*/

        inicia2();
    }
}
