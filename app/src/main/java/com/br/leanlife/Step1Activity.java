package com.br.leanlife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Raphael on 04/05/2016.
 */
public class Step1Activity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fb.configuralinguaini(this);
        setContentView(R.layout.activity_step1);

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
                Step1Activity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_step1",
                screenView
        );

        LinearLayout fab = (LinearLayout) findViewById(R.id.botaof);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Step1Activity.this, ChatBotActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }
        });

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



        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String photo = preferences.getString("photo", "");
        String name = preferences.getString("name", "");

        String lan = Locale.getDefault().getLanguage();
        if( lan.equals("es") )
        {
            TextView t1 = (TextView) findViewById(R.id.nome2);
            t1.setText("Hola "+name+",\nVamos a empezar!");
        }
        else if(lan.equals("pt"))
        {
            TextView t1 = (TextView) findViewById(R.id.nome2);
            t1.setText("Olá "+name+",\nVamos começar?");
        }
        else
        {
            TextView t1 = (TextView) findViewById(R.id.nome2);
            t1.setText("Hi "+name+",\nLet\'s get started!");
        }
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
