package com.br.leanlife;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

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


public class Tab4Fragment extends Fragment{

    private BottomBar mBottomBar;
    String urlf = "https://leanlifeapp.com/api/public/";
    private RelativeLayout mainLayout;
    private PieChart mChart;

    private RelativeLayout mainLayout2;
    private BarChart mChart2;

    public Tab4Fragment() {
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
        return inflater.inflate(R.layout.tab4, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        mBottomBar = BottomBar.attach(view.findViewById(R.id.bottombar1), savedInstanceState);

        if(Build.VERSION.SDK_INT == 23){
            mBottomBar.noNavBarGoodness();
        }else{
            mBottomBar.noNavBarGoodness();

        }

        mBottomBar.setItemsFromMenu(R.menu.bottom_menu2, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                LinearLayout l1 = (LinearLayout) getView().findViewById(R.id.corpo1);
                LinearLayout l2 = (LinearLayout) getView().findViewById(R.id.corpo2);
                if (menuItemId == R.id.bottomBarItemOne) {
                    l1.setVisibility(LinearLayout.VISIBLE);
                    l2.setVisibility(LinearLayout.GONE);
                }
                else if (menuItemId == R.id.bottomBarItemTwo) {
                    l2.setVisibility(LinearLayout.VISIBLE);
                    l1.setVisibility(LinearLayout.GONE);
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.

        mBottomBar.setActiveTabColor("#ff0000");

        //gettrofeis();

        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.photo);
        ImageView mImage = (ImageView) view.findViewById(R.id.image);
        mImage.setImageBitmap(getCircleBitmap(bm));



    }
    public void gettrofeis()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, urlf+"getallconquistas?token="+token ,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            if(getView() != null)
                            {
                                JSONObject user = response.getJSONObject("data");
                                JSONArray trofeis = user.getJSONArray("trofeis");
                                JSONArray desafios = user.getJSONArray("desafios");
                                JSONArray desafioscomplete = user.getJSONArray("desafioscomplete");

                                LinearLayout containermedalhas = getActivity().findViewById(R.id.medalhas);
                                containermedalhas.removeAllViews();
                                for (int i = 0; i < trofeis.length(); i++) {
                                    JSONObject trofeu = trofeis.getJSONObject(i);

                                    LinearLayout v1b = new LinearLayout(getActivity());
                                    v1b.setOrientation(LinearLayout.HORIZONTAL);
                                    v1b.setPadding(5,5,5,5);
                                    v1b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                                    ImageView img1x = new ImageView(getActivity()); // Pass it an Activity or Context
                                    String medalha_trofeu = trofeu.getString("medalha_trofeu");
                                    String feito = trofeu.getString("feito");
                                    if(feito.equals("1"))
                                    {
                                        if(medalha_trofeu != null && !medalha_trofeu.equals("") && !medalha_trofeu.equals("null"))
                                        {
                                            ImageLoader imageLoader = ImageLoader.getInstance();
                                            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                                                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                                                    .build();
                                            imageLoader.displayImage(medalha_trofeu, img1x, options);
                                        }
                                        else
                                        {
                                            // create bitmap from resource
                                            Bitmap bm = BitmapFactory.decodeResource(getResources(),
                                                    R.drawable.medal_full);
                                            // set circle bitmap
                                            img1x.setImageBitmap(bm);
                                        }
                                    }
                                    else
                                    {
                                        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                                                R.drawable.lockcircle);
                                        img1x.setImageBitmap(bm);
                                    }

                                    img1x.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
                                    img1x.setAdjustViewBounds(true);
                                    v1b.addView(img1x);

                                    TextView t1 = new TextView(getActivity()); // Pass it an Activity or Context
                                    SharedPreferences preferences = getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                    String language = preferences.getString("language", "");
                                    String textofinal = "";
                                    if(language.equals("1"))
                                    {
                                        //ingles
                                        textofinal = trofeu.getString("motivo_en");
                                    }
                                    else if(language.equals("2"))
                                    {
                                        //espanhol
                                        textofinal = trofeu.getString("motivo_es");
                                    }
                                    else if(language.equals("3"))
                                    {
                                        //portugues
                                        textofinal = trofeu.getString("motivo_pt");
                                    }
                                    textofinal = textofinal  + "<br/><b>" + trofeu.getString("xpres") + " pts</b>";
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        t1.setText(Html.fromHtml(textofinal, Html.FROM_HTML_MODE_COMPACT));
                                    }
                                    else
                                    {
                                        t1.setText(Html.fromHtml(textofinal));
                                    }
                                    t1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f));
                                    t1.setPadding(15,40,10,10);
                                    v1b.addView(t1);

