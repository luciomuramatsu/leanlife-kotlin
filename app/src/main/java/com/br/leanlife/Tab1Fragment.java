package com.br.leanlife;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.br.leanlife.models.Exercicio;
import com.br.leanlife.models.Exercise;
import com.br.leanlife.models.Group_exercise;
import com.br.leanlife.models.Muscular_group;
import com.br.leanlife.models.Treino;
import com.br.leanlife.models.Workout;


public class Tab1Fragment extends Fragment{

    int value;
    Tab1Fragment minhatab;

    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    int megacontador = 0;
    int treinos_free = 0;
    int permissionf = 2;
    String data_max_free = "";
    String primeiravez = "1";
    List<Integer> nexiste1 = new ArrayList<Integer>();
    List<Integer> nexiste2 = new ArrayList<Integer>();
    List<Integer> nexiste3 = new ArrayList<Integer>();
    int contadornexiste = 0;

    int ciclox = 0;
    int estagiox = 0;
    int sessaox = 0;

    CharSequence[] meusitens1 = null;
    CharSequence[] meusitens2 = null;

    int fezprimeirotreino = 0;

    List<Treino> treinos = new ArrayList<Treino>();
    RequestQueue requestQueue;
    public Tab1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab1, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        /*LinearLayout aviso1 = (LinearLayout) getActivity().findViewById(R.id.paginaaviso);
        aviso1.setVisibility(View.GONE);*/

        //slideDown(aviso1);
        pegadadosPlanos();
        getconfig();
        fazaparecermsg();

        meusitens1 = new CharSequence[]{getString(R.string.tab1_8), getString(R.string.tab1_9),
                getString(R.string.tab1_10)};

        meusitens2 = new CharSequence[]{getString(R.string.tab1_12), getString(R.string.tab1_13),
                getString(R.string.tab1_14)};

        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String modificoutreino = preferences.getString("modificoutreino", "");
        trocaidioma();
        if(modificoutreino.equals("1") || primeiravez.equals("1"))
        {
            if(getView() != null) {
                inicia();
            }
        }
        else
        {
            if(getView() != null)
            {
                inicia2();
            }
        }

