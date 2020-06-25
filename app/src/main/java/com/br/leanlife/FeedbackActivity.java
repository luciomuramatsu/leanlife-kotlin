package com.br.leanlife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TextView;

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
public class FeedbackActivity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();
    int valorselect = 1;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
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
                FeedbackActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_feedback",
                screenView
        );
        TextView t1 = (TextView) findViewById(R.id.seg_1);
        TextView t2 = (TextView) findViewById(R.id.seg_2);
        TextView t3 = (TextView) findViewById(R.id.seg_3);
        TextView t4 = (TextView) findViewById(R.id.seg_4);
        TextView t5 = (TextView) findViewById(R.id.seg_5);
        TextView t6 = (TextView) findViewById(R.id.seg_6);
        TextView t7 = (TextView) findViewById(R.id.seg_7);
        TextView t8 = (TextView) findViewById(R.id.seg_8);
        TextView t9 = (TextView) findViewById(R.id.seg_9);
        TextView t10 = (TextView) findViewById(R.id.seg_10);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 1;
                TextView t1 = (TextView) findViewById(R.id.seg_1);
                t1.setBackgroundColor(Color.parseColor("#FF0000"));
                t1.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 2;
                TextView t2 = (TextView) findViewById(R.id.seg_2);
                t2.setBackgroundColor(Color.parseColor("#FF0000"));
                t2.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 3;
                TextView t3 = (TextView) findViewById(R.id.seg_3);
                t3.setBackgroundColor(Color.parseColor("#FF0000"));
                t3.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 4;
                TextView t4 = (TextView) findViewById(R.id.seg_4);
                t4.setBackgroundColor(Color.parseColor("#FF0000"));
                t4.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 5;
                TextView t5 = (TextView) findViewById(R.id.seg_5);
                t5.setBackgroundColor(Color.parseColor("#FF0000"));
                t5.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 6;
                TextView t6 = (TextView) findViewById(R.id.seg_6);
                t6.setBackgroundColor(Color.parseColor("#FF0000"));
                t6.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 7;
                TextView t7 = (TextView) findViewById(R.id.seg_7);
                t7.setBackgroundColor(Color.parseColor("#FF0000"));
                t7.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 8;
                TextView t8 = (TextView) findViewById(R.id.seg_8);
                t8.setBackgroundColor(Color.parseColor("#FF0000"));
                t8.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 9;
                TextView t9 = (TextView) findViewById(R.id.seg_9);
                t9.setBackgroundColor(Color.parseColor("#FF0000"));
                t9.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        t10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiraselect();
                valorselect = 10;
                TextView t10 = (TextView) findViewById(R.id.seg_10);
                t10.setBackgroundColor(Color.parseColor("#FF0000"));
                t10.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        Button fab = (Button) findViewById(R.id.btn_enviar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                envianome();
            }
        });
    }
    public void tiraselect()
    {
        TextView t1 = (TextView) findViewById(R.id.seg_1);
        TextView t2 = (TextView) findViewById(R.id.seg_2);
        TextView t3 = (TextView) findViewById(R.id.seg_3);
        TextView t4 = (TextView) findViewById(R.id.seg_4);
        TextView t5 = (TextView) findViewById(R.id.seg_5);
        TextView t6 = (TextView) findViewById(R.id.seg_6);
        TextView t7 = (TextView) findViewById(R.id.seg_7);
        TextView t8 = (TextView) findViewById(R.id.seg_8);
        TextView t9 = (TextView) findViewById(R.id.seg_9);
        TextView t10 = (TextView) findViewById(R.id.seg_10);
        t1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t1.setTextColor(Color.parseColor("#FF0000"));

        t2.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t2.setTextColor(Color.parseColor("#FF0000"));

        t3.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t3.setTextColor(Color.parseColor("#FF0000"));

        t4.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t4.setTextColor(Color.parseColor("#FF0000"));

        t5.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t5.setTextColor(Color.parseColor("#FF0000"));

        t6.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t6.setTextColor(Color.parseColor("#FF0000"));

        t7.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t7.setTextColor(Color.parseColor("#FF0000"));

        t8.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t8.setTextColor(Color.parseColor("#FF0000"));

        t9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t9.setTextColor(Color.parseColor("#FF0000"));

        t10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        t10.setTextColor(Color.parseColor("#FF0000"));
    }

    public void envianome()
    {
        EditText input2 = (EditText) findViewById(R.id.mensagem);
        String mensagem = input2.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("mensagem", mensagem);
        params.put("nota", ""+valorselect);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.esqueci_pos1));
        progress.setMessage(getString(R.string.esqueci_pos2));
        progress.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"enviafeedback?token="+token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progress.dismiss();

                        final AlertDialog alertDialog = new AlertDialog.Builder(ct1).create();
                        alertDialog.setTitle(getString(R.string.nomeemail_pos_0));
                        alertDialog.setMessage(getString(R.string.nomeemail_pos_1));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                                finish();
                            }
                        });
                        alertDialog.show();
                        //finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        fb.mensagemerro(getString(R.string.nomeemail_pos_2), getString(R.string.nomeemail_pos_3),ct1);
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }


}

