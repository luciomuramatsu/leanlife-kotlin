package com.br.leanlife;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public TabLayout tabLayout;
    private ViewPager viewPager;
    CharSequence[] itemsphoto = null;
    public String urlffoto = "";
    static int GALLERY_PREREQUEST = 1886;
    static int CAMERA_PREREQUEST = 1887;
    static int CAMERA_REQUEST = 1888;
    static int GALLERY_REQUEST = 1889;
    static int CROP_PIC = 1890;
    private Uri picUri;

    ProgressDialog progress = null;
    String urlf = "https://leanlifeapp.com/api/public/";
    FuncoesBasicas fb = new FuncoesBasicas();

    RequestQueue requestQueue;
    String linguapassada = "";
    ServiceConnection mServiceConn = null;
    IInAppBillingService mService;

    Activity ct1 = null;
    int tabselecionada = 0;
    private static ViewPagerAdapter adapter = null;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fb.configuralinguaini(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }

        ct1 = this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenView = new Bundle();
        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                MainActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_main",
                screenView
        );

        //envia token do device pro server
        String meutoken = FirebaseInstanceId.getInstance().getToken();
        fb.enviatoken(meutoken,this);

        //pages
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        /*TextView newTab = (TextView) new TextView(this);
        newTab.setText("  Mapa");
        newTab.setGravity(Gravity.CENTER);
        newTab.setTextColor(Color.rgb(0, 117, 17));
        newTab.setCompoundDrawablesWithIntrinsicBounds(R.drawable.map, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(newTab);*/




        tabLayout.getTabAt(0).setIcon(R.drawable.dumbbel);
        //tabLayout.getTabAt(0).setText(R.string.abas_1);
        int colorx = Color.parseColor("#ffffff");
        tabLayout.getTabAt(0).getIcon().setColorFilter(colorx, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(1).setIcon(R.drawable.trophy_full_icon);
        //tabLayout.getTabAt(1).setText(R.string.abas_4);

        tabLayout.getTabAt(2).setIcon(R.drawable.chart_icon);
        //tabLayout.getTabAt(1).setText(R.string.abas_4);

        //Vagas
        tabLayout.getTabAt(3).setIcon(R.drawable.settings);
        //tabLayout.getTabAt(2).setText(R.string.abas_2);

        //Interesses
        tabLayout.getTabAt(4).setIcon(R.drawable.user_icon);
        //tabLayout.getTabAt(3).setText(R.string.abas_3);

        int colorx2 = Color.parseColor("#F0F0F0");
        tabLayout.getTabAt(1).getIcon().setColorFilter(colorx2, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(colorx2, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(colorx2, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(colorx2, PorterDuff.Mode.SRC_IN);

        ImageView img1 = (ImageView) findViewById(R.id.trofeuf);
        ImageView img2 = (ImageView) findViewById(R.id.trofeuf2);
        ImageView img3 = (ImageView) findViewById(R.id.trofeuf3);
        img2.setVisibility(View.VISIBLE);
        img1.setVisibility(View.GONE);

        img2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(ct1, TodosVideosActivity.class);
                startActivity(myIntent);

            }
        });

        img3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(ct1, ChatBotActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("numeroaviso", "0");
                myIntent.putExtras(mBundle);
                startActivity(myIntent);

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                int colorx = Color.parseColor("#ffffff");
                tab.getIcon().setColorFilter(colorx, PorterDuff.Mode.SRC_IN);
                tabselecionada = tab.getPosition();
                ImageView img1 = (ImageView) findViewById(R.id.trofeuf);
                ImageView img2 = (ImageView) findViewById(R.id.trofeuf2);
                img2.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);

                SharedPreferences preferences =
                        getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                String editado = preferences.getString("trocou", "");

                img1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Tab2Fragment t1 = (Tab2Fragment) getFragment(3);
                        t1.enviasettings();

                    }
                });


                if(tab.getPosition()==3)
                {
                    Tab2Fragment t1 = (Tab2Fragment) getFragment(3);
                    t1.inicia();


                    img1.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.GONE);
                }
                else if(tab.getPosition()==2)
                {
                    Tab5Fragment t1 = (Tab5Fragment) getFragment(2);
                    img2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.GONE);
                }
                else if(tab.getPosition()==1)
                {
                    Tab4Fragment t1 = (Tab4Fragment) getFragment(1);
                    t1.gettrofeis();
                    img2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.GONE);
                }
                else if(tab.getPosition() == 0)
                {
                    img2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.GONE);

                    preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    String modificoutreino = preferences.getString("modificoutreino", "");
                    if(modificoutreino.equals("1"))
                    {
                        Tab1Fragment t1 = (Tab1Fragment) getFragment(0);
                        if(t1.primeiravez.equals("0"))
                        {
                            t1.inicia();
                        }
                    }

                }
                else
                {
                    img2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.GONE);
                }

                if(editado.equals("1") && tab.getPosition() != 2)
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    Tab2Fragment t1 = (Tab2Fragment) getFragment(3);
                                    t1.enviasettings();

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Tab2Fragment t2 = (Tab2Fragment) getFragment(3);
                                    t2.editou("0");
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                    builder.setMessage(getString(R.string.tab2_13)).setPositiveButton("Confirm", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int colorx = Color.parseColor("#ffffff");
                tab.getIcon().setColorFilter(colorx, PorterDuff.Mode.SRC_IN);

            }
        });

        itemsphoto = new CharSequence[]{getString(R.string.nomeemail_pos_6), getString(R.string.nomeemail_pos_7),
                getString(R.string.nomeemail_pos_8)};

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

        //verifica idioma
        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String language = preferences.getString("language", "");
        if(language != null && language.equals(""))
        {
            linguapassada = "3";
        }
        else
        {
            linguapassada = language;
        }

    }
    public void iniciaconexaopag()
    {
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
                receberdadosinapp();
            }
        };

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }
    public void receberdadosinapp()
    {
        try {
            //talvez na linha de baixo tenha que trocar inapp por subs
            Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
            int response = ownedItems.getInt("RESPONSE_CODE");


            if (response == 0) {
                ArrayList<String> ownedSkus =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String>  purchaseDataList =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String>  signatureList =
                        ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String continuationToken =
                        ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = purchaseDataList.get(i);
                    String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);
                    try {
                        JSONObject jo = new JSONObject(purchaseData);
                        String sku2 = jo.getString("productId");
                        String purchaseTime = jo.getString("purchaseTime");
                        String purchaseState = jo.getString("purchaseState");
                        if(purchaseState.equals("0"))
                        {
                            String expirationdata = fb.geraexpirationdata(Long.parseLong(purchaseTime),sku2);
                            String planof = fb.convertplano(sku2);
                            mudaplano(expirationdata, planof);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // do something with this purchase information
                    // e.g. display the updated list of products owned by user
                }

                // if continuationToken != null, call getPurchases again
                // and pass in the token to retrieve more items
            }

            ownedItems = mService.getPurchases(3, getPackageName(), "subs", null);
            response = ownedItems.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> ownedSkus =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String>  purchaseDataList =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String>  signatureList =
                        ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String continuationToken =
                        ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = purchaseDataList.get(i);
                    String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);
                    try {
                        JSONObject jo = new JSONObject(purchaseData);
                        String sku2 = jo.getString("productId");
                        String purchaseTime = jo.getString("purchaseTime");
                        String purchaseState = jo.getString("purchaseState");
                        if(purchaseState.equals("0"))
                        {
                            String expirationdata = fb.geraexpirationdata(Long.parseLong(purchaseTime),sku2);
                            String planof = fb.convertplano(sku2);
                            mudaplano(expirationdata, planof);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // do something with this purchase information
                    // e.g. display the updated list of products owned by user
                }

                // if continuationToken != null, call getPurchases again
                // and pass in the token to retrieve more items
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void mudaplano(String expiracao, String planoid)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("expiracaodata", expiracao);
        params.put("plano", planoid);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"mudaplano?token="+token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resp = response.getString("data");
                            if(resp.equals("1") )
                            {
                                SharedPreferences preferences =
                                        ct1.getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("modificoutreino", "1");
                                editor.commit();
                            }
                        } catch (JSONException e) {
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
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Tab1Fragment(), "");
        adapter.addFragment(new Tab4Fragment(), "");
        adapter.addFragment(new Tab5Fragment(), "");
        adapter.addFragment(new Tab2Fragment(), "");
        adapter.addFragment(new Tab3Fragment(), "");

        viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(adapter);
    }
    public Fragment getFragment(int pos) {
        return adapter.getItem(pos);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(adapter != null && adapter.getCount() >= 5 && tabselecionada == 0)
        {
            iniciaconexaopag();

            SharedPreferences preferences =
                    getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
            String modificoutreino = preferences.getString("modificoutreino", "");
            if(modificoutreino.equals("1"))
            {
                Tab1Fragment t1 = (Tab1Fragment) getFragment(0);
                if(t1.primeiravez.equals("0"))
                {
                    t1.inicia();
                }
            }

        }
        if(adapter != null && adapter.getCount() >= 5 && tabselecionada == 1)
        {
            Tab4Fragment t1 = (Tab4Fragment) getFragment(1);
            //t1.inicia2();
        }
        if(adapter != null && adapter.getCount() >= 5 && tabselecionada == 2)
        {
            Tab5Fragment t1 = (Tab5Fragment) getFragment(2);
            //t1.inicia2();
        }
        if(adapter != null && adapter.getCount() >= 5 && tabselecionada == 3)
        {
            Tab2Fragment t1 = (Tab2Fragment) getFragment(3);
            t1.salvavariaveis();
            t1.inicia2();
        }
        if(adapter != null && adapter.getCount() >= 5 && tabselecionada == 4)
        {
            SharedPreferences preferences =
                    getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
            String language = preferences.getString("language", "");
            if(linguapassada.equals(language))
            {
                Tab3Fragment t1 = (Tab3Fragment) getFragment(4);
                t1.inicia();
            }
            else
            {
                if(language.equals("1"))
                {
                    fb.setLocale("en",ct1);
                }
                else if(language.equals("2"))
                {
                    fb.setLocale("es",ct1);
                }
                else
                {
                    fb.setLocale("pt",ct1);
                }
                Intent refresh = getIntent();
                finish();
                startActivity(refresh);
            }

        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public Fragment getFragment(int pagina) {
            Fragment resultado = null;
            if(pagina<mFragmentList.size())
            {
                resultado = mFragmentList.get(pagina);
            }
            return resultado;
        }

        Drawable myDrawable;
        String title;
        @Override
        public CharSequence getPageTitle(int position) {
            title = mFragmentTitleList.get(position);
            SpannableStringBuilder sb = new SpannableStringBuilder(title); // space added before text for convenience

            return sb;
        }
    }



    public void selectImage() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.nomeemail_pos_11));
        builder.setItems(itemsphoto, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (item==0) {
                    if (ActivityCompat.checkSelfPermission(ct1, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions((Activity) ct1,
                                new String[]{Manifest.permission.CAMERA},
                                MainActivity.CAMERA_PREREQUEST);
                    } else {
                        cameraIntent();
                    }

                } else if (item==1) {

                    if (ActivityCompat.checkSelfPermission(ct1, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions((Activity) ct1,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MainActivity.GALLERY_PREREQUEST);
                    } else {
                        galleryIntent();
                    }

                } else if (item==2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.nomeemail_pos_9)),GALLERY_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == CAMERA_PREREQUEST)
        {
            if(grantResults!=null&&grantResults.length>0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                }
            }
        }
        else if (requestCode == GALLERY_PREREQUEST) {
            if(grantResults!=null&&grantResults.length>0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == GALLERY_REQUEST) {

                onSelectFromGalleryResult(data);
            }
            else if(requestCode == CAMERA_REQUEST) {
                //picUri = data.getData();

                onCaptureImageResult(data);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap bm = null;
                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ImageView mImage = (ImageView) findViewById(R.id.foto2);
                    mImage.setImageBitmap(getCircleBitmap(bm));
                    sobeimagem(destination);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    }
    private void performCrop() {
        CropImage.activity(picUri)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowRotation(false)
                .setAllowCounterRotation(false)
                .setAutoZoomEnabled(false)
                .start(this);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // set circle bitmap
        /*ImageView mImage = (ImageView) findViewById(R.id.image);
        mImage.setImageBitmap(getCircleBitmap(bm));
        sobeimagem(destination);*/
        picUri = Uri.fromFile(destination);
        performCrop();
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*ImageView mImage = (ImageView) findViewById(R.id.image);
        mImage.setImageBitmap(getCircleBitmap(thumbnail));
        sobeimagem(destination);*/
        picUri = Uri.fromFile(destination);
        performCrop();
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

        MultipartRequest mr1 = new MultipartRequest(urlf+"salvafoto?token="+token,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //Log.d("xxx", new String(error.networkResponse.data));
                        if(ct1 != null) {
                            progress.dismiss();
                            fb.mensagemerro(getString(R.string.nomeemail_pos_2), getString(R.string.nomeemail_pos_3), ct1);
                        }
                    }
                },
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(ct1 != null) {
                            progress.dismiss();
                            try {
                                //Log.d("TESTEEE",response);
                                JSONObject obj = new JSONObject(response);
                                JSONArray jsonArray = obj.getJSONArray("data");

                                urlffoto = jsonArray.get(0).toString();
                                SharedPreferences preferences =
                                        getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("photo", urlffoto);
                                editor.commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, filef, new HashMap<String,String>() );

        requestQueue.add(mr1);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
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
