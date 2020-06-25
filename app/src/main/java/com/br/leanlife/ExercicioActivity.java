package com.br.leanlife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.br.leanlife.models.Exercicio;
import com.br.leanlife.models.Exercise;
import com.br.leanlife.models.Group_exercise;
import com.br.leanlife.models.Muscular_group;
import com.br.leanlife.models.Treino;
import com.br.leanlife.models.Workout;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Raphael on 04/05/2016.
 */
public class ExercicioActivity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    int exercicioantsalvo = -1;
    Exercise exercicioantsalvocomp =  null;
    int rankingatual = -1;
    int atualexercicio = -1;
    Exercise atualexerciciocomp =  null;
    int numeroseries = 4;

    int statusrelogio = 0;
    int timingrelogio = 60;
    int timeintervalo = 60;

    String lastgif;

    List<Exercise> allexerciciosparecidos = new ArrayList<Exercise>();
    List<String> allpesosparecidos1 = new ArrayList<String>();
    List<String> allpesosparecidos2 = new ArrayList<String>();
    List<String> allpesosparecidos3 = new ArrayList<String>();
    List<String> allpesosparecidos4 = new ArrayList<String>();
    List<String> allpesosparecidos5 = new ArrayList<String>();
    List<String> allpesosparecidos6 = new ArrayList<String>();
    String podemodificarf = "0";
    String ulttreinoclick = "";
    String abreespecial = "0";
    String idexeroutine = "";

    Integer tipotreino = 3;

    List<Treino> treinosmeu = new ArrayList<Treino>();

    private FirebaseAnalytics mFirebaseAnalytics;
    int contadorexebonito = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio);
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
                voltouatras();

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
                ExercicioActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_exercise",
                screenView
        );

        TextView fab = (TextView) findViewById(R.id.botaoconcluir);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preenviadone();
            }
        });

        TextView fab2 = (TextView) findViewById(R.id.pular);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviaskippre();
            }
        });

        ImageView img1 = (ImageView) findViewById(R.id.trofeuf);
        img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(ct1, TodosVideosActivity.class);
                startActivity(myIntent);

            }
        });

        Bundle b = this.getIntent().getExtras();
        ulttreinoclick = "-1"; // or other values
        if(b != null)
        {
            ulttreinoclick = b.getString("ulttreinoclick");
            podemodificarf = b.getString("podemodificar");
            abreespecial = b.getString("abreespecial");
            idexeroutine = b.getString("idexeroutine");
        }
        if(podemodificarf.equals("1"))
        {
            LinearLayout t1 = (LinearLayout) findViewById(R.id.parte1);
            LinearLayout t2 = (LinearLayout) findViewById(R.id.parte2);
            LinearLayout t3 = (LinearLayout) findViewById(R.id.parte3);
            LinearLayout t4 = (LinearLayout) findViewById(R.id.parte4);
            LinearLayout t5 = (LinearLayout) findViewById(R.id.parte5);
            LinearLayout t6 = (LinearLayout) findViewById(R.id.parte6);

            EditText t1x = (EditText) findViewById(R.id.parte1_5);
            EditText t2x = (EditText) findViewById(R.id.parte2_5);
            EditText t3x = (EditText) findViewById(R.id.parte3_5);
            EditText t4x = (EditText) findViewById(R.id.parte4_5);
            EditText t5x = (EditText) findViewById(R.id.parte5_5);
            EditText t6x = (EditText) findViewById(R.id.parte6_5);

            ImageView esq = (ImageView) findViewById(R.id.setaesquerda);
            ImageView dir = (ImageView) findViewById(R.id.setadireita);
            LinearLayout cronometro = (LinearLayout) findViewById(R.id.cronometro);

            t1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foitime(1);
                }
            });
            t2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foitime(2);
                }
            });
            t3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foitime(3);
                }
            });
            t4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foitime(4);
                }
            });
            t5.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foitime(5);
                }
            });
            t6.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foitime(6);
                }
            });

            t1x.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    foitime(1);

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            t2x.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    foitime(2);

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            t3x.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    foitime(3);

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            t4x.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    foitime(4);

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            t5x.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    foitime(5);

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            t6x.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    foitime(6);

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });

            esq.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foiesquerda();
                }
            });
            dir.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foidireita();
                }
            });

            cronometro.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    foicronometro();
                }
            });
        }
        else
        {
            EditText t1 = (EditText) findViewById(R.id.parte1_5);
            EditText t2 = (EditText) findViewById(R.id.parte2_5);
            EditText t3 = (EditText) findViewById(R.id.parte3_5);
            EditText t4 = (EditText) findViewById(R.id.parte4_5);
            EditText t5 = (EditText) findViewById(R.id.parte5_5);
            EditText t6 = (EditText) findViewById(R.id.parte6_5);
            t1.setFocusableInTouchMode(false);
            t1.setFocusable(false);
            t2.setFocusableInTouchMode(false);
            t2.setFocusable(false);
            t3.setFocusableInTouchMode(false);
            t3.setFocusable(false);
            t4.setFocusableInTouchMode(false);
            t4.setFocusable(false);
            t5.setFocusableInTouchMode(false);
            t5.setFocusable(false);
            t6.setFocusableInTouchMode(false);
            t6.setFocusable(false);


        }
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

        (new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run()
                            {
                                atualizarelogio();
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        })).start(); // the while thread will start in BG thread

        inicia();

        LinearLayout aviso1 = (LinearLayout) findViewById(R.id.paginaaviso);
        aviso1.setVisibility(View.GONE);

    }
    public void foiesquerda()
    {
        List<Treino> treinos = treinosmeu;
        int idf = Integer.parseInt(ulttreinoclick);
        int idf2 = Integer.parseInt(idexeroutine);
        for(int i =0; i<treinos.size(); i++)
        {
            Treino treinof = treinos.get(i);
            if(treinof.id == idf)
            {
                for(int j=0; j<treinof.allexercise_routines.size(); j++)
                {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciof = exercisef.exercise_id__exercise_routine;
                    int grupoexercicio = exercisef.group_exercise_id;
                    int exidf = exercisef.id;
                    if(exidf == idf2)
                    {
                        //exercicios parecidos
                        List<Exercise> parecidos = getparecidos(exercisef.group_exercise_id);
                        salvapesos();
                        int rankingant = rankingatual - 1;
                        int rankingprox = rankingatual + 1;

                        for(int varfor = (parecidos.size() - 1); varfor>=0; varfor = varfor - 1)
                        {
                            Exercise parecidof = parecidos.get(varfor);
                            int parecidofid = parecidof.id;
                            if(varfor == rankingant || (varfor == 0 && rankingant == -1))
                            {
                                //verifica se pode ser esse
                                //print(self.rankingatual)
                                int naopode = 0;
                                for(int k=0; k<treinof.allexercise_routines.size(); k++)
                                {
                                    Exercicio exercisef2 = treinof.allexercise_routines.get(k);
                                    Exercise testao = exercisef2.exercise_id__exercise_routine;
                                    int auxidx = testao.id;
                                    if(auxidx == parecidofid && ( (auxidx == exercicioantsalvo  && rankingatual > 1 ) || ( auxidx == atualexercicio ) ) )
                                    {
                                        naopode = 1;
                                    }
                                }
                                if(naopode == 0)
                                {
                                    //selecionou
                                    contadorexebonito = contadorexebonito - 1;
                                    printaparecidosbonito(grupoexercicio);
                                    if(rankingatual==0)
                                    {
                                        atualexercicio =  exercicioantsalvo;
                                        atualexerciciocomp = exercicioantsalvocomp;
                                        preencheex(exercicioantsalvocomp);
                                        rankingatual = -1;
                                        consultapesos();
                                        break;
                                    }
                                    else
                                    {
                                        atualexercicio = parecidofid;
                                        atualexerciciocomp = parecidof;
                                        preencheex(parecidof);
                                        rankingatual = varfor;
                                        consultapesos();
                                        break;
                                    }

                                }
                                else
                                {
                                    rankingant = rankingant - 1;
                                }
                            }
                        }
                    }

                }
            }
        }

        getpesos();
    }
    public void foidireita()
    {
        List<Treino> treinos = treinosmeu;
        int idf = Integer.parseInt(ulttreinoclick);
        int idf2 = Integer.parseInt(idexeroutine);
        for(int i =0; i<treinos.size(); i++)
        {
            Treino treinof = treinos.get(i);
            if(treinof.id == idf)
            {
                for(int j=0; j<treinof.allexercise_routines.size(); j++)
                {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciof = exercisef.exercise_id__exercise_routine;
                    int grupoexercicio = exercisef.group_exercise_id;
                    int exidf = exercisef.id;
                    if(exidf == idf2)
                    {
                        //exercicios parecidos
                        List<Exercise> parecidos = getparecidos(exercisef.group_exercise_id);
                        salvapesos();
                        int rankingant = rankingatual - 1;
                        int rankingprox = rankingatual + 1;

                        for(int varfor=0; varfor<parecidos.size(); varfor = varfor + 1)
                        {
                            Exercise parecidof = parecidos.get(varfor);
                            int parecidofid = parecidof.id;
                            if(varfor == rankingprox)
                            {
                                //verifica se pode ser esse
                                int naopode = 0;
                                for(int k=0; k<treinof.allexercise_routines.size(); k++)
                                {
                                    Exercicio exercisef2 = treinof.allexercise_routines.get(k);
                                    Exercise testao = exercisef2.exercise_id__exercise_routine;
                                    int auxidx = testao.id;
                                    if(auxidx == parecidofid && (auxidx == exercicioantsalvo) )
                                    {
                                        naopode = 1;
                                    }
                                }
                                if(naopode == 0)
                                {
                                    //selecionou
                                    contadorexebonito = contadorexebonito + 1;
                                    printaparecidosbonito(grupoexercicio);
                                    atualexercicio = parecidofid;
                                    atualexerciciocomp = parecidof;
                                    preencheex(parecidof);
                                    rankingatual = varfor;
                                    consultapesos();
                                    break;
                                }
                                else
                                {

                                    EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
                                    EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
                                    EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
                                    EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
                                    EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
                                    EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);

                                    if( !parte1_5.getText().toString().equals("") )
                                    {
                                        allpesosparecidos1.set(rankingatual + 2,parte1_5.getText().toString());
                                    }
                                    if( !parte2_5.getText().toString().equals("") )
                                    {
                                        allpesosparecidos2.set(rankingatual + 2,parte2_5.getText().toString());
                                    }
                                    if( !parte3_5.getText().toString().equals("") )
                                    {
                                        allpesosparecidos3.set(rankingatual + 2,parte3_5.getText().toString());
                                    }
                                    if( !parte4_5.getText().toString().equals("") )
                                    {
                                        allpesosparecidos4.set(rankingatual + 2,parte4_5.getText().toString());
                                    }
                                    if( !parte5_5.getText().toString().equals("") )
                                    {
                                        allpesosparecidos5.set(rankingatual + 2,parte5_5.getText().toString());
                                    }
                                    if( !parte6_5.getText().toString().equals("") )
                                    {
                                        allpesosparecidos6.set(rankingatual + 2,parte6_5.getText().toString());
                                    }
                                    rankingprox = rankingprox + 1;
                                }
                            }
                        }
                    }

                }
            }
        }
        getpesos();
    }
    public void foicronometro()
    {
        timingrelogio = timeintervalo;
        TextView t1= (TextView) findViewById(R.id.cronometrotexto1);
        TextView t2= (TextView) findViewById(R.id.cronometrotexto2);
        if(statusrelogio == 0)
        {
            statusrelogio = 1;
            t2.setText(getString(R.string.exercicio_pos7));
        }
        else
        {
            statusrelogio = 0;
            t2.setText(getString(R.string.exercicio_pos6));
        }
    }
    public void atualizarelogio()
    {
        if(statusrelogio == 1)
        {
            timingrelogio = timingrelogio - 1;
            if(timingrelogio == 0)
            {
                SharedPreferences preferences =
                        getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                String vibrate = preferences.getString("vibrate", "");
                if(vibrate.equals("") || vibrate.equals("NULL") || vibrate.equals("null"))
                {
                    mudavibrate("1");
                }
                else
                {

                    mudavibrate(vibrate);
                }
            }
            if(timingrelogio <= 0)
            {
                timingrelogio = timeintervalo;
                statusrelogio = 0;
                TextView t2 = (TextView) findViewById(R.id.cronometrotexto2);
                t2.setText(getString(R.string.exercicio_pos6));
            }
        }
        TextView t1crn = (TextView) findViewById(R.id.tempof2);
        t1crn.setText(""+timingrelogio);
    }
    public void mudavibrate(String num)
    {
        if(num.equals("1"))
        {
            //vibra e toca
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
        }
        else if(num.equals("2"))
        {
            //só vibra
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
        }
    }
    public void foitime(int num)
    {

        RelativeLayout parte1_1 = (RelativeLayout) findViewById(R.id.parte1_1);
        RelativeLayout parte2_1 = (RelativeLayout) findViewById(R.id.parte2_1);
        RelativeLayout parte3_1 = (RelativeLayout) findViewById(R.id.parte3_1);
        RelativeLayout parte4_1 = (RelativeLayout) findViewById(R.id.parte4_1);
        RelativeLayout parte5_1 = (RelativeLayout) findViewById(R.id.parte5_1);
        RelativeLayout parte6_1 = (RelativeLayout) findViewById(R.id.parte6_1);
        parte1_1.setVisibility(View.GONE);
        parte2_1.setVisibility(View.GONE);
        parte3_1.setVisibility(View.GONE);
        parte4_1.setVisibility(View.GONE);
        parte5_1.setVisibility(View.GONE);
        parte6_1.setVisibility(View.GONE);

        EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
        EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
        EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
        EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
        EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
        EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);

        if( !parte1_5.getText().toString().equals("") )
        {
            parte1_1.setVisibility(View.VISIBLE);
        }
        if( !parte2_5.getText().toString().equals("") )
        {
            parte2_1.setVisibility(View.VISIBLE);
        }
        if( !parte3_5.getText().toString().equals("") )
        {
            parte3_1.setVisibility(View.VISIBLE);
        }
        if( !parte4_5.getText().toString().equals("") )
        {
            parte4_1.setVisibility(View.VISIBLE);
        }
        if( !parte5_5.getText().toString().equals("") )
        {
            parte5_1.setVisibility(View.VISIBLE);
        }
        if( !parte6_5.getText().toString().equals("") )
        {
            parte6_1.setVisibility(View.VISIBLE);
        }

        TextView parte1_4 = (TextView) findViewById(R.id.parte1_4);
        TextView parte2_4 = (TextView) findViewById(R.id.parte2_4);
        TextView parte3_4 = (TextView) findViewById(R.id.parte3_4);
        TextView parte4_4 = (TextView) findViewById(R.id.parte4_4);
        TextView parte5_4 = (TextView) findViewById(R.id.parte5_4);
        TextView parte6_4 = (TextView) findViewById(R.id.parte6_4);

        if(num == 1)
        {
            parte1_1.setVisibility(View.VISIBLE);
            String velof = parte1_4.getText().toString();
            trocagif(velof);
        }
        else if(num == 2)
        {
            parte2_1.setVisibility(View.VISIBLE);
            String velof = parte2_4.getText().toString();
            trocagif(velof);
        }
        else if(num == 3)
        {
            parte3_1.setVisibility(View.VISIBLE);
            String velof = parte3_4.getText().toString();
            trocagif(velof);
        }
        else if(num == 4)
        {
            parte4_1.setVisibility(View.VISIBLE);
            String velof = parte4_4.getText().toString();
            trocagif(velof);
        }
        else if(num == 5)
        {
            parte5_1.setVisibility(View.VISIBLE);
            String velof = parte5_4.getText().toString();
            trocagif(velof);
        }
        else if(num == 6)
        {
            parte6_1.setVisibility(View.VISIBLE);
            String velof = parte6_4.getText().toString();
            trocagif(velof);
        }

    }
    public void trocagif(String velof)
    {
        String foto1 = atualexerciciocomp.gif;
        String foto2 = atualexerciciocomp.gif2;
        String foto3 = atualexerciciocomp.gif3;
        ImageView imgf = (ImageView) findViewById(R.id.giffinal);
        String photo = "";
        if(velof.equals("3s/rep"))
        {
            photo = foto1;
        }
        else if(velof.equals("2s/rep"))
        {
            photo = foto2;
        }
        else if(velof.equals("1s/rep"))
        {
            photo = foto3;
        }
        //Log.d("xxx",velof);
        if(photo != null && !photo.equals("") && !photo.equals("null"))
        {
            lastgif = photo;
            comecagif();
        }
    }
    public void comecagif()
    {
        if(ct1 != null)
        {
            ImageView imgf = (ImageView) findViewById(R.id.giffinal);
            Glide
                .with(this) // replace with 'this' if it's in activity
                .load(lastgif)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        if(ct1 != null)
                        {
                            comecagif();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loadinggif)
                .into(imgf);
        }

    }
    public List<Exercise> getparecidos(int grupoexercicio)
    {
        List<Exercise> meuarray= new ArrayList<Exercise>();
        for(int i=0; i<allexerciciosparecidos.size(); i++)
        {
            Exercise parecido = allexerciciosparecidos.get(i);
            int grupoexmeu = parecido.group_exercise_id;
            if(grupoexmeu == grupoexercicio)
            {
                meuarray.add(parecido);
            }
        }
        return meuarray;
    }
    public void printaparecidosbonito(int grupoexercicio)
    {
        List<Exercise> meuarray= new ArrayList<Exercise>();
        for(int i=0; i<allexerciciosparecidos.size(); i++)
        {
            Exercise parecido = allexerciciosparecidos.get(i);
            int grupoexmeu = parecido.group_exercise_id;
            if(grupoexmeu == grupoexercicio)
            {
                meuarray.add(parecido);
                allpesosparecidos1.add("");
                allpesosparecidos2.add("");
                allpesosparecidos3.add("");
                allpesosparecidos4.add("");
                allpesosparecidos5.add("");
                allpesosparecidos6.add("");
            }
        }
        allpesosparecidos1.add("");
        allpesosparecidos2.add("");
        allpesosparecidos3.add("");
        allpesosparecidos4.add("");
        allpesosparecidos5.add("");
        allpesosparecidos6.add("");
        TextView contexparecido = (TextView) findViewById(R.id.contexeparecido);
        contexparecido.setText("" + contadorexebonito + "/" + meuarray.size());
    }
    public void getpesos()
    {

        EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
        EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
        EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
        EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
        EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
        EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);

        parte1_5.setText("" + allpesosparecidos1.get(rankingatual + 1));
        parte2_5.setText("" + allpesosparecidos2.get(rankingatual + 1));
        parte3_5.setText("" + allpesosparecidos3.get(rankingatual + 1));
        parte4_5.setText("" + allpesosparecidos4.get(rankingatual + 1));
        parte5_5.setText("" + allpesosparecidos5.get(rankingatual + 1));
        parte6_5.setText("" + allpesosparecidos6.get(rankingatual + 1));

    }
    public void salvapesos()
    {
        EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
        EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
        EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
        EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
        EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
        EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);
        if(!parte1_5.getText().toString().equals("") && !parte1_5.getText().toString().equals("null"))
        {
            allpesosparecidos1.set(rankingatual + 1,parte1_5.getText().toString());
        }
        if(!parte2_5.getText().toString().equals("") && !parte2_5.getText().toString().equals("null"))
        {
            allpesosparecidos2.set(rankingatual + 1,parte2_5.getText().toString());
        }
        if(!parte3_5.getText().toString().equals("") && !parte3_5.getText().toString().equals("null"))
        {
            allpesosparecidos3.set(rankingatual + 1,parte3_5.getText().toString());
        }
        if(!parte4_5.getText().toString().equals("") && !parte4_5.getText().toString().equals("null"))
        {
            allpesosparecidos4.set(rankingatual + 1,parte4_5.getText().toString());
        }
        if(!parte5_5.getText().toString().equals("") && !parte5_5.getText().toString().equals("null"))
        {
            allpesosparecidos5.set(rankingatual + 1,parte5_5.getText().toString());
        }
        if(!parte6_5.getText().toString().equals("") && !parte6_5.getText().toString().equals("null"))
        {
            allpesosparecidos6.set(rankingatual + 1,parte6_5.getText().toString());
        }
    }
    public void enviaskippre()
    {
        if(podemodificarf.equals("0"))
        {
            new AlertDialog.Builder(this)
            .setTitle("")
            .setMessage(getString(R.string.exercicio_pos11))
            .setPositiveButton(getString(R.string.exercicio_pos3), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //finish();
                }

            })
            .show();
            return;
        }

        new AlertDialog.Builder(this)
        .setTitle(getString(R.string.exercicio_pos1))
        .setMessage(getString(R.string.exercicio_pos12))
        .setPositiveButton(getString(R.string.exercicio_pos3), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enviaskip();
            }

        })
        .setNegativeButton(getString(R.string.exercicio_pos4), null)
        .show();
    }
    public void enviaskip()
    {
        int terminou = 1;
        String statusini = "0";
        List<Treino> treinos = treinosmeu;
        int idf = Integer.parseInt(ulttreinoclick);
        int idf2 = Integer.parseInt(idexeroutine);

        for(int i =0; i<treinos.size(); i++)
        {
            Treino treinof = treinos.get(i);
            if(treinof.id == idf)
            {
                statusini = ""+treinof.flag_status;
                for(int j=0; j<treinof.allexercise_routines.size(); j++) {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciof = exercisef.exercise_id__exercise_routine;
                    int grupoexercicio = exercisef.group_exercise_id;
                    int exidf = exercisef.id;
                    if (exidf == idf2) {
                        exercisef.flag_status = 3;
                    }
                }
                //verifica se todos exercicios foram concluidos ou skypped

                for(int j=0; j<treinof.allexercise_routines.size(); j++) {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciof = exercisef.exercise_id__exercise_routine;
                    int grupoexercicio = exercisef.group_exercise_id;

                    int flagstatusf = exercisef.flag_status;
                    if(flagstatusf != 1 && flagstatusf != 3 && ((tipotreino == 0 && exercisef.estatreino1.equals("1")) || (tipotreino == 1 && exercisef.estatreino2.equals("1")) || (tipotreino == 2 && exercisef.estatreino3.equals("1")) || (tipotreino == 3 && exercisef.estatreino4.equals("1"))) )
                    {
                        //ainda n terminou
                        terminou = 0;
                    }
                }
                //terminou o treino
                if(terminou == 1)
                {
                    treinof.flag_status = 1;
                }
            }
        }
        //salvar treinos no banco
        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.esqueci_pos1));
        progress.setMessage(getString(R.string.esqueci_pos2));
        progress.show();
        //salvar treinos no banco
        fb.substituitreino(treinos, this);
        progress.dismiss();

        //if(terminou && statusini != "1")
        if( terminou == 1 && !statusini.equals("1") )
        {
            SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("modificoutreino", "1");
            editor.commit();

            Intent myIntent = new Intent(this, CongratActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("ulttreinoclick", ulttreinoclick);
            mBundle.putString("podemodificar", podemodificarf);
            mBundle.putString("abreespecial", abreespecial);
            myIntent.putExtras(mBundle);
            startActivity(myIntent);
        }
        else{
            finish();
        }

    }

    public void preenviadone()
    {
        if(podemodificarf.equals("0"))
        {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage(getString(R.string.exercicio_pos11))
                    .setPositiveButton(getString(R.string.exercicio_pos3), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }

                    })
                    .show();
            return;
        }
        int erro = 0;
        EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
        EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
        EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
        EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
        EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
        EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);
        float temp = 0 ;
        try {
            if(numeroseries>=1 && parte1_5.getText().toString().equals(""))
            {
                erro = 1;
                temp = Float.parseFloat(parte1_5.getText().toString());
            }
            if(numeroseries>=2 && parte2_5.getText().toString().equals(""))
            {
                erro = 1;
                temp = Float.parseFloat(parte2_5.getText().toString());
            }
            if(numeroseries>=3 && parte3_5.getText().toString().equals(""))
            {
                erro = 1;
                temp = Float.parseFloat(parte3_5.getText().toString());
            }
            if(numeroseries>=4 && parte4_5.getText().toString().equals(""))
            {
                erro = 1;
                temp = Float.parseFloat(parte4_5.getText().toString());
            }
            if(numeroseries>=5 && parte5_5.getText().toString().equals(""))
            {
                erro = 1;
                temp = Float.parseFloat(parte5_5.getText().toString());
            }
            if(numeroseries>=6 && parte6_5.getText().toString().equals(""))
            {
                erro = 1;
                temp = Float.parseFloat(parte6_5.getText().toString());
            }

        } catch (NumberFormatException ex) {
            erro = 1;
        }
        if(erro == 1)
        {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage(getString(R.string.exercicio_pos13))
                    .setPositiveButton(getString(R.string.exercicio_pos3), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }

                    })
                    .show();
            return;
        }
        enviadone();
    }

    public void enviadone()
    {
        int terminou = 1;
        String statusini = "0";
        statusrelogio = 0;
        SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String tipopeso = preferences.getString("type_weight", "");

        List<Treino> treinos = treinosmeu;
        int idf = Integer.parseInt(ulttreinoclick);
        int idf2 = Integer.parseInt(idexeroutine);
        for(int i =0; i<treinos.size(); i++)
        {
            Treino treinof = treinos.get(i);
            if(treinof.id == idf)
            {
                statusini = "" + treinof.flag_status;
                for(int j=0; j<treinof.allexercise_routines.size(); j++) {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciof = exercisef.exercise_id__exercise_routine;
                    int grupoexercicio = exercisef.group_exercise_id;
                    int exidf = exercisef.id;
                    if (exidf == idf2) {
                        String dataf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());

                        exercisef.date_ini = dataf;
                        exercisef.date_final = dataf;
                        exercisef.flag_status = 1;
                        exercisef.exercise_id__exercise_routine = atualexerciciocomp;
                        exercisef.exercise_id = atualexercicio;
                        int contadormeu = 0;
                        List<Workout> workouts = exercisef.allworkouts;
                        for(int k=0; k<exercisef.allworkouts.size(); k++)
                        {
                            Workout workoutf = exercisef.allworkouts.get(k);
                            EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
                            EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
                            EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
                            EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
                            EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
                            EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);
                            String weightx = parte1_5.getText().toString();
                            if(contadormeu == 1)
                            {
                                weightx = parte2_5.getText().toString();
                            }
                            else if(contadormeu == 2)
                            {
                                weightx = parte3_5.getText().toString();
                            }
                            else if(contadormeu == 3)
                            {
                                weightx = parte4_5.getText().toString();
                            }
                            else if(contadormeu == 4)
                            {
                                weightx = parte5_5.getText().toString();
                            }
                            else if(contadormeu == 5)
                            {
                                weightx = parte6_5.getText().toString();
                            }
                            workoutf.weight = Float.parseFloat(weightx);
                            workoutf.flag_status = 1;
                            workoutf.type_weight = Integer.parseInt(tipopeso);
                            contadormeu += 1;
                        }
                    }
                }
                //verifica se todos exercicios foram concluidos ou skypped
                for(int j=0; j<treinof.allexercise_routines.size(); j++) {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciof = exercisef.exercise_id__exercise_routine;
                    int grupoexercicio = exercisef.group_exercise_id;

                    int flagstatusf = exercisef.flag_status;
                    if(flagstatusf != 1 && flagstatusf != 3 && ((tipotreino == 0 && exercisef.estatreino1.equals("1")) || (tipotreino == 1 && exercisef.estatreino2.equals("1")) || (tipotreino == 2 && exercisef.estatreino3.equals("1")) || (tipotreino == 3 && exercisef.estatreino4.equals("1")) ))
                    {
                        //ainda n terminou
                        terminou = 0;
                    }
                }
                if(terminou == 1)
                {
                    //terminou o treino
                    treinof.flag_status = 1;
                }

            }
        }
        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.esqueci_pos1));
        progress.setMessage(getString(R.string.esqueci_pos2));
        progress.show();
        //salvar treinos no banco
        fb.substituitreino(treinos, this);
        progress.dismiss();

        if( terminou == 1 && !statusini.equals("1") )
        {
            preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("modificoutreino", "1");
            editor.commit();

            Intent myIntent = new Intent(this, CongratActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("ulttreinoclick", ulttreinoclick);
            mBundle.putString("podemodificar", podemodificarf);
            mBundle.putString("abreespecial", abreespecial);
            myIntent.putExtras(mBundle);
            startActivity(myIntent);
        }
        else{
            finish();
        }

    }
    public void preencheex(Exercise exerciseesp)
    {
        String nome = exerciseesp.name_english;
        SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String language = preferences.getString("language", "");
        if(language.equals("1"))
        {
            //ingles
            nome = exerciseesp.name_english;
        }
        else if(language.equals("2"))
        {
            //espanhol
            nome = exerciseesp.name_espanish;
        }
        else if(language.equals("3"))
        {
            //portugues
            nome = exerciseesp.name_port;
        }
        String fotof1 = exerciseesp.gif;
        String fotof2 = exerciseesp.gif2;
        String fotof3 = exerciseesp.gif3;
        TextView nomeex = (TextView) findViewById(R.id.nomeexer);
        nomeex.setText(nome);

        String dificuldade1 = exerciseesp.difficulty;
        String dificuldade = "";
        TextView dificuulty = (TextView) findViewById(R.id.dificuldadeex);
        if(dificuldade1.equals("1"))
        {
            dificuldade = getString(R.string.exercicio_pos8);
        }
        else if(dificuldade1.equals("2"))
        {
            dificuldade = getString(R.string.exercicio_pos9);
        }
        else if(dificuldade1.equals("3"))
        {
            dificuldade = getString(R.string.exercicio_pos10);
        }
        dificuulty.setText(dificuldade);


        lastgif = fotof2;
        comecagif();

        ImageView estrela1 = (ImageView) findViewById(R.id.estrela1);
        ImageView estrela2 = (ImageView) findViewById(R.id.estrela2);
        ImageView estrela3 = (ImageView) findViewById(R.id.estrela3);
        ImageView estrela4 = (ImageView) findViewById(R.id.estrela4);
        ImageView estrela5 = (ImageView) findViewById(R.id.estrela5);

        Integer estrelas1 = Integer.parseInt(exerciseesp.efficiency);
        estrela1.setVisibility(View.GONE);
        estrela2.setVisibility(View.GONE);
        estrela3.setVisibility(View.GONE);
        estrela4.setVisibility(View.GONE);
        estrela5.setVisibility(View.GONE);
        if(estrelas1 <= 1)
        {
            estrela1.setVisibility(View.VISIBLE);
        }
        else if(estrelas1 <= 2)
        {
            estrela1.setVisibility(View.VISIBLE);
            estrela2.setVisibility(View.VISIBLE);
        }
        else if(estrelas1 <= 3)
        {
            estrela1.setVisibility(View.VISIBLE);
            estrela2.setVisibility(View.VISIBLE);
            estrela3.setVisibility(View.VISIBLE);
        }
        else if(estrelas1 <= 4)
        {
            estrela1.setVisibility(View.VISIBLE);
            estrela2.setVisibility(View.VISIBLE);
            estrela3.setVisibility(View.VISIBLE);
            estrela4.setVisibility(View.VISIBLE);
        }
        else if(estrelas1 <= 5)
        {
            estrela1.setVisibility(View.VISIBLE);
            estrela2.setVisibility(View.VISIBLE);
            estrela3.setVisibility(View.VISIBLE);
            estrela4.setVisibility(View.VISIBLE);
            estrela5.setVisibility(View.VISIBLE);
        }

    }
    public void preenchepesosdefault()
    {

        SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String tipopeso = preferences.getString("type_weight", "");
        String tipopeso2 = " (Kg)";
        if(tipopeso.equals("2"))
        {
            tipopeso2 = " (lb)";
        }
        String stringf = "Enter" + tipopeso2;
        EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
        EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
        EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
        EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
        EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
        EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);
        parte1_5.setHint(stringf);
        parte2_5.setHint(stringf);
        parte3_5.setHint(stringf);
        parte4_5.setHint(stringf);
        parte5_5.setHint(stringf);
        parte6_5.setHint(stringf);

    }
    public void consultapesos()
    {
        String stringf = "Prev: (...)";
        EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
        EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
        EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
        EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
        EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
        EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);
        parte1_5.setHint(stringf);
        parte2_5.setHint(stringf);
        parte3_5.setHint(stringf);
        parte4_5.setHint(stringf);
        parte5_5.setHint(stringf);
        parte6_5.setHint(stringf);

        String meutreino = ulttreinoclick;
        int meuexercicio = atualexercicio;

        SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("meutreino", meutreino);
        params.put("meuexercicio", ""+meuexercicio);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"retornapesospassados?token=" + token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resp = response.getJSONArray("data");

                            String stringf = "";
                            preenchepesosdefault();

                            int rep1jafoi = 0;
                            int rep2jafoi = 0;
                            int rep3jafoi = 0;
                            int rep4jafoi = 0;
                            int rep5jafoi = 0;
                            int rep6jafoi = 0;

                            for(int i=0; i<resp.length(); i++)
                            {
                                JSONObject exerciciox = resp.getJSONObject(i);
                                JSONArray workoutsjafeito = exerciciox.getJSONArray("workoutsjafeito");
                                for(int j=0; j<workoutsjafeito.length(); j++)
                                {
                                    JSONObject workout = workoutsjafeito.getJSONObject(j);
                                    stringf = "";
                                    String pesox2 = workout.getString("weight");
                                    String type_weightx = workout.getString("type_weight");
                                    if(type_weightx.equals("1"))
                                    {
                                        //kg
                                        stringf = "Prev: " + pesox2 + " kg";
                                    }
                                    else
                                    {
                                        stringf = "Prev: " + pesox2 + " lb";
                                    }
                                    EditText parte1_5 = (EditText) findViewById(R.id.parte1_5);
                                    EditText parte2_5 = (EditText) findViewById(R.id.parte2_5);
                                    EditText parte3_5 = (EditText) findViewById(R.id.parte3_5);
                                    EditText parte4_5 = (EditText) findViewById(R.id.parte4_5);
                                    EditText parte5_5 = (EditText) findViewById(R.id.parte5_5);
                                    EditText parte6_5 = (EditText) findViewById(R.id.parte6_5);

                                    TextView p2rep1 = (TextView) findViewById(R.id.parte1_3);
                                    TextView p2rep2 = (TextView) findViewById(R.id.parte2_3);
                                    TextView p2rep3 = (TextView) findViewById(R.id.parte3_3);
                                    TextView p2rep4 = (TextView) findViewById(R.id.parte4_3);
                                    TextView p2rep5 = (TextView) findViewById(R.id.parte5_3);
                                    TextView p2rep6 = (TextView) findViewById(R.id.parte6_3);
                                    String reps_estimated = workout.getString("reps_estimated");
                                    if(rep1jafoi == 0 && p2rep1.getText().toString().equals(reps_estimated + " Reps"))
                                    {
                                        rep1jafoi = 1;
                                        parte1_5.setHint(stringf);
                                    }
                                    if(rep2jafoi == 0 && p2rep2.getText().toString().equals(reps_estimated + " Reps"))
                                    {
                                        rep2jafoi = 1;
                                        parte2_5.setHint(stringf);
                                    }
                                    if(rep3jafoi == 0 && p2rep3.getText().toString().equals(reps_estimated + " Reps"))
                                    {
                                        rep3jafoi = 1;
                                        parte3_5.setHint(stringf);
                                    }
                                    if(rep4jafoi == 0 && p2rep4.getText().toString().equals(reps_estimated + " Reps"))
                                    {
                                        rep4jafoi = 1;
                                        parte4_5.setHint(stringf);
                                    }
                                    if(rep5jafoi == 0 && p2rep5.getText().toString().equals(reps_estimated + " Reps"))
                                    {
                                        rep5jafoi = 1;
                                        parte5_5.setHint(stringf);
                                    }
                                    if(rep6jafoi == 0 && p2rep6.getText().toString().equals(reps_estimated + " Reps"))
                                    {
                                        rep6jafoi = 1;
                                        parte6_5.setHint(stringf);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        preenchepesosdefault();

                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    public void inicia()
    {
        DatabaseHandler db = new DatabaseHandler(this);
        List<Treino> treinos = db.getAlltreinosimple(ulttreinoclick);
        treinosmeu = treinos;
        allexerciciosparecidos = db.getAllExercises();
        SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String tipopeso = preferences.getString("type_weight", "");
        int idf = Integer.parseInt(ulttreinoclick);
        int idf2 = Integer.parseInt(idexeroutine);
        for(int i =0; i<treinos.size(); i++)
        {
            Treino treinof = treinos.get(i);
            if(treinof.id == idf)
            {
                for(int j=0; j<treinof.allexercise_routines.size(); j++) {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciof = exercisef.exercise_id__exercise_routine;
                    int grupoexercicio = exercisef.group_exercise_id;
                    int exidf = exercisef.id;
                    if (exidf == idf2)
                    {

                        //ex selecionado
                        Exercise exerciseesp = exercisef.exercise_id__exercise_routine;
                        atualexercicio = exerciseesp.id;
                        atualexerciciocomp = exerciseesp;
                        exercicioantsalvo = exerciseesp.id;
                        exercicioantsalvocomp = exerciseesp;
                        printaparecidosbonito(grupoexercicio);

                        int statusexercicio = exercisef.flag_status;
                        String fotof1 = exerciseesp.gif;
                        String fotof2 = exerciseesp.gif2;
                        String fotof3 = exerciseesp.gif3;
                        timeintervalo = exercisef.rest_seconds;
                        timingrelogio = timeintervalo;
                        preencheex(exerciseesp);

                        //all workouts ja feitos
                        int contador = 1;
                        LinearLayout parte1 = (LinearLayout) findViewById(R.id.parte1);
                        RelativeLayout div1 = (RelativeLayout) findViewById(R.id.div1);
                        LinearLayout parte2 = (LinearLayout) findViewById(R.id.parte2);
                        RelativeLayout div2 = (RelativeLayout) findViewById(R.id.div2);
                        LinearLayout parte3 = (LinearLayout) findViewById(R.id.parte3);
                        RelativeLayout div3 = (RelativeLayout) findViewById(R.id.div3);
                        LinearLayout parte4 = (LinearLayout) findViewById(R.id.parte4);
                        RelativeLayout div4 = (RelativeLayout) findViewById(R.id.div4);
                        LinearLayout parte5 = (LinearLayout) findViewById(R.id.parte5);
                        RelativeLayout div5 = (RelativeLayout) findViewById(R.id.div5);
                        LinearLayout parte6 = (LinearLayout) findViewById(R.id.parte6);
                        RelativeLayout div6 = (RelativeLayout) findViewById(R.id.div6);

                        RelativeLayout parte1_1 = (RelativeLayout) findViewById(R.id.parte1_1);
                        RelativeLayout parte2_1 = (RelativeLayout) findViewById(R.id.parte2_1);
                        RelativeLayout parte3_1 = (RelativeLayout) findViewById(R.id.parte3_1);
                        RelativeLayout parte4_1 = (RelativeLayout) findViewById(R.id.parte4_1);
                        RelativeLayout parte5_1 = (RelativeLayout) findViewById(R.id.parte5_1);
                        RelativeLayout parte6_1 = (RelativeLayout) findViewById(R.id.parte6_1);

                        TextView parte1_3 = (TextView) findViewById(R.id.parte1_3);
                        TextView parte2_3 = (TextView) findViewById(R.id.parte2_3);
                        TextView parte3_3 = (TextView) findViewById(R.id.parte3_3);
                        TextView parte4_3 = (TextView) findViewById(R.id.parte4_3);
                        TextView parte5_3 = (TextView) findViewById(R.id.parte5_3);
                        TextView parte6_3 = (TextView) findViewById(R.id.parte6_3);

                        TextView parte1_4 = (TextView) findViewById(R.id.parte1_4);
                        TextView parte2_4 = (TextView) findViewById(R.id.parte2_4);
                        TextView parte3_4 = (TextView) findViewById(R.id.parte3_4);
                        TextView parte4_4 = (TextView) findViewById(R.id.parte4_4);
                        TextView parte5_4 = (TextView) findViewById(R.id.parte5_4);
                        TextView parte6_4 = (TextView) findViewById(R.id.parte6_4);

                        EditText t1 = (EditText) findViewById(R.id.parte1_5);
                        EditText t2 = (EditText) findViewById(R.id.parte2_5);
                        EditText t3 = (EditText) findViewById(R.id.parte3_5);
                        EditText t4 = (EditText) findViewById(R.id.parte4_5);
                        EditText t5 = (EditText) findViewById(R.id.parte5_5);
                        EditText t6 = (EditText) findViewById(R.id.parte6_5);


                        parte1.setVisibility(View.GONE);
                        div1.setVisibility(View.GONE);
                        parte2.setVisibility(View.GONE);
                        div2.setVisibility(View.GONE);
                        parte3.setVisibility(View.GONE);
                        div3.setVisibility(View.GONE);
                        parte4.setVisibility(View.GONE);
                        div4.setVisibility(View.GONE);
                        parte5.setVisibility(View.GONE);
                        div5.setVisibility(View.GONE);
                        parte6.setVisibility(View.GONE);
                        div6.setVisibility(View.GONE);

                        parte1_1.setVisibility(View.GONE);
                        parte2_1.setVisibility(View.GONE);
                        parte3_1.setVisibility(View.GONE);
                        parte4_1.setVisibility(View.GONE);
                        parte5_1.setVisibility(View.GONE);
                        parte6_1.setVisibility(View.GONE);
                        List<Workout> workouts = exercisef.allworkouts;
                        int primeirox = 1;
                        for(int k=0; k<exercisef.allworkouts.size(); k++)
                        {
                            Workout workoutf = exercisef.allworkouts.get(k);
                            int velocity = workoutf.velocity;
                            String reps_estimated = workoutf.reps_estimated;
                            Float weight = workoutf.weight;
                            int status = workoutf.flag_status;

                            if(!reps_estimated.equals("0") && !reps_estimated.equals("-1"))
                            {
                                String velof = "3s/rep";
                                if(velocity == 2)
                                {
                                    velof = "2s/rep";
                                }
                                else if(velocity == 3)
                                {
                                    velof = "1s/rep";
                                }

                                String weightf = "";
                                if(weight != null && weight > 0)
                                {
                                    weightf = ""+weight;
                                }

                                int somebloco = 1;
                                if(primeirox == 1 && status == 0)
                                {
                                    somebloco = 0;
                                    ImageView imgf = (ImageView) findViewById(R.id.giffinal);
                                    String photo = "";
                                    if(velof.equals("3s/rep"))
                                    {
                                        photo = fotof1;
                                    }
                                    else if(velof.equals("2s/rep"))
                                    {
                                        photo = fotof2;
                                    }
                                    else if(velof.equals("1s/rep"))
                                    {
                                        photo = fotof3;
                                    }
                                    if(photo != null && !photo.equals("") && !photo.equals("null"))
                                    {
                                        lastgif = photo;
                                        comecagif();
                                    }
                                    primeirox = 0;
                                }

                                String tipopeso2 = " (Kg)";
                                if(tipopeso.equals("2"))
                                {
                                    tipopeso2 = " (lb)";
                                }
                                if(contador == 1)
                                {
                                    parte1.setVisibility(View.VISIBLE);
                                    div1.setVisibility(View.VISIBLE);
                                    if(somebloco == 1)
                                    {
                                        parte1_1.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        parte1_1.setVisibility(View.VISIBLE);
                                    }
                                    parte1_3.setText(reps_estimated + " Reps");
                                    parte1_4.setText(velof);
                                    t1.setText(weightf);

                                    t1.setHint("Enter" + tipopeso2);
                                }
                                if(contador == 2)
                                {
                                    parte2.setVisibility(View.VISIBLE);
                                    div2.setVisibility(View.VISIBLE);
                                    if(somebloco == 1)
                                    {
                                        parte2_1.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        parte2_1.setVisibility(View.VISIBLE);
                                    }
                                    parte2_3.setText(reps_estimated + " Reps");
                                    parte2_4.setText(velof);
                                    t2.setText(weightf);

                                    t2.setHint("Enter" + tipopeso2);
                                }
                                if(contador == 3)
                                {
                                    parte3.setVisibility(View.VISIBLE);
                                    div3.setVisibility(View.VISIBLE);
                                    if(somebloco == 1)
                                    {
                                        parte3_1.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        parte3_1.setVisibility(View.VISIBLE);
                                    }
                                    parte3_3.setText(reps_estimated + " Reps");
                                    parte3_4.setText(velof);
                                    t3.setText(weightf);

                                    t3.setHint("Enter" + tipopeso2);
                                }
                                if(contador == 4)
                                {
                                    parte4.setVisibility(View.VISIBLE);
                                    div4.setVisibility(View.VISIBLE);
                                    if(somebloco == 1)
                                    {
                                        parte4_1.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        parte4_1.setVisibility(View.VISIBLE);
                                    }
                                    parte4_3.setText(reps_estimated + " Reps");
                                    parte4_4.setText(velof);
                                    t4.setText(weightf);

                                    t4.setHint("Enter" + tipopeso2);
                                }
                                if(contador == 5)
                                {
                                    parte5.setVisibility(View.VISIBLE);
                                    div5.setVisibility(View.VISIBLE);
                                    if(somebloco == 1)
                                    {
                                        parte5_1.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        parte5_1.setVisibility(View.VISIBLE);
                                    }
                                    parte5_3.setText(reps_estimated + " Reps");
                                    parte5_4.setText(velof);
                                    t5.setText(weightf);

                                    t5.setHint("Enter" + tipopeso2);
                                }
                                if(contador == 6)
                                {
                                    parte6.setVisibility(View.VISIBLE);
                                    div6.setVisibility(View.VISIBLE);
                                    if(somebloco == 1)
                                    {
                                        parte6_1.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        parte6_1.setVisibility(View.VISIBLE);
                                    }
                                    parte6_3.setText(reps_estimated + " Reps");
                                    parte6_4.setText(velof);
                                    t6.setText(weightf);

                                    t6.setHint("Enter" + tipopeso2);
                                }
                                numeroseries = contador;
                                contador += 1;
                            }


                        }
                    }

                }
            }
        }
        consultapesos();

    }
    @Override
    public void onBackPressed() {
        voltouatras();
    }
    public void voltouatras()
    {
        if(statusrelogio == 1)
        {
            //mensagem de tem certeza?
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exercicio_pos1)
                    .setMessage(R.string.exercicio_pos2)
                    .setPositiveButton(R.string.exercicio_pos3, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            statusrelogio = 0;
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.exercicio_pos4, null)
                    .show();
        }
        else
        {
            finish();
        }
    }

}