                                    containermedalhas.addView(v1b);
                                }
                                LinearLayout desafioscontainer = getActivity().findViewById(R.id.desafioscomplete);
                                desafioscontainer.setPadding(0,5,0,5);
                                desafioscontainer.removeAllViews();
                                for (int i = 0; i < desafioscomplete.length(); i++) {
                                    JSONObject desafiof = desafioscomplete.getJSONObject(i);

                                    LinearLayout v1b = new LinearLayout(getActivity());
                                    v1b.setOrientation(LinearLayout.VERTICAL);
                                    v1b.setBackgroundResource(R.drawable.container_meta);
                                    v1b.setPadding(10,10,10,10);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    lp.setMargins(0,0,0,5);
                                    v1b.setLayoutParams(lp);

                                    TextView t1 = new TextView(getActivity()); // Pass it an Activity or Context
                                    SharedPreferences preferences = getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                    String language = preferences.getString("language", "");
                                    if(language.equals("1"))
                                    {
                                        //ingles
                                        t1.setText(desafiof.getString("titulo_en"));
                                    }
                                    else if(language.equals("2"))
                                    {
                                        //espanhol
                                        t1.setText(desafiof.getString("titulo_es"));
                                    }
                                    else if(language.equals("3"))
                                    {
                                        //portugues
                                        t1.setText(desafiof.getString("titulo_pt"));
                                    }
                                    t1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    t1.setPadding(5,5,5,5);
                                    v1b.addView(t1);

                                    TextView t2 = new TextView(getActivity()); // Pass it an Activity or Context
                                    t2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    String xpres = desafiof.getString("xpres");
                                    t2.setText(xpres + "pts");
                                    t2.setTextSize(20f);
                                    t2.setPadding(5,5,5,5);
                                    v1b.addView(t2);

                                    desafioscontainer.addView(v1b);
                                }

                                LinearLayout desafioscontainernew = getActivity().findViewById(R.id.desafios);
                                desafioscontainernew.setPadding(0,5,0,5);
                                desafioscontainernew.removeAllViews();
                                for (int i = 0; i < desafios.length(); i++) {
                                    JSONObject desafiof = desafios.getJSONObject(i);

                                    LinearLayout v1b = new LinearLayout(getActivity());
                                    v1b.setOrientation(LinearLayout.VERTICAL);
                                    v1b.setBackgroundResource(R.drawable.container_meta);
                                    v1b.setPadding(10,10,10,10);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    lp.setMargins(0,0,0,5);
                                    v1b.setLayoutParams(lp);

                                    TextView t1 = new TextView(getActivity()); // Pass it an Activity or Context
                                    SharedPreferences preferences = getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                    String language = preferences.getString("language", "");
                                    if(language.equals("1"))
                                    {
                                        //ingles
                                        t1.setText(desafiof.getString("titulo_en"));
                                    }
                                    else if(language.equals("2"))
                                    {
                                        //espanhol
                                        t1.setText(desafiof.getString("titulo_es"));
                                    }
                                    else if(language.equals("3"))
                                    {
                                        //portugues
                                        t1.setText(desafiof.getString("titulo_pt"));
                                    }
                                    t1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    t1.setPadding(5,5,5,5);
                                    t1.setTextSize(18f);
                                    v1b.addView(t1);

                                    TextView t2 = new TextView(getActivity()); // Pass it an Activity or Context
                                    t2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    String xpres = desafiof.getString("xpres");
                                    t2.setText(xpres + "pts");
                                    t2.setTextSize(20f);
                                    t2.setPadding(5,5,5,5);
                                    v1b.addView(t2);

                                    TextView t3 = new TextView(getActivity()); // Pass it an Activity or Context
                                    t3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    String dia = desafiof.getString("datalimite");
                                    String dias[] = dia.split(" ");
                                    String subdias[] = dias[0].split("-");
                                    dia = subdias[2] + "-" + subdias[1] + "-" + subdias[0];
                                    t3.setText(getString(R.string.desafiobaixo6) + " " + dia);
                                    t3.setTextSize(12f);
                                    t3.setPadding(5,5,5,5);
                                    v1b.addView(t3);



                                    desafioscontainernew.addView(v1b);
                                }

                                TextView nomepessoa = (TextView) getActivity().findViewById(R.id.nomepessoa);
                                nomepessoa.setText(user.getString("name"));

                                ImageView image = (ImageView) getActivity().findViewById(R.id.image);
                                String photo = user.getString("photo");
                                if(photo != null && !photo.equals("") && !photo.equals("null"))
                                {
                                    ImageLoader imageLoader = ImageLoader.getInstance();
                                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                                            .displayer(new RoundedBitmapDisplayer(300))
                                            .cacheOnDisk(true).resetViewBeforeLoading(true)
                                            .build();
                                    imageLoader.displayImage(photo, image, options);
                                }
                                else
                                {
                                    // create bitmap from resource
                                    Bitmap bm = BitmapFactory.decodeResource(getResources(),
                                            R.drawable.photo);
                                    // set circle bitmap
                                    image.setImageBitmap(getCircleBitmap(bm));
                                }

                                String leveltotal = user.getString("leveltotal");
                                TextView level = (TextView) getActivity().findViewById(R.id.level);
                                TextView xp = (TextView) getActivity().findViewById(R.id.xp);
                                TextView ranking = (TextView) getActivity().findViewById(R.id.ranking);

                                String meurankingfinal = user.getString("meurankingfinal");

                                ranking.setText(getString(R.string.desafiobaixo12) + ": " + meurankingfinal);
                                String xptotal = user.getString("xptotal");
                                Integer xptotal2 = Integer.parseInt(xptotal);

                                int lvlfinal = 1;
                                Integer xpfinal = xptotal2;
                                Integer contaux = 1;
                                Integer last = 0;
                                while(true)
                                {
                                    if(xpfinal< last + (contaux)*1000)
                                    {
                                        lvlfinal = contaux;
                                        String xp1 = String.valueOf(xpfinal - last);
                                        String xp2 = String.valueOf((contaux)*1000);
                                        xp.setText("" + xp1 + "pts / "+ xp2 +"pts");
                                        level.setText("Level: " + String.valueOf(lvlfinal) + " | ");

                                        int progresso1 = ( xpfinal - last ) * 100 / (contaux*1000);
                                        ProgressBar mProgress = (ProgressBar) getActivity().findViewById(R.id.progressBar);
                                        mProgress.setProgress( progresso1 );   // Main Progress
                                        mProgress.setMax(100); // Maximum Progress
                                        Drawable dw1 = null;

                                        Resources res = getResources();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            dw1 = res.getDrawable(R.drawable.progress_bar, null);
                                        }
                                        else
                                        {
                                            dw1 = res.getDrawable(R.drawable.progress_bar);
                                        }
                                        mProgress.setProgressDrawable(dw1);

                                        ObjectAnimator animation = ObjectAnimator.ofInt(mProgress, "progress", 0, progresso1);
                                        animation.setDuration(990);
                                        animation.setInterpolator(new DecelerateInterpolator());
                                        animation.start();
                                        break;
                                    }
                                    last = last + (contaux)*1000;
                                    contaux++;
                                }




                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

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
}