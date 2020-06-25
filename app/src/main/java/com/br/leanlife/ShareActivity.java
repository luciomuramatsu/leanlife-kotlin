package com.br.leanlife;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rapha on 05/09/2017.
 */
public class ShareActivity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();
    int contadorbtn = 0;
    ArrayList<LinearLayout> minhalista = new ArrayList<>();

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
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
                ShareActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_share",
                screenView
        );

        LinearLayout btn5 = (LinearLayout) findViewById(R.id.btn_enter1);

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addpergunta();
            }
        });

        addpergunta();

        Button fab = (Button) findViewById(R.id.btn_enviar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                envianome();
            }
        });
    }
    public void addpergunta()
    {
        LinearLayout ll1 = (LinearLayout) findViewById(R.id.container_emails);
        LinearLayout v1 = new LinearLayout(ct1);
        v1.setOrientation(LinearLayout.VERTICAL);
        v1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        v1.setId(contadorbtn*10);

        LinearLayout v1b = new LinearLayout(ct1);
        v1b.setOrientation(LinearLayout.HORIZONTAL);
        v1b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText tx1 = new EditText(ct1); // Pass it an Activity or Context
        tx1.setHint("Email");
        tx1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.85f));
        tx1.setId(contadorbtn*10+2);

        RelativeLayout v1c = new RelativeLayout(ct1);
        v1c.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f));
        v1c.setGravity(Gravity.END);

        ImageView img1x = new ImageView(ct1); // Pass it an Activity or Context
        img1x.setImageResource(R.drawable.trash_icon);
        img1x.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        img1x.setId(contadorbtn * 10 + 1);
        img1x.setTag("" + contadorbtn);
        img1x.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String contaux = (String) v.getTag();
                int contf = Integer.parseInt(contaux);
                LinearLayout lini = (LinearLayout) findViewById(R.id.container_emails);
                LinearLayout lx = (LinearLayout) lini.findViewById(contf * 10);
                lini.removeView(lx);
            }
        });



        v1c.addView(img1x);
        v1b.addView(tx1);
        v1b.addView(v1c);
        v1.addView(v1b);

        ll1.addView(v1);

        minhalista.add(contadorbtn,v1);
        contadorbtn++;
    }

    public void envianome()
    {
        String emailsf = "";

        for (int i = 0; i < contadorbtn; i++) {
            if (findViewById(i * 10 + 2) != null) {
                EditText edt1 = (EditText) findViewById(i * 10 + 2);
                String email = edt1.getText().toString();

                if (email.equals("")) {
                    fb.mensagemerro(getString(R.string.cadastro_pos2), getString(R.string.cadastro_pos4), ct1);
                    return;
                }
                else if(!isEmailValid(email))
                {
                    fb.mensagemerro(getString(R.string.share_pos_0), "Correct the email: " + email, ct1);
                    return;
                }

                if(!emailsf.equals("") )
                {
                    emailsf = emailsf + "," + email;
                }
                else
                {
                    emailsf = email;
                }
            }
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("emails", emailsf);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.esqueci_pos1));
        progress.setMessage(getString(R.string.esqueci_pos2));
        progress.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"enviainvites?token="+token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progress.dismiss();
                        String resp = "1";
                        try {
                            resp = response.getString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(resp.equals("1") )
                        {
                            fb.mensagemerro(getString(R.string.share_pos_1), getString(R.string.share_pos_3), ct1);
                        }
                        else
                        {
                            fb.mensagemerro(getString(R.string.share_pos_1), getString(R.string.share_pos_2), ct1);
                            SharedPreferences preferences = getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("modificoutreino", "1");
                            editor.commit();
                        }

                        //finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 400) {
                            // HTTP Status Code: 400 Unauthorized
                            fb.mensagemerro(getString(R.string.share_pos_0), getString(R.string.share_pos_4), ct1);
                        }
                        else
                        {
                            fb.mensagemerro(getString(R.string.nomeemail_pos_2), getString(R.string.nomeemail_pos_3),ct1);
                        }

                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

