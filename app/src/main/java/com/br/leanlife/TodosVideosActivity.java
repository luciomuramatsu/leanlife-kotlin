package com.br.leanlife;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by rapha on 05/09/2017.
 */
public class TodosVideosActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_todosvideos);
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
                "entrou_allvideos",
                screenView
        );

        RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.video1);
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.video2);
        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.video3);
        RelativeLayout rl4 = (RelativeLayout) findViewById(R.id.video4);
        RelativeLayout rl5 = (RelativeLayout) findViewById(R.id.video5);
        RelativeLayout rl6 = (RelativeLayout) findViewById(R.id.video6);

        rl1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ct1, VideoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("grid", 1);
                mBundle.putInt("tipovideo", 0);
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
            }
        });

        rl2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ct1, VideoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("grid", 1);
                mBundle.putInt("tipovideo", 1);
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
            }
        });

        rl3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ct1, VideoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("grid", 1);
                mBundle.putInt("tipovideo", 2);
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
            }
        });

        rl4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ct1, VideoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("grid", 1);
                mBundle.putInt("tipovideo", 3);
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
            }
        });

        rl5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ct1, VideoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("grid", 1);
                mBundle.putInt("tipovideo", 4);
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
            }
        });

        rl6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ct1, VideoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("grid", 1);
                mBundle.putInt("tipovideo", 5);
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
            }
        });


    }

}
