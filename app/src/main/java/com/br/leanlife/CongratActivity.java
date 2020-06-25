package com.br.leanlife;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.br.leanlife.models.Exercicio;
import com.br.leanlife.models.Exercise;
import com.br.leanlife.models.Group_exercise;
import com.br.leanlife.models.Muscular_group;
import com.br.leanlife.models.Treino;
import com.br.leanlife.models.Workout;

/**
 * Created by Raphael on 04/05/2016.
 */
public class CongratActivity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    CharSequence[] frases = {"\"Choosing to be positive and having a grateful attitude is going to determine how you're going to live your life.\" Joel Osteen", "\"Attitude is a little thing that makes a big difference.\" Winston Churchill", "\"Happiness is an attitude of mind, born of the simple determination to be happy under all outward circumstances.\" J. Donald Walters", "\"Take care of your body. It's the only place you have to live.\" Jim Rohn", "\"3 months from now. You will thank yourself.\" ", "\"The only bad workout is the one you didn't do.\" ", "\"Sore muscles. Happy pain.\" ", "\"Once you see results, it becomes an addiction\" ", "\"It never gets easier. You just get better.\" ", "\"It's not about being good at something. It's all about being good to yourself.\" ", "\"Eat clean. Train dirty.\" ", "\"Good habits are as addictive as bad habits. Make your choice.\" ", "\"No matter how slow you go, you are still lapping everybody on the couch.\" ", "\"Stop saying \"tomorrow\". There is no time like the present.\" ", "\"Winners make it happen, losers let it happen.\" ", "\"Forget the scale, forget the measurements. Be the healthiest version of you! Whatever level that is.\" ", "\"The road to success is always under construction.\" ", "\"Forget all the reasons why it won't work and believe the one reason why it will.\" ", "\"If it wasn't hard everyone would do it. It's the hard the makes it great.\" ", "\"When you feel like quitting think about why you started.\" ", "\"Strive for progress not perfection.\" ", "\"Don't compare your chapter 1 to someone else's chapter 20.\" ", "\"Always strive to improve yourself.\" ", "\"Your life does not get better by chance. It gets better my change.\" ", "\"You can't climb the ladder of success with your hands in your pockets.\" ", "\"Don't ever let someone turn your SKY into a ceiling\" ", "\"Do it now. Sometimes 'later' becomes 'never'.\" ", "\"Every morning you have two choices: Continue to sleep with your dreams, or wake up and chase them.\" ", "\"Will it be easy? Nope. Worth it? Absolutely.\" ", "\"Strength doesn't come from what you can do. It comes from overcoming the things you once thought you couldn't.\" ", "\"A pound of muscles weighs the same as a pound of fat. But it's much better looking.\" ", "\"Small daily improvements are the key to staggering long-term results.\" ", "\"You can't spell challenge without change. If you are going to rise to the challenge, you have to be prepared to change.\" ", "\"You are your only competition. Focus on beating your old you's best and can do everything.\" ", "\"Motivation is what gets you started. Habit is what keeps you going.\" Jim Ryun", "\"Get fit in the Gym. Lose weight in the kitchen.\" ", "\"Making a big life change is pretty scary. But, know what's even scarier? Regret.\" ", "\"This is your world. Shape it or someone else will.\" Gary Lew", "\"Every PRO was once an amateur. Every EXPERT was once a beginner. So, dream big and start now.\" ", "\"Eat to meet long-term goals, not short-term satisfaction.\" ", "\"Action always beats perfection.\" ", "\"Columbus traveled the world in a sailboat... And you can't get to the gym?\" ", "\"Stop being afraid of what could go wrong and start being positive about what could go right.\" ", "\"This is not a quick fix. It's a permanent change.\" ", "\"If you hate starting over. Stop quitting.\" ", "\"It takes a dream to get started, desire to keep going and determination to finish.\" Eddie Harris Jr.", "\"If you can't stop thinking about it. Don't stop working for it.\" ", "\"Old ways won't open new doors.\" ", "\"We can't become what we need to be by remaining what we are.\" Oprah Winfrey", "\"If you are persistent, you will get it. If you are consistent, you will keep it.\" ", "\"Every successful person begins with two beliefs: The future can be better than the present, and I have the power to make it so.\" ", "\"It's going to be hard. But hard is not impossible.\" ", "\"Success does not come to you. You go to it.\" ", "\"I already know what giving up feels like. I want to see what happens if I don't.\" ", "\"You may slow down. You may stop for a while. But never quit.\" ", "\"Surround yourself with those who challenge you, push you and motivate you.\" ", "\"Limits, like fear, are often just illusion\" Michael Jordan", "\"Never give up. Everyone has bad days. Pick yourself and keep going.\" ", "\"Amazing things don't happen in your comfort zone.\" ", "\"Don't try to be perfect. Just try to be better than you were yesterday.\" ", "\"If you spend too much time thinking about a thing, you'll never get it done. Make at least one definite move daily toward your goal \" Bruce Lee", "\"If you can't find a way. Create one.\" ", "\"Permanent results come from permanent changes.\" ", "\"The harder you work, the better you get.\" ", "\"Unless you try to do something beyond what you have already mastered, you will never grow.\" ", "\"Many of life's failures are people who did not realize how close they were to success when they gave up.\" ", "\"Some people want it to happen, some wish it would happen, others make it happen.\" Michael Jordan", "\"I may not be there yet. But I'm closer than I was yesterday.\" ", "\"You are awesome because you started\" ", "\"Only those who will risk going too far can possibly find out how far one can go.\" ", "\"Don't limit your challenges, challenge your limits.\" ", "\"A goal without a plan is just a wish.\" ", "\"Don't give up just because you had a bad day. Forgive yourself and do better tomorrow.\" ", "\"Ability is what you are capable of doing. Motivation determines what you do. Attitude determines how well you do it.\" Lou Holtz", "\"Don't stop when you're tired. Stop when you're done.\" ", "\"No matter how many mistakes you make or how slow you progress. You are still way ahead of everyone who isn't trying.\" ", "\"Don't be upset by the results you didn't get with the work you didn't do.\" ", "\"Don't stop trying just because you've hit a wall. Progress is progress, no matter how small.\" ", "\"Don't ever let somebody tell you. 'You can't do something.' You got a dream. You gotta protect it. People can't do something themselves. They wanna tell you, you can't do it. If you want something. Go get it.\" ", "\"Have you noticed that exercising actually gives you more energy than you had before you started?\" ", "\"Good things come to those who work\" ", "\"You don't get what you want. You get what you work for.\" ", "\"Fit is not a destination. It's a way of life.\" ", "\"If we quit every time we felt uncomfortable we wouldn't achieve anything.\" ", "\"You don't have to go fast. You just have to go.\" ", "\"Our bodies are capable of anything. It's our minds we have to convince.\" ", "\"Good food keeps me in good mood.\" ", "\"It doesn't matter what others are doing. It matters what you are doing.\" ", "\"Exercise is the most potent and underutilized antidepressant. And it's for free!\" ", "\"Positive energy. Positive results.\" ", "\"Get fit for life not for summer.\" ", "\"Stop waiting for things to happen. Go out and make them happen.\" ", "\"Believe in yourself and you will be unstoppable.\" ", "\"Be proud of the chances you're making.\" ", "\"Success isn't overnight. It's when every day you get a little better than the day before. It all adds up.\" ", "\"Failure doesn't come from falling down. Failure comes from not getting up.\" ", "\"You don't have to eat less. You just need to eat right.\" ", "\"Exercise not only changes your body. It changes your mind, your attitude and your mood.\" ", "\"Doubt kills more dreams than failure ever will.\" ", "\"The only limitations is in your own mind.\""};
    String value = "-1";
    String podemodificar = "0";
    String abreespecial = "1";
    String origem = "1";
    File ultimafile = null;

    static int GALLERY_PREREQUEST = 1886;
    static int CAMERA_PREREQUEST = 1887;
    static int CAMERA_REQUEST = 1888;
    static int GALLERY_REQUEST = 1889;
    static int CROP_PIC = 1890;
    private Uri picUri;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrat);

        FacebookSdk.sdkInitialize(getApplicationContext());
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
                voltouatras();

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
        // create bitmap from resource
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenView = new Bundle();
        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                CongratActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_congrat",
                screenView
        );

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

        Bundle b = this.getIntent().getExtras();
        value = "-1"; // or other values
        abreespecial = "0";
        if(b != null)
        {
            value = b.getString("ulttreinoclick");
            abreespecial = b.getString("abreespecial");
            podemodificar = b.getString("podemodificar");
        }

        TextView tmeu = (TextView) findViewById(R.id.trofeuf);
        tmeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CongratActivity.this, TreinosActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("ulttreinoclick", ""+value);
                mBundle.putString("podemodificar", podemodificar);
                mBundle.putString("abreespecial", abreespecial);
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
            }
        });
        if(abreespecial.equals("0"))
        {
            tmeu.setVisibility(View.GONE);
        }

        LinearLayout l1 = (LinearLayout) findViewById(R.id.share1);
        ImageView img1 = (ImageView) findViewById(R.id.share2);
        ImageView img2 = (ImageView) findViewById(R.id.share3);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot("2");
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot("1");
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot("2");
            }
        });
        inicia();
    }
    @Override
    public void onBackPressed() {
        voltouatras();
    }
    public void voltouatras()
    {
        if(abreespecial.equals("1"))
        {
            finish();
        }
        else
        {
            SharedPreferences preferences =
                    getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
            String playvideocongrat = preferences.getString("playvideocongrat", "");
            if(!playvideocongrat.equals("1"))
            {
                preferences =
                        getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("playvideocongrat", "1");
                editor.commit();

                Intent myIntent = new Intent(CongratActivity.this, VideoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("grid", 0);
                mBundle.putInt("tipovideo", 5);
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
                finish();
            }
            else
            {
                Intent myIntent = new Intent(CongratActivity.this, MainActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                finish(); // call this to finish the current activity
            }

            /*Intent myIntent = new Intent(CongratActivity.this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish(); // call this to finish the current activity*/
        }

    }

    public void shareface(String print)
    {
        String message = getString(R.string.congrat_7);
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setContentTitle(message)
                .setContentUrl(Uri.parse(print))
                .build();

        ShareDialog.show(CongratActivity.this,shareLinkContent);
    }
    public void sharetwitter()
    {
        String message = getString(R.string.congrat_7);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(ultimafile));

        startActivity(Intent.createChooser(share, "Share this via"));
    }
    private void takeScreenshot(String minhaorigem) {
        origem = minhaorigem;


        if (ActivityCompat.checkSelfPermission(ct1, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((Activity) ct1,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    CongratActivity.GALLERY_PREREQUEST);
        } else {
            takescreenshot2();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == GALLERY_PREREQUEST) {
            if(grantResults!=null&&grantResults.length>0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takescreenshot2();
                }
            }
        }
    }
    public void takescreenshot2()
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            ultimafile = imageFile;
            if(origem.equals("1"))
            {
                sobeimagem(ultimafile);
            }
            else
            {
                sharetwitter();
            }
            //sobeimagem(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    public void sobeimagem(File filef)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();

        //params.put("image[]", fileBody);

        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.nomeemail_pos_4));
        progress.setMessage(getString(R.string.nomeemail_pos_10));
        progress.show();

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        MultipartRequest mr1 = new MultipartRequest(urlf+"imagemsimple",
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //Log.d("xxx", new String(error.networkResponse.data));
                        progress.dismiss();
                        fb.mensagemerro(getString(R.string.nomeemail_pos_2), getString(R.string.nomeemail_pos_3),ct1);
                    }
                },
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progress.dismiss();
                        try
                        {
                            //Log.d("TESTEEE",response);
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("data");

                            String meuprint = jsonArray.get(0).toString();
                            if(origem.equals("1"))
                            {
                                shareface(meuprint);
                            }
                            else
                            {
                                sharetwitter();
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, filef, new HashMap<String,String>() );

        requestQueue.add(mr1);
    }
    public void inicia()
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((frases.length - 1) + 1);

        TextView t1 = (TextView) findViewById(R.id.frasefinal);
        t1.setText(frases[randomNum]);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String photo = preferences.getString("photo", "");
        String name = preferences.getString("name", "");
        String tipopeso = preferences.getString("type_weight", "");

        TextView nome1 = (TextView) findViewById(R.id.nome1);
        nome1.setText(name);

        ImageView img1 = (ImageView) findViewById(R.id.photo1);
        if(photo != null && !photo.equals("") && !photo.equals("null"))
        {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .displayer(new RoundedBitmapDisplayer(300))
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .build();
            imageLoader.displayImage(photo, img1, options);
        }
        else
        {
            // create bitmap from resource
            Bitmap bm = BitmapFactory.decodeResource(getResources(),
                    R.drawable.photo);
            // set circle bitmap
            img1.setImageBitmap(getCircleBitmap(bm));
        }

        DatabaseHandler db = new DatabaseHandler(ct1);
        List<Treino> treinos = db.getAlltreinosimple(value);
        String primeiro = "1";
        for(int i1 =0; i1<treinos.size(); i1++)
        {
            Treino treinof = treinos.get(i1);
            int idf = treinof.id;
            int status = treinof.flag_status;
            int itemciclo = treinof.cycle_number;
            int itemstage = treinof.stage_number;
            int itemsession = treinof.session_number;

            if(idf == Integer.parseInt(value))
            {
                ImageView fundo = (ImageView) findViewById(R.id.fundo);
                if(itemstage == 1)
                {
                    fundo.setImageResource(R.drawable.iconcongrat1);
                }
                else if(itemstage == 2)
                {
                    fundo.setImageResource(R.drawable.iconcongrat2);
                }
                else if(itemstage == 3)
                {
                    fundo.setImageResource(R.drawable.iconcongrat3);
                }
                else
                {
                    fundo.setImageResource(R.drawable.iconcongrat4);
                }
                TextView t1x = (TextView) findViewById(R.id.nome3);
                t1x.setText(getString(R.string.congrat_2) + " " + itemsession + " " + getString(R.string.congrat_2b) + " " + itemstage);

                RelativeLayout tempof1 = (RelativeLayout) findViewById(R.id.tempof1);
                RelativeLayout tempof1b = (RelativeLayout) findViewById(R.id.tempof1b);
                RelativeLayout tempof1c = (RelativeLayout) findViewById(R.id.tempof1c);
                RelativeLayout tempof1d = (RelativeLayout) findViewById(R.id.tempof1d);
                TextView tempof2 = (TextView) findViewById(R.id.tempof2);
                TextView tempof2b = (TextView) findViewById(R.id.tempof2b);
                TextView tempof2c = (TextView) findViewById(R.id.tempof2c);
                TextView tempof2d = (TextView) findViewById(R.id.tempof2d);


                if(itemsession == 1)
                {
                    tempof1.setBackgroundResource(R.drawable.iconcongrat1);
                    tempof1b.setBackgroundResource(R.drawable.iconcongrat1);
                    tempof1c.setBackgroundResource(R.drawable.iconcongrat1);
                    tempof1d.setBackgroundResource(R.drawable.iconcongrat1);
                }
                else if(itemsession == 2)
                {
                    tempof1.setBackgroundResource(R.drawable.iconcongrat2);
                    tempof1b.setBackgroundResource(R.drawable.iconcongrat2);
                    tempof1c.setBackgroundResource(R.drawable.iconcongrat2);
                    tempof1d.setBackgroundResource(R.drawable.iconcongrat2);
                }
                else if(itemsession == 3)
                {
                    tempof1.setBackgroundResource(R.drawable.iconcongrat3);
                    tempof1b.setBackgroundResource(R.drawable.iconcongrat3);
                    tempof1c.setBackgroundResource(R.drawable.iconcongrat3);
                    tempof1d.setBackgroundResource(R.drawable.iconcongrat3);
                }
                else if(itemsession == 4)
                {
                    tempof1.setBackgroundResource(R.drawable.iconcongrat4);
                    tempof1b.setBackgroundResource(R.drawable.iconcongrat4);
                    tempof1c.setBackgroundResource(R.drawable.iconcongrat4);
                    tempof1d.setBackgroundResource(R.drawable.iconcongrat4);
                }
                int meunum1 = 0;
                int meunum2 = 0;
                int meunum3 = 0;
                int meunum4 = 0;
                for(int j=0; j<treinof.allexercise_routines.size(); j++)
                {
                    Exercicio exercisef = treinof.allexercise_routines.get(j);
                    Group_exercise grupo1 = exercisef.group_exercise_id__exercise_routine;
                    Muscular_group muscular1 = grupo1.muscular_group_id__group_exercise;
                    Exercise exerciciox = exercisef.exercise_id__exercise_routine;

                    int statusf = exercisef.flag_status;
                    if(statusf == 1)
                    {
                        meunum1 += 1;
                        List<Workout> workouts = exercisef.allworkouts;
                        for(int k=0; k<workouts.size(); k++)
                        {
                            Workout workoutf = workouts.get(k);
                            String reps_estimated = workoutf.reps_estimated;
                            if(!reps_estimated.equals("0") && !reps_estimated.equals("-1") )
                            {
                                meunum2 += 1;
                                String reps = reps_estimated;
                                if(reps_estimated.equals("6-8"))
                                {
                                    reps = "7";
                                }
                                else if(reps_estimated.equals("8-12"))
                                {
                                    reps = "10";
                                }
                                else if(reps_estimated.equals("12-16"))
                                {
                                    reps = "14";
                                }
                                else if(reps_estimated.equals("16-20"))
                                {
                                    reps = "18";
                                }
                                else if(reps_estimated.equals("20-25"))
                                {
                                    reps = "23";
                                }
                                meunum3 += Integer.parseInt(reps);
                                if(workoutf.weight != null && !workoutf.weight.equals(""))
                                {
                                    meunum4 += Integer.parseInt(reps) * workoutf.weight;
                                }

                            }
                        }
                    }
                }
                tempof2.setText("" + meunum1);
                tempof2b.setText("" + meunum2);
                tempof2c.setText("" + meunum3);
                tempof2d.setText("" + meunum4);
            }
        }
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
