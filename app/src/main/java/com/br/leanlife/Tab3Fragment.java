package com.br.leanlife;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.br.leanlife.models.Treino;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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


public class Tab3Fragment extends Fragment{

    int value;
    Tab3Fragment minhatab;

    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    RequestQueue requestQueue;
    public Tab3Fragment() {
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
        return inflater.inflate(R.layout.tab3, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        LinearLayout l1 = (LinearLayout) view.findViewById(R.id.botaosair);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deslogar();
            }
        });

        LinearLayout l1b = (LinearLayout) view.findViewById(R.id.botao1);
        l1b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), NomeEmailActivity.class);
                startActivity(myIntent);

            }
        });

        LinearLayout l2b = (LinearLayout) view.findViewById(R.id.botao2);
        l2b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity meu1 = (MainActivity) getActivity();
                meu1.selectImage();
            }
        });
        LinearLayout l3b = (LinearLayout) view.findViewById(R.id.botao3);
        l3b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), CronometroActivity.class);
                startActivity(myIntent);

            }
        });
        LinearLayout l4b = (LinearLayout) view.findViewById(R.id.botao4);
        l4b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), IdiomaActivity.class);
                startActivity(myIntent);

            }
        });
        LinearLayout l5b = (LinearLayout) view.findViewById(R.id.botao5);
        l5b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getActivity(), PlanoActivity.class);
                startActivity(myIntent);

            }
        });

        LinearLayout l6b = (LinearLayout) view.findViewById(R.id.botao6);
        l6b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), ShareActivity.class);
                startActivity(myIntent);

            }
        });

        LinearLayout l7b = (LinearLayout) view.findViewById(R.id.botao7);
        l7b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(myIntent);

            }
        });

        LinearLayout l8b = (LinearLayout) view.findViewById(R.id.botao8);
        l8b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.br.leanlife"));
                startActivity(browserIntent);
            }
        });

        LinearLayout l9b = (LinearLayout) view.findViewById(R.id.botao9);
        l9b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://leanlifeapp.com/terms.html"));
                startActivity(browserIntent);
            }
        });

        LinearLayout l10b = (LinearLayout) view.findViewById(R.id.botao10);
        l10b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://leanlifeapp.com/privacy.html"));
                startActivity(browserIntent);
            }
        });

        inicia();
    }
    public void deslogar()
    {
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        List<Treino> treinosx = new ArrayList<Treino>();
        DatabaseHandler db = new DatabaseHandler(getActivity());
        treinosx = db.getAllTreinos();

        Gson gson = new Gson();

        String treinosjson = "[]";
        String treinosjson2 = gson.toJson(treinosx);
        if(!treinosjson2.equals("") && !treinosjson2.equals("NULL") && !treinosjson2.equals("null"))
        {
            treinosjson = treinosjson2;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("treinos", treinosjson);

        progress = new ProgressDialog(getActivity());
        progress.setTitle(getString(R.string.esqueci_pos1));
        progress.setMessage(getString(R.string.esqueci_pos2));
        progress.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"logout?token=" + token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //salvando sessão

                        progress.dismiss();
                        SharedPreferences preferences =
                                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token", "");
                        editor.putString("name", "" );
                        editor.putString("id", "" );
                        editor.putString("email", "" );
                        editor.putString("photo", "" );
                        editor.putString("treinos", "" );
                        editor.putString("step2", "" );
                        editor.putString("step3", "" );
                        editor.putString("step4_1", "" );
                        editor.putString("step4_2", "" );
                        editor.putString("step4_3", "" );
                        editor.putString("step4_4", "" );
                        editor.putString("step4_5", "" );
                        editor.putString("step5_1", "" );
                        editor.putString("step5_2", "" );
                        editor.putString("step5_2b", "" );
                        editor.putString("step5_3", "" );
                        editor.putString("step5_3b", "" );
                        editor.putString("modificoutreino", "" );
                        editor.putString("trocou", "" );
                        editor.putString("preenchido", "" );
                        editor.putString("meuplano", "" );
                        editor.putString("expiracaomeuplano", "" );
                        editor.commit();

                        Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(myIntent);
                        getActivity().finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        fb.mensagemerro(getString(R.string.step6_pos1), getString(R.string.step6_pos2), getActivity());
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
        View view = getView();
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String meuplano = preferences.getString("meuplano", "");
        String expiracaomeuplano = preferences.getString("expiracaomeuplano", "");
        String photo = preferences.getString("photo", "");
        String vibrate = preferences.getString("vibrate", "");
        String language = preferences.getString("language", "");

        ImageView mImage = (ImageView) getView().findViewById(R.id.foto2);
        if(photo != null && !photo.equals("") && !photo.equals("null"))
        {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .displayer(new RoundedBitmapDisplayer(300))
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .build();
            imageLoader.displayImage(photo, mImage, options);
        }
        else
        {
            // create bitmap from resource
            Bitmap bm = BitmapFactory.decodeResource(getResources(),
                    R.drawable.photo);
            // set circle bitmap
            mImage.setImageBitmap(getCircleBitmap(bm));
        }

        TextView t2 = (TextView) getView().findViewById(R.id.texto2);
        if(vibrate.equals("1"))
        {
            t2.setText(getString(R.string.cronometro_1));
        }
        else if(vibrate.equals("2"))
        {
            t2.setText(getString(R.string.cronometro_2));
        }
        else if(vibrate.equals("3"))
        {
            t2.setText(getString(R.string.cronometro_3));
        }

        TextView t3 = (TextView) getView().findViewById(R.id.texto3);
        if(language.equals("1"))
        {
            t3.setText(getString(R.string.idioma_1));
        }
        else if(language.equals("2"))
        {
            t3.setText(getString(R.string.idioma_2));
        }
        else if(language.equals("3"))
        {
            t3.setText(getString(R.string.idioma_3));
        }

        pegadados();
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
    public void pegadados()
    {

        /*progress = new ProgressDialog(getActivity());
        progress.setTitle(getString(R.string.nomeemail_pos_4));
        progress.setMessage(getString(R.string.nomeemail_pos_5));
        progress.show();*/

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
                                TextView textofree = (TextView) getActivity().findViewById(R.id.textofree);
                                String dataexp = item.getString("data_max_free");

                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                Date dataagora = new Date();
                                Date datafree = new Date();
                                try {
                                    datafree = df.parse(dataexp);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");

                                if(dataagora.compareTo(datafree) > 0)
                                {
                                    textofree.setText(getString(R.string.tab3_free2));
                                }
                                else
                                {
                                    textofree.setText(getString(R.string.tab3_free1)+" "+df2.format(datafree));
                                }

                                JSONArray objs = item.getJSONArray("planosmaisnovos");
                                for(int i=0;i<objs.length();i++)
                                {
                                    JSONObject planof = objs.getJSONObject(i);
                                    String planoid = planof.getString("plan_id");
                                    String expiracao = planof.getString("expiracao");

                                    if(getView() != null)
                                    {
                                        TextView t4 = (TextView) getView().findViewById(R.id.texto4);
                                        t4.setText(fb.convertplanobonito(planoid,getActivity()));
                                    }


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

}