        /*TextView msgaviso1 = (TextView) getActivity().findViewById(R.id.msgpaginaaviso);
        ImageView imgaviso1 = (ImageView) getActivity().findViewById(R.id.closepaginaaviso);
        imgaviso1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LinearLayout aviso1 = (LinearLayout) getActivity().findViewById(R.id.paginaaviso);
                //aviso1.setVisibility(View.GONE);
                slideDown(aviso1);
            }
        });
        aviso1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                vaiaviso(0);
            }
        });*/

    }
    /*public void vaiaviso(int tipo)
    {
        LinearLayout aviso1 = (LinearLayout) getActivity().findViewById(R.id.paginaaviso);
        TextView msgaviso1 = (TextView) getActivity().findViewById(R.id.msgpaginaaviso);
        ImageView imgaviso1 = (ImageView) getActivity().findViewById(R.id.closepaginaaviso);
        slideDown(aviso1);

        Intent myIntent = new Intent(getActivity(), ChatBotActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("numeroaviso", "" + tipo);
        myIntent.putExtras(mBundle);
        startActivity(myIntent);
    }
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);

        view.animate()
                .translationY(view.getHeight())
                .alpha(1.0f)
                .setListener(null);
    }
    public void slideDown(View view){
        view.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LinearLayout aviso1 = (LinearLayout) getActivity().findViewById(R.id.paginaaviso);
                aviso1.setVisibility(View.GONE);
            }
        });
    }*/
    public void getconfig()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, urlf+"getconfig" ,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            if(getActivity() != null) {
                                JSONArray item = response.getJSONArray("data");
                                if (item.length() > 0) {
                                    JSONObject item1 = item.getJSONObject(0);
                                    String message1 = item1.getString("message1");
                                    String message2 = item1.getString("message2");
                                    String message3 = item1.getString("message3");

                                    SharedPreferences preferences =
                                            getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("message1", message1);
                                    editor.putString("message2", message2);
                                    editor.putString("message3", message3);
                                    editor.commit();
                                }
                            }

                            //progress.dismiss();

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            //progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progress.dismiss();
                        //fb.mensagemerro(getString(R.string.nomeemail_pos_4), getString(R.string.nomeemail_pos_5),getActivity());
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    public void trocaidioma()
    {

        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String language = preferences.getString("language", "");
        String token = preferences.getString("token", "");

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("language", language);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"trocaidioma?token="+token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
        if(progress != null)
        {
            progress.dismiss();
        }
        progress = new ProgressDialog(getActivity());
        progress.setTitle(getString(R.string.nomeemail_pos_4));
        progress.setMessage(getString(R.string.nomeemail_pos_5));
        progress.show();


        //Log.d("DDD","po");
        DatabaseHandler db = new DatabaseHandler(getActivity());
        treinos = db.getAllTreinos();

        Gson gson = new Gson();

        String treinosjson = "[]";
        String treinosjson2 = gson.toJson(treinos);
        if(!treinosjson2.equals("") && !treinosjson2.equals("NULL") && !treinosjson2.equals("null"))
        {
            treinosjson = treinosjson2;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("treinos", treinosjson);

        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"gettreinos?token="+token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            if(getView() != null) {
                                JSONObject item = response.getJSONObject("data");
                                treinos_free = item.getInt("treinos_free");
                                data_max_free = item.getString("data_max_free");
                                permissionf = item.getInt("permission_user");

                                salvatreino(item);


                                SharedPreferences preferences =
                                        getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("modificoutreino", "0");
                                editor.commit();

                                primeiravez = "1";
                                inicia2();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(getView() != null) {
                            inicia2();
                            progress.dismiss();
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
    public void inicia2()
    {

        fezprimeirotreino = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dataagora = new Date();
        Date datafree = new Date();
        try {
            datafree = df.parse(data_max_free);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String modificoutreino = preferences.getString("modificoutreino", "");

        if(modificoutreino.equals("1") || primeiravez.equals("1"))
        {
            DatabaseHandler db = new DatabaseHandler(getActivity());
            treinos = db.getAllTreinossimplebase();
        }
        else
        {

        }

        primeiravez = "0";
        int x = 0;
        contadornexiste = 0;
        if(treinos.size() <= 0)
        {
            return;
        }

        int numciclo = treinos.get(0).cycle_number;
        String primeirotreino = "1";

        int ultimoestagio = -1;
        for(int i =0; i<treinos.size(); i++)
        {
            Treino treinof = treinos.get(i);
            int idf = treinof.id;
            int status = treinof.flag_status;
            int itemciclo = treinof.cycle_number;
            int itemstage = treinof.stage_number;
            int itemsession = treinof.session_number;
            if(itemciclo == numciclo)
            {
                if(ultimoestagio < itemstage)
                {
                    ultimoestagio = itemstage;
                }
            }
        }




        String acaboutreinos = "1";
        for(int i =0; i<treinos.size(); i++)
        {
            Treino treinof = treinos.get(i);
            int statustreino = treinof.flag_status;
            if(statustreino == 0)
            {
                acaboutreinos = "0";
            }
        }

        String naotemplano = "1";
        preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String meuplano = preferences.getString("meuplano", "");
        if(!meuplano.equals("") && !meuplano.equals("null"))
        {
            naotemplano = "0";
        }

        String temunlock = "0";
        if(dataagora.compareTo(datafree) > 0 && naotemplano.equals("1"))
        {
            temunlock = "1";
        }

        int acrescenta = 0;
        if(acaboutreinos.equals("1") || temunlock.equals("1"))
        {
            acrescenta = 1;
        }

        LinearLayout ll1 = (LinearLayout) getView().findViewById(R.id.bloco1);
        ll1.removeAllViews();
        for(int i = numciclo + acrescenta; i>=1; i--)
        {
            LinearLayout v1 = new LinearLayout(getActivity());
            v1.setOrientation(LinearLayout.VERTICAL);
            v1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView tx1 = new TextView(getActivity()); // Pass it an Activity or Context
            tx1.setText("-- LeanCycle "+i+" --");

            tx1.setTypeface(null, Typeface.BOLD);
            tx1.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 40, 10, 10);
            tx1.setLayoutParams(layoutParams);

            TextView tx2 = new TextView(getActivity()); // Pass it an Activity or Context
            if(i == 1)
            {
                tx2.setText(R.string.tab1_1);
            }
            else
            {
                tx2.setText(R.string.tab1_2);
            }

            tx2.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(10, 20, 10, 20);
            tx2.setLayoutParams(layoutParams2);

            v1.addView(tx1);
            v1.addView(tx2);
            if(temunlock.equals("1") && i== numciclo + acrescenta)
            {
                //bloco de UNLOCK
                tx1.setText("");
                tx1.setVisibility(View.GONE);
                tx2.setText(getString(R.string.tab1_4));

                Button btn1 = new Button(getActivity()); // Pass it an Activity or Context
                btn1.setText(R.string.tab1_6);
                btn1.setBackgroundColor(Color.parseColor("#FF0000"));
                btn1.setTextColor(Color.parseColor("#FFFFFF"));
                btn1.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams3.setMargins(40, 20, 40, 20);
                btn1.setLayoutParams(layoutParams3);
                btn1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        unlock();
                    }
                });
                v1.addView(btn1);
            }
			else if(acaboutreinos.equals("1") && i == numciclo + acrescenta)
            {
                //bloco de novo ciclo
                tx1.setText("");
                tx1.setVisibility(View.GONE);
                tx2.setText(getString(R.string.tab1_3));

                Button btn1 = new Button(getActivity()); // Pass it an Activity or Context
                btn1.setText(R.string.tab1_5);
                btn1.setBackgroundColor(Color.parseColor("#FF0000"));
                btn1.setTextColor(Color.parseColor("#FFFFFF"));
                btn1.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams3.setMargins(40, 20, 40, 20);
                btn1.setLayoutParams(layoutParams3);
                btn1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        novociclo();
                    }
                });
                v1.addView(btn1);
            }
            else
            {
                for(int j=1; j<=4; j++)
                {
                    LinearLayout v1b = new LinearLayout(getActivity());
                    v1b.setOrientation(LinearLayout.HORIZONTAL);
                    v1b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                    for(int k=1; k<=4; k++)
                    {

                        LinearLayout v1c = new LinearLayout(getActivity());
                        v1c.setOrientation(LinearLayout.VERTICAL);
                        v1c.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
                        v1c.setGravity(Gravity.CENTER);

                        RelativeLayout blocoimg1x = new RelativeLayout(getActivity()); // Pass it an Activity or Context
                        blocoimg1x.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        blocoimg1x.setGravity(Gravity.CENTER);
                        v1c.addView(blocoimg1x);

                        ImageView img1x = new ImageView(getActivity()); // Pass it an Activity or Context
                        img1x.setImageResource(R.drawable.treino_past);
                        img1x.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
                        blocoimg1x.addView(img1x);

                        ImageView img1xletra = new ImageView(getActivity()); // Pass it an Activity or Context
                        img1xletra.setImageResource(R.drawable.letra_a);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(50, 50, Gravity.TOP|Gravity.END);
                        lp.setMargins(110,0, 0, 0);
                        img1xletra.setLayoutParams(lp);
                        img1xletra.setAdjustViewBounds(true);


                        String encontroutreino = "0";
                        for(int i1 =0; i1<treinos.size(); i1++)
                        {
                            Treino treinof = treinos.get(i1);
                            int idf = treinof.id;
                            int status = treinof.flag_status;
                            int itemciclo = treinof.cycle_number;
                            int itemstage = treinof.stage_number;
                            int itemsession = treinof.session_number;
                            String train_type = treinof.train_type;
                            if(i == itemciclo && j == itemstage && k == itemsession)
                            {
                                encontroutreino = "1";
                                if(i == numciclo && j == ultimoestagio)
                                {
                                    if(status == 0 && temunlock.equals("0"))
                                    {
                                        img1x.setTag(idf);
                                        img1x.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                Intent myIntent = new Intent(getActivity(), TreinosActivity.class);
                                                Bundle mBundle = new Bundle();
                                                mBundle.putString("ulttreinoclick", v.getTag().toString());
                                                mBundle.putString("podemodificar", "1");
                                                mBundle.putString("abreespecial", "0");
                                                myIntent.putExtras(mBundle);
                                                startActivity(myIntent);
                                            }
                                        });


                                    }
                                    else if(status == 0 && temunlock.equals("1"))
                                    {

                                    }
                                    else
                                    {
                                        img1x.setTag(idf);
                                        final String finalTemunlock = temunlock;
                                        img1x.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                Intent myIntent = new Intent(getActivity(), CongratActivity.class);
                                                Bundle mBundle = new Bundle();
                                                mBundle.putString("ulttreinoclick", v.getTag().toString());
                                                if(finalTemunlock.equals("1"))
                                                {
                                                    mBundle.putString("podemodificar", "0");
                                                }
                                                else
                                                {
                                                    mBundle.putString("podemodificar", "1");
                                                }

                                                mBundle.putString("abreespecial", "1");
                                                myIntent.putExtras(mBundle);
                                                startActivity(myIntent);
                                            }
                                        });
                                    }
                                }
                                else
                                {
                                    nexiste1.add(i);
                                    nexiste2.add(j);
                                    nexiste3.add(k);
                                    img1x.setTag(contadornexiste);
                                    img1x.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            puxadobanco((int) v.getTag());
                                        }
                                    });
                                    contadornexiste++;
                                }

                                if(status == 0)
                                {
                                    //não foi treinado
                                    if(temunlock.equals("1"))
                                    {
                                        img1x.setImageResource(R.drawable.treino_bloq);
                                    }
                                    else if(primeirotreino.equals("1"))
                                    {
                                        //treino GO
                                        img1x.setImageResource(R.drawable.treino_go);
                                        primeirotreino = "0";
                                    }
                                    else
                                    {
                                        // treino futuro
                                        img1x.setImageResource(R.drawable.proximotreino);
                                    }
                                }
                                else if(status == 1)
                                {
                                    //foi treinado
                                    img1x.setImageResource(R.drawable.treino_past);
                                    TextView tx2b = new TextView(getActivity()); // Pass it an Activity or Context
                                    tx2b.setText("Done");
                                    tx2b.setTextSize(12);
                                    tx2b.setGravity(Gravity.CENTER);
                                    tx2b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                                    v1c.addView(tx2b);

                                    fezprimeirotreino = 1;
                                }

                                boolean entrouletra = false;
                                if(train_type.equals("A"))
                                {
                                    img1xletra.setImageResource(R.drawable.letra_a);
                                    entrouletra = true;
                                }
                                else if(train_type.equals("B"))
                                {
                                    img1xletra.setImageResource(R.drawable.letra_b);
                                    entrouletra = true;
                                }
                                else if(train_type.equals("C"))
                                {
                                    img1xletra.setImageResource(R.drawable.letra_c);
                                    entrouletra = true;
                                }
                                else if(train_type.equals("D"))
                                {
                                    img1xletra.setImageResource(R.drawable.letra_d);
                                    entrouletra = true;
                                }
                                if(entrouletra)     blocoimg1x.addView(img1xletra);
                            }
                        }
                        if(encontroutreino.equals("0"))
                        {
                            if(numciclo > i || (numciclo == i && ultimoestagio > j))
                            {

                                fezprimeirotreino = 1;

                                img1x.setImageResource(R.drawable.treino_past);
                                TextView tx2b = new TextView(getActivity()); // Pass it an Activity or Context
                                tx2b.setText("Done");
                                tx2b.setTextSize(12);
                                tx2b.setGravity(Gravity.CENTER);
                                tx2b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                                v1c.addView(tx2b);

                                nexiste1.add(i);
                                nexiste2.add(j);
                                nexiste3.add(k);
                                img1x.setTag(contadornexiste);
                                img1x.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        puxadobanco((int) v.getTag());
                                    }
                                });
                                contadornexiste++;
                            }
                            else
                            {
                                img1x.setImageResource(R.drawable.treino_bloq);
                            }
                        }






                        v1b.addView(v1c);
                    }

                    RelativeLayout linha = new RelativeLayout(getActivity());
                    linha.setBackgroundColor(Color.GRAY);
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    layoutParams3.setMargins(0, 20, 0, 20);
                    linha.setLayoutParams(layoutParams3);

                    v1.addView(v1b);
                    v1.addView(linha);
                }
            }



            ll1.addView(v1);
        }

        /*if(fezprimeirotreino == 0)
        {

        }*/


    }
    public void fazaparecermsg()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, urlf+"gettooltip" ,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            if(getActivity() != null) {
                                JSONArray item = response.getJSONArray("data");
                                if (item.length() > 0) {
                                    JSONObject item1 = item.getJSONObject(0);
                                    String message1 = item1.getString("mensagempt");
                                    String message2 = item1.getString("mensagemen");
                                    String message3 = item1.getString("mensagemes");
                                    String titulo1 = item1.getString("titulopt");
                                    String titulo2 = item1.getString("tituloen");
                                    String titulo3 = item1.getString("tituloes");
                                    String resp1en = item1.getString("resp1en");
                                    String resp1es = item1.getString("resp1es");
                                    String resp1pt = item1.getString("resp1pt");
                                    String resp2en = item1.getString("resp2en");
                                    String resp2pt = item1.getString("resp2pt");
                                    String resp2es = item1.getString("resp2es");
                                    String resp3en = item1.getString("resp3en");
                                    String resp3pt = item1.getString("resp3pt");
                                    String resp3es = item1.getString("resp3es");

                                    String respopt1en = item1.getString("respopt1en");
                                    String respopt1es = item1.getString("respopt1es");
                                    String respopt1pt = item1.getString("respopt1pt");
                                    String respopt2en = item1.getString("respopt2en");
                                    String respopt2pt = item1.getString("respopt2pt");
                                    String respopt2es = item1.getString("respopt2es");
                                    String respopt3en = item1.getString("respopt3en");
                                    String respopt3pt = item1.getString("respopt3pt");
                                    String respopt3es = item1.getString("respopt3es");

                                    SharedPreferences preferences =
                                            getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("messagepttooltip", message1);
                                    editor.putString("messageentooltip", message2);
                                    editor.putString("messageestooltip", message3);
                                    editor.putString("resp1en", resp1en);
                                    editor.putString("resp1es", resp1es);
                                    editor.putString("resp1pt", resp1pt);
                                    editor.putString("resp2en", resp2en);
                                    editor.putString("resp2pt", resp2pt);
                                    editor.putString("resp2es", resp2es);
                                    editor.putString("resp3en", resp3en);
                                    editor.putString("resp3pt", resp3pt);
                                    editor.putString("resp3es", resp3es);

                                    editor.putString("respopt1en", respopt1en);
                                    editor.putString("respopt1es", respopt1es);
                                    editor.putString("respopt1pt", respopt1pt);
                                    editor.putString("respopt2en", respopt2en);
                                    editor.putString("respopt2pt", respopt2pt);
                                    editor.putString("respopt2es", respopt2es);
                                    editor.putString("respopt3en", respopt3en);
                                    editor.putString("respopt3pt", respopt3pt);
                                    editor.putString("respopt3es", respopt3es);


                                    editor.commit();

                                    /*LinearLayout aviso1 = (LinearLayout) getActivity().findViewById(R.id.paginaaviso);
                                    TextView msgaviso1 = (TextView) getActivity().findViewById(R.id.msgpaginaaviso);
                                    ImageView imgaviso1 = (ImageView) getActivity().findViewById(R.id.closepaginaaviso);
                                    //aviso1.setVisibility(View.VISIBLE);

                                    slideUp(aviso1);

                                    preferences =
                                            getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                    String language = preferences.getString("language", "");
                                    if(language.equals("1"))
                                    {
                                        msgaviso1.setText(titulo2);
                                    }
                                    else if(language.equals("2"))
                                    {
                                        msgaviso1.setText(titulo3);
                                    }
                                    else
                                    {
                                        msgaviso1.setText(titulo1);
                                    }*/


                                }
                            }

                            //progress.dismiss();

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            //progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progress.dismiss();
                        //fb.mensagemerro(getString(R.string.nomeemail_pos_4), getString(R.string.nomeemail_pos_5),getActivity());
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }
    public void novociclo()
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.tab1_11));
        builder.setItems(meusitens2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (item==0) {
                    novociclo2();

                } else if (item==1) {

                    MainActivity m1 = (MainActivity) getActivity();
                    m1.tabLayout.getTabAt(1).select();

                } else if (item==2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void unlock()
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.tab1_7));
        builder.setItems(meusitens1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (item==0) {

                    Intent myIntent = new Intent(getActivity(), ShareActivity.class);
                    startActivity(myIntent);

                } else if (item==1) {

                    Intent myIntent = new Intent(getActivity(), PlanoActivity.class);
                    startActivity(myIntent);

                } else if (item==2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void novociclo2()
    {
        if(progress != null)
        {
            progress.dismiss();
        }
        progress = new ProgressDialog(getActivity());
        progress.setTitle(getString(R.string.nomeemail_pos_4));
        progress.setMessage(getString(R.string.nomeemail_pos_5));
        progress.show();


        DatabaseHandler db = new DatabaseHandler(getActivity());
        treinos = db.getAllTreinos();

        Gson gson = new Gson();

        String treinosjson = "[]";
        String treinosjson2 = gson.toJson(treinos);
        if(!treinosjson2.equals("") && !treinosjson2.equals("NULL") && !treinosjson2.equals("null"))
        {
            treinosjson = treinosjson2;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("treinos", treinosjson);

        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"crianovoestagio?token="+token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(getView() != null) {
                            progress.dismiss();
                            inicia();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(getView() != null) {
                            progress.dismiss();
                            fb.mensagemerro(getString(R.string.nomeemail_pos_2), getString(R.string.nomeemail_pos_3), getActivity());
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
    public void puxadobanco(int numrecebe)
    {
        int idf = numrecebe;
        int idfdotreino = -1;

        ciclox = nexiste1.get(idf);
        estagiox = nexiste2.get(idf);
        sessaox = nexiste3.get(idf);

        idfdotreino = enviatreinonexiste(ciclox,estagiox,sessaox);

        if(idfdotreino == -1)
        {
            if(progress != null)
            {
                progress.dismiss();
            }
            progress = new ProgressDialog(getActivity());
            progress.setTitle(getString(R.string.nomeemail_pos_4));
            progress.setMessage(getString(R.string.nomeemail_pos_5));
            progress.show();


            DatabaseHandler db = new DatabaseHandler(getActivity());
            treinos = db.getAllTreinos();

            Gson gson = new Gson();

            String treinosjson = "[]";
            String treinosjson2 = gson.toJson(treinos);
            if(!treinosjson2.equals("") && !treinosjson2.equals("NULL") && !treinosjson2.equals("null"))
            {
                treinosjson = treinosjson2;
            }
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            Map<String, String> params = new HashMap<String, String>();
            params.put("treinos", treinosjson);

            SharedPreferences preferences =
                    getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
            String token = preferences.getString("token", "");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"gettreinos?ciclo="+ciclox+"&estagio="+estagiox+"&sessao="+sessaox+"&token="+token, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if(getView() != null) {
                                progress.dismiss();
                                try {
                                    JSONObject item = response.getJSONObject("data");
                                    salvatreino(item);

                                    enviatreinonexiste(ciclox, estagiox, sessaox);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(getView() != null) {
                                progress.dismiss();
                                fb.mensagemerro(getString(R.string.nomeemail_pos_2), getString(R.string.nomeemail_pos_3), getActivity());
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
    }
    public int enviatreinonexiste(int ciclo,int estagio,int sessao)
    {
        int idfdotreino = -1;

        DatabaseHandler db = new DatabaseHandler(getActivity());
        treinos = db.getAllTreinossimplebase();
        for(int i1 =0; i1<treinos.size(); i1++)
        {
            Treino treinof = treinos.get(i1);
            int idf = treinof.id;
            int status = treinof.flag_status;
            int itemciclo = treinof.cycle_number;
            int itemstage = treinof.stage_number;
            int itemsession = treinof.session_number;
            if(itemciclo == ciclo && itemstage == estagio && itemsession == sessao)
            {
                idfdotreino = idf;
            }
        }

        if(idfdotreino != -1)
        {
            Intent myIntent = new Intent(getActivity(), CongratActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("ulttreinoclick", ""+idfdotreino);
            mBundle.putString("podemodificar", "0");
            mBundle.putString("abreespecial", "1");
            myIntent.putExtras(mBundle);
            startActivity(myIntent);
        }
        return idfdotreino;
    }
    public void pegadadosPlanos()
    {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, urlf+"profile?id="+value+"&token="+token ,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            JSONObject item = response.getJSONObject("data");

                            if(getView() != null)
                            {

                                JSONArray objs = item.getJSONArray("planosmaisnovos");
                                for(int i=0;i<objs.length();i++)
                                {
                                    JSONObject planof = objs.getJSONObject(i);
                                    String planoid = planof.getString("plan_id");
                                    String expiracao = planof.getString("expiracao");
                                    SharedPreferences preferences =
                                            getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("meuplano", planoid);
                                    editor.putString("expiracaomeuplano", expiracao );
                                    editor.commit();
                                    break;
                                }
                            }


                            //progress.dismiss();

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            //progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progress.dismiss();
                        //fb.mensagemerro(getString(R.string.nomeemail_pos_4), getString(R.string.nomeemail_pos_5),getActivity());
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    public void salvatreino(JSONObject item) throws JSONException {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        db.deletaTudo();

        JSONArray exerciciosx = item.getJSONArray("exercicios");
        for(int i=0; i<exerciciosx.length(); i++)
        {
            JSONObject exercise_id__exercise_routine = exerciciosx.getJSONObject(i);
            db.addExercise(new Exercise(exercise_id__exercise_routine.getInt("id"),exercise_id__exercise_routine.getInt("group_exercise_id"),exercise_id__exercise_routine.getString("name_port"),
                    exercise_id__exercise_routine.getString("name_english"),exercise_id__exercise_routine.getString("name_espanish"),exercise_id__exercise_routine.getString("image_mini"),
                    exercise_id__exercise_routine.getString("image_medium"),exercise_id__exercise_routine.getString("image_big"),exercise_id__exercise_routine.getString("gif"),exercise_id__exercise_routine.getString("nickname"),exercise_id__exercise_routine.getString("created_at"),
                    exercise_id__exercise_routine.getString("updated_at"),exercise_id__exercise_routine.getString("exists"),exercise_id__exercise_routine.getString("gif2"),
                    exercise_id__exercise_routine.getString("gif3"),exercise_id__exercise_routine.getString("ranking"),exercise_id__exercise_routine.getString("efficiency"),exercise_id__exercise_routine.getString("difficulty"),exercise_id__exercise_routine.getString("find_exercise")));
        }
        JSONArray treinos = item.getJSONArray("treinos");
        for(int i=0; i<treinos.length(); i++)
        {
            JSONObject treinof = treinos.getJSONObject(i);
            db.addTreinos(new Treino(treinof.getInt("id"),treinof.getInt("training_1_id"),treinof.getInt("training_2_id"),treinof.getInt("users_id"),treinof.getInt("cycle_number"),treinof.getInt("stage_number"),treinof.getInt("session_number"),treinof.getInt("flag_status"),treinof.getString("date_ini"),treinof.getString("date_final"),treinof.getString("free_or_paid"),treinof.getString("created_at"),treinof.getString("updated_at"),treinof.getString("train_type"),treinof.getInt("max_exercises"),null));
            JSONArray exercicios = treinof.getJSONArray("allexercise_routines");
            for(int j=0; j<exercicios.length(); j++)
            {
                JSONObject exerciciof = exercicios.getJSONObject(j);
                db.addExercicio(new Exercicio(exerciciof.getInt("id"),exerciciof.getInt("routine_id"),exerciciof.getInt("group_exercise_id"),exerciciof.getInt("exercise_id"),exerciciof.getInt("rest_seconds"),exerciciof.getInt("series"),exerciciof.getString("date_ini"),exerciciof.getString("date_final"),exerciciof.getInt("flag_status"),exerciciof.getString("created_at"),exerciciof.getString("updated_at"),exerciciof.getString("train_type"),exerciciof.getString("estatreino1"),exerciciof.getString("estatreino2"),exerciciof.getString("estatreino3"),exerciciof.getString("estatreino4"),null,null,null));
                JSONArray allworkouts = exerciciof.getJSONArray("allworkouts");
                for(int k=0; k<allworkouts.length(); k++)
                {
                    JSONObject workoutf = allworkouts.getJSONObject(k);
                    Integer status = 0;
                    if(!workoutf.isNull("status"))
                    {
                        status = Integer.parseInt(workoutf.getString("status"));
                    }

                    Integer type_weight = 0;
                    if(!workoutf.isNull("type_weight"))
                    {
                        type_weight = Integer.parseInt(workoutf.getString("type_weight"));
                    }

                    Float weight = 0.0f;
                    if(!workoutf.isNull("weight"))
                    {
                        weight = Float.valueOf(String.valueOf(workoutf.getDouble("weight")));
                    }

                    db.addworkout(new Workout(workoutf.getInt("id"),workoutf.getInt("exercise_routine_id"),workoutf.getString("reps_estimated"),workoutf.getString("reps_real"),workoutf.getInt("velocity"),weight,type_weight,status,workoutf.getString("date_ini"),workoutf.getString("date_final"),workoutf.getInt("flag_status"),workoutf.getString("created_at"),workoutf.getString("updated_at")));
                }

                JSONObject group_exercise_id__exercise_routine = exerciciof.getJSONObject("group_exercise_id__exercise_routine");
                db.addgroupexercise(new Group_exercise(group_exercise_id__exercise_routine.getInt("id"),group_exercise_id__exercise_routine.getInt("muscular_group_id"),group_exercise_id__exercise_routine.getString("name_port"),
                        group_exercise_id__exercise_routine.getString("name_english"),group_exercise_id__exercise_routine.getString("name_espanish"),group_exercise_id__exercise_routine.getString("nickname"),group_exercise_id__exercise_routine.getString("ranking"),group_exercise_id__exercise_routine.getString("repititions_session"),
                        group_exercise_id__exercise_routine.getString("exists"),group_exercise_id__exercise_routine.getString("created_at"),group_exercise_id__exercise_routine.getString("updated_at"),null));

                JSONObject muscular_group_id__group_exercise = group_exercise_id__exercise_routine.getJSONObject("muscular_group_id__group_exercise");
                db.addmusculargroup(new Muscular_group(muscular_group_id__group_exercise.getInt("id"),muscular_group_id__group_exercise.getString("name_port"),muscular_group_id__group_exercise.getString("name_english"),muscular_group_id__group_exercise.getString("name_espanish"),muscular_group_id__group_exercise.getString("nickname"),muscular_group_id__group_exercise.getString("exists"),
                        muscular_group_id__group_exercise.getString("created_at"),muscular_group_id__group_exercise.getString("updated_at"),muscular_group_id__group_exercise.getString("genericmuscular"),muscular_group_id__group_exercise.getString("urlicon")));

                /*JSONObject exercise_id__exercise_routine = exerciciof.getJSONObject("exercise_id__exercise_routine");
                db.addExercise(new Exercise(exercise_id__exercise_routine.getInt("id"),exercise_id__exercise_routine.getInt("group_exercise_id"),exercise_id__exercise_routine.getString("name_port"),
                        exercise_id__exercise_routine.getString("name_english"),exercise_id__exercise_routine.getString("name_espanish"),exercise_id__exercise_routine.getString("image_mini"),
                        exercise_id__exercise_routine.getString("image_medium"),exercise_id__exercise_routine.getString("image_big"),exercise_id__exercise_routine.getString("gif"),exercise_id__exercise_routine.getString("nickname"),exercise_id__exercise_routine.getString("created_at"),
                        exercise_id__exercise_routine.getString("updated_at"),exercise_id__exercise_routine.getString("exists"),exercise_id__exercise_routine.getString("gif2"),
                        exercise_id__exercise_routine.getString("gif3"),exercise_id__exercise_routine.getString("ranking"),exercise_id__exercise_routine.getString("efficiency"),exercise_id__exercise_routine.getString("difficulty")));*/
            }
        }
    }
}