package com.br.leanlife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rapha on 05/09/2017.
 */
public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();
    int tipovideo;
    int grid;
    private VideoView mVV;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static ProgressDialog progressDialog;

    private MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        TextView fab2 = (TextView) findViewById(R.id.pular);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviaskippre();
            }
        });

        grid = 0;
        tipovideo=0;
        Bundle e = getIntent().getExtras();
        if (e!=null && e.containsKey("tipovideo")) {
            tipovideo = e.getInt("tipovideo");
            grid = e.getInt("grid");
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenView = new Bundle();
        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                FeedbackActivity.class.getSimpleName()
        );
        screenView.putString(
                "videonumero",
                ""+tipovideo
        );
        mFirebaseAnalytics.logEvent(
                "entrou_video",
                screenView
        );



        /*Button fab = (Button) findViewById(R.id.btn_enviar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                envianome();
            }
        });*/

        mVV = (VideoView)findViewById(R.id.videoView);
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);

        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setScreenOnWhilePlaying(true);

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer arg0) {
                progressDialog.dismiss();
                mVV.start();
            }
        });
        mVV.setOnCompletionListener(this);
        mVV.setOnPreparedListener(this);

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(mVV);
        mVV.setMediaController(mediaController);


        playFileRes(tipovideo);

        mVV.start();
    }
    public void onPrepared(MediaPlayer mp) {
        progressDialog.dismiss();

        mp.setLooping(false);
    }
    public void enviaskippre()
    {
        if(grid == 1)
        {
            finish();
        }
        else if(tipovideo == 0 )
        {
            Intent myIntent = new Intent(VideoActivity.this, ChatBotActivity.class);
            startActivity(myIntent);
            finish();
        }
        else
        {
            Intent myIntent = new Intent(VideoActivity.this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish();
        }
    }
    private boolean playFileRes(int tipovideo) {
        String fileRes = "";
        if(tipovideo == 0)
        {
            fileRes = "https://www.leanlifeapp.com/images/videosapp/video1.3gp";
        }
        else if(tipovideo == 1)
        {
            fileRes = "https://www.leanlifeapp.com/images/videosapp/video2new.3gp";
        }
        else if(tipovideo == 2)
        {
            fileRes = "https://www.leanlifeapp.com/images/videosapp/video3.3gp";
        }
        else if(tipovideo == 3)
        {
            fileRes = "https://www.leanlifeapp.com/images/videosapp/video4.3gp";
        }
        else if(tipovideo == 4)
        {
            fileRes = "https://www.leanlifeapp.com/images/videosapp/video5.3gp";
        }
        else if(tipovideo == 5)
        {
            fileRes = "https://www.leanlifeapp.com/images/videosapp/video6.3gp";
        }
        mVV.setVideoURI(Uri.parse(fileRes));
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        enviaskippre();
    }



}

