package com.br.leanlife;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by rapha on 05/09/2017.
 */
public class IdiomaActivity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    Integer opt1 = 0;
    Integer opt2 = 0;
    Integer opt3 = 0;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idioma);
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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenView = new Bundle();
        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                IdiomaActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_idioma",
                screenView
        );

        Button fab2 = (Button) findViewById(R.id.btn_enviar);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selecionados = qtdselecionados();
                if(selecionados < 1)
                {
                    fb.mensagemerro(getString(R.string.step3_pos1), getString(R.string.step3_pos2), ct1);
                    return;
                }
                else
                {
                    String strfinal = "";
                    if(opt1 == 1)
                    {
                        strfinal = "1";
                    }
                    else if(opt2 == 1)
                    {
                        strfinal = "2";
                    }
                    else if(opt3 == 1)
                    {
                        strfinal = "3";
                    }

                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("language", strfinal);
                    editor.commit();

                    finish();
                }

            }
        });

        LinearLayout l1 = (LinearLayout) findViewById(R.id.opcao1);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionou(1);
            }
        });

        LinearLayout l2 = (LinearLayout) findViewById(R.id.opcao2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionou(2);
            }
        });

        LinearLayout l3 = (LinearLayout) findViewById(R.id.opcao3);
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionou(3);
            }
        });

        inicia();

    }

    public void inicia()
    {
        ImageView img1 = (ImageView) findViewById(R.id.opcao1_icon);
        ImageView img2 = (ImageView) findViewById(R.id.opcao2_icon);
        ImageView img3 = (ImageView) findViewById(R.id.opcao3_icon);
        img1.setVisibility(View.GONE);
        img2.setVisibility(View.GONE);
        img3.setVisibility(View.GONE);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String step2 = preferences.getString("language", "");
        if(step2 != null && !step2.equals("") && !step2.equals("null"))
        {
            if(step2.equals("1") )
            {
                img1.setVisibility(View.VISIBLE);
                opt1 = 1;
            }
            else if(step2.equals("2"))
            {
                img2.setVisibility(View.VISIBLE);
                opt2 = 1;
            }
            else if(step2.equals("3"))
            {
                img3.setVisibility(View.VISIBLE);
                opt3 = 1;
            }
        }
    }
    public int qtdselecionados()
    {
        int numf = 0;
        if(opt1 == 1)       numf += 1;
        if(opt2 == 1)       numf += 1;
        if(opt3 == 1)       numf += 1;

        return numf;
    }
    public void selecionou(Integer num)
    {
        ImageView img1 = (ImageView) findViewById(R.id.opcao1_icon);
        ImageView img2 = (ImageView) findViewById(R.id.opcao2_icon);
        ImageView img3 = (ImageView) findViewById(R.id.opcao3_icon);
        img1.setVisibility(View.GONE);
        img2.setVisibility(View.GONE);
        img3.setVisibility(View.GONE);
        opt1 = 0;
        opt2 = 0;
        opt3 = 0;
        if(num == 1)
        {
            img1.setVisibility(View.VISIBLE);
            opt1 = 1;
        }
        else if(num == 2)
        {
            img2.setVisibility(View.VISIBLE);
            opt2 = 1;
        }
        else if(num == 3)
        {
            img3.setVisibility(View.VISIBLE);
            opt3 = 1;
        }
    }

}
