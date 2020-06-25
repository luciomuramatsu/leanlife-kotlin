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
import android.widget.EditText;

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
public class NomeEmailActivity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomeemail);
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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenView = new Bundle();
        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                NomeEmailActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_nomeemail",
                screenView
        );

        Button fab = (Button) findViewById(R.id.btn_enviar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                envianome();
            }
        });

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String name = preferences.getString("name", "");
        String email = preferences.getString("email", "");

        EditText input2 = (EditText) findViewById(R.id.input_nome);
        EditText input1 = (EditText) findViewById(R.id.input_email);
        input1.setText(email);
        input2.setText(name);
    }
    public void envianome()
    {
        EditText input2 = (EditText) findViewById(R.id.input_nome);
        String nome = input2.getText().toString();
        if(nome.equals("")||nome.isEmpty())
        {
            fb.mensagemerro(getString(R.string.cadastro_pos2), getString(R.string.cadastro_pos3),ct1);
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", nome);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.esqueci_pos1));
        progress.setMessage(getString(R.string.esqueci_pos2));
        progress.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"changename?token="+token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progress.dismiss();
                        fb.mensagemerro(getString(R.string.nomeemail_pos_0), getString(R.string.nomeemail_pos_1), ct1);
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

