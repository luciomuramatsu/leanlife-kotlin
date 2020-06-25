package com.br.leanlife;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Raphael on 04/05/2016.
 */
public class Step5Activity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private FirebaseAnalytics mFirebaseAnalytics;

    String step5_2 = "";
    String step5_2b = "";
    CharSequence[] items1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step5);
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
                Step5Activity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_step5",
                screenView
        );

        items1 = new CharSequence[]{"30","45","60","90"};

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

                EditText edt1 = (EditText) findViewById(R.id.edit1);
                if(edt1.getText().toString().equals(""))
                {
                    fb.mensagemerro(getString(R.string.step5_pos1), getString(R.string.step5_pos2), ct1);
                }
                else if(step5_2.equals("") || step5_2.equals("null"))
                {
                    fb.mensagemerro(getString(R.string.step5_pos1), getString(R.string.step5_pos3), ct1);
                }
                else
                {
                    String valor1 = edt1.getText().toString();
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step5_1", valor1);
                    editor.commit();

                    Intent myIntent = new Intent(Step5Activity.this, Step6Activity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(myIntent);
                }


            }
        });

        //foiemphasize
        LinearLayout fab3 = (LinearLayout) findViewById(R.id.foiemphasize);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Step5Activity.this, AreaEmphasizeActivity.class);
                startActivity(myIntent);
            }
        });

        inicia();
    }
    public void inicia()
    {
        final EditText choice1 = (EditText) findViewById(R.id.edit1);
        choice1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice1.setText(items1[which].toString());
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String step5_1 = preferences.getString("step5_1", "");
        step5_2 = preferences.getString("step5_2", "");
        step5_2b = preferences.getString("step5_2b", "");

        EditText edt1 = (EditText) findViewById(R.id.edit1);
        if(!step5_1.equals("") && !step5_1.equals("null"))
        {
            edt1.setText(step5_1);
        }
        atualizaemp();
    }
    protected void onResume()
    {
        super.onResume();

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        step5_2 = preferences.getString("step5_2", "");
        step5_2b = preferences.getString("step5_2b", "");

        atualizaemp();
    }
    public void atualizaemp()
    {
        TextView areasemphasize = (TextView) findViewById(R.id.areasemphasize);
        if(!step5_2b.equals("") && !step5_2b.equals("null"))
        {

            areasemphasize.setText(step5_2b);
        }
        else
        {
            areasemphasize.setText(getString(R.string.step5_pos4));
        }
    }
}
