package com.br.leanlife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.RelativeLayout;
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
public class Step6Activity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step6);
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
                Step6Activity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_step6",
                screenView
        );

        ImageView fab = (ImageView) findViewById(R.id.volta1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RelativeLayout fab2 = (RelativeLayout) findViewById(R.id.frente1);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviatudo();
            }
        });

        TextView t1 = (TextView) findViewById(R.id.link1);
        TextView t2 = (TextView) findViewById(R.id.link2);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://leanlifeapp.com/terms.html"));
                startActivity(browserIntent);
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://leanlifeapp.com/privacy.html"));
                startActivity(browserIntent);
            }
        });
    }
    public void enviatudo()
    {
        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String step2 = preferences.getString("step2", "");
        String step3 = preferences.getString("step3", "");
        String step4_1 = preferences.getString("step4_1", "");
        String step4_2 = preferences.getString("step4_2", "");
        String step4_3 = preferences.getString("step4_3", "");
        String step4_4 = preferences.getString("step4_4", "");
        String step4_5 = preferences.getString("step4_5", "");
        String step5_1 = preferences.getString("step5_1", "");
        String step5_2 = preferences.getString("step5_2", "");
        String step5_2b = preferences.getString("step5_2b", "");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id_main_goal", step2);
        params.put("id_activity_level", step3);
        params.put("gender", step4_1);
        params.put("birthday", step4_2);
        params.put("type_weight", step4_3);
        params.put("height", step4_4);
        params.put("weight", step4_5);
        params.put("minutes_per_workout", step5_1);

        String[] separated = step5_2.split(",");
        for(int i=0; i<separated.length; i++)
        {
            String strf = separated[i];
            params.put("emphasize["+i+"]", strf);
        }
        params.put("contemp1", ""+separated.length);
        params.put("contemp2", "0");

        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.esqueci_pos1));
        progress.setMessage(getString(R.string.esqueci_pos2));
        progress.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"profile?token=" + token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progress.dismiss();
                        //salvando sessão
                        SharedPreferences preferences =
                                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("preenchido", "1");
                        editor.commit();

                        Intent myIntent = new Intent(Step6Activity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        fb.mensagemerro(getString(R.string.step6_pos1), getString(R.string.step6_pos2), ct1);
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
