package com.br.leanlife;

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
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Raphael on 04/05/2016.
 */
public class Step3Activity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private FirebaseAnalytics mFirebaseAnalytics;

    Integer opt1 = 0;
    Integer opt2 = 0;
    Integer opt3 = 0;
    Integer opt4 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ct1 = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenView = new Bundle();
        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                Step3Activity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_step3",
                screenView
        );

        ImageView fab = (ImageView) findViewById(R.id.volta1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView fab2 = (ImageView) findViewById(R.id.frente1);
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
                    else if(opt4 == 1)
                    {
                        strfinal = "4";
                    }

                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step3", strfinal);
                    editor.commit();

                    Intent myIntent = new Intent(Step3Activity.this, Step4Activity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(myIntent);
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

        LinearLayout l4 = (LinearLayout) findViewById(R.id.opcao4);
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionou(4);
            }
        });

        inicia();

    }
    public void inicia()
    {
        ImageView img1 = (ImageView) findViewById(R.id.opcao1_icon);
        ImageView img2 = (ImageView) findViewById(R.id.opcao2_icon);
        ImageView img3 = (ImageView) findViewById(R.id.opcao3_icon);
        ImageView img4 = (ImageView) findViewById(R.id.opcao4_icon);
        img1.setVisibility(View.GONE);
        img2.setVisibility(View.GONE);
        img3.setVisibility(View.GONE);
        img4.setVisibility(View.GONE);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String step2 = preferences.getString("step3", "");
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
            else if(step2.equals("4"))
            {
                img4.setVisibility(View.VISIBLE);
                opt4 = 1;
            }
        }
    }
    public int qtdselecionados()
    {
        int numf = 0;
        if(opt1 == 1)       numf += 1;
        if(opt2 == 1)       numf += 1;
        if(opt3 == 1)       numf += 1;
        if(opt4 == 1)       numf += 1;

        return numf;
    }
    public void selecionou(Integer num)
    {
        ImageView img1 = (ImageView) findViewById(R.id.opcao1_icon);
        ImageView img2 = (ImageView) findViewById(R.id.opcao2_icon);
        ImageView img3 = (ImageView) findViewById(R.id.opcao3_icon);
        ImageView img4 = (ImageView) findViewById(R.id.opcao4_icon);
        img1.setVisibility(View.GONE);
        img2.setVisibility(View.GONE);
        img3.setVisibility(View.GONE);
        img4.setVisibility(View.GONE);
        opt1 = 0;
        opt2 = 0;
        opt3 = 0;
        opt4 = 0;
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
        else if(num == 4)
        {
            img4.setVisibility(View.VISIBLE);
            opt4 = 1;
        }
    }
}
