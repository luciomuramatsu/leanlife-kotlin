package com.br.leanlife;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.br.leanlife.util.IabHelper;
import com.br.leanlife.util.IabResult;

/**
 * Created by rapha on 05/09/2017.
 */
public class Plano2Activity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    IInAppBillingService mService;
    ArrayList<String> skuList = new ArrayList<String>();
    ServiceConnection mServiceConn = null;

    private FirebaseAnalytics mFirebaseAnalytics;

    //configurar
    String plano1 = "leanlifeass1";
    String plano2 = "leanlifeass2";
    String plano3 = "leanlifeass3";
    String plano4 = "leanlife.for.life";
    String itemidlist = "ITEM_ID_LIST";
    String tokenpay = "";

    //respostas
    String plano1preco = "";
    String plano2preco = "";
    String plano3preco = "";
    String plano4preco = "";

    Integer opt1 = 0;
    Integer opt2 = 0;
    Integer opt3 = 0;
    Integer opt4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plano2);
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
                Plano2Activity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_plano2",
                screenView
        );

        TextView fab = (TextView) findViewById(R.id.foidetalhes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Plano2Activity.this, Plano3Activity.class);
                startActivity(myIntent);
            }
        });

        LinearLayout l1 = (LinearLayout) findViewById(R.id.opcao1);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.opcao2);
        LinearLayout l3 = (LinearLayout) findViewById(R.id.opcao3);
        LinearLayout l4 = (LinearLayout) findViewById(R.id.opcao4);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectplan(1);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectplan(2);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectplan(3);
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectplan(4);
            }
        });



        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
                receberdados();
                receberdadosinapp();
            }
        };

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        inicia();

    }
    public void inicia()
    {
        ImageView img1 = (ImageView) findViewById(R.id.opcao1_icon);
        ImageView img2 = (ImageView) findViewById(R.id.opcao2_icon);
        ImageView img3 = (ImageView) findViewById(R.id.opcao3_icon);
        ImageView img4 = (ImageView) findViewById(R.id.opcao4_icon);
        img1.setVisibility(View.GONE);
        img2.setVisibility(View.GONE);
        img3.setVisibility(View.GONE);
        img4.setVisibility(View.GONE);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String step2 = preferences.getString("meuplano", "");
        if(step2 != null && !step2.equals("") && !step2.equals("null"))
        {
            if(step2.equals("PD52GNNZRR2") )
            {
                img1.setVisibility(View.VISIBLE);
                opt1 = 1;
            }
            else if(step2.equals("Leanlifeass2"))
            {
                img2.setVisibility(View.VISIBLE);
                opt2 = 1;
            }
            else if(step2.equals("Leanlifeass3"))
            {
                img3.setVisibility(View.VISIBLE);
                opt3 = 1;
            }
            else if(step2.equals("leanlife.For.life"))
            {
                img4.setVisibility(View.VISIBLE);
                opt4 = 1;
            }
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
                        if(progress != null)
                        {
                            progress.dismiss();
                        }

                        //fb.mensagemerro(getString(R.string.nomeemail_pos_0), getString(R.string.nomeemail_pos_1), ct1);

                        try {
                            String resp = response.getString("data");
                            if(resp.equals("1") )
                            {
                                SharedPreferences preferences =
                                        ct1.getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("modificoutreino", "1");
                                editor.commit();

                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progress != null)
                        {
                            progress.dismiss();
                        }
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
    public void selectplan(Integer num)
    {
        String sku = "";
        try {
            Bundle buyIntentBundle = null;
            sku = skuList.get(num - 1);
            if(num != 4)
            {
                buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                        sku, "subs", tokenpay);
            }
            else
            {
                buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                        sku, "inapp", tokenpay);
            }


            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

            if(pendingIntent != null)
            {
                startIntentSenderForResult(pendingIntent.getIntentSender(),
                        1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0));
            }
            else
            {
                fb.mensagemerro(getString(R.string.step2_pos1), getString(R.string.plano2_pos1), ct1);
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku2 = jo.getString("productId");
                    String purchaseTime = jo.getString("purchaseTime");
                    String purchaseState = jo.getString("purchaseState");
                    if(purchaseState.equals("0"))
                    {
                        String expirationdata = fb.geraexpirationdata(Long.parseLong(purchaseTime),sku2);
                        String planof = fb.convertplano(sku2);

                        progress = new ProgressDialog(this);
                        progress.setTitle(getString(R.string.esqueci_pos1));
                        progress.setMessage(getString(R.string.esqueci_pos2));
                        progress.show();

                        mudaplano(expirationdata, planof);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void receberdados()
    {

        skuList.add(plano1);
        skuList.add(plano2);
        skuList.add(plano3);
        skuList.add(plano4);
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList(itemidlist, skuList);

        Bundle skuDetails = null;
        try {
            skuDetails = mService.getSkuDetails(3,
                    getPackageName(), "inapp", querySkus);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int response = skuDetails.getInt("RESPONSE_CODE");
        if (response == 0) {
            ArrayList<String> responseList
                    = skuDetails.getStringArrayList("DETAILS_LIST");

            for (String thisResponse : responseList) {

                String sku = null;
                try {
                    JSONObject object = new JSONObject(thisResponse);
                    sku = object.getString("productId");
                    String price = object.getString("price");
                    if (sku.equals(plano1)) plano1preco = price;
                    else if (sku.equals(plano2)) plano2preco = price;
                    else if (sku.equals(plano3)) plano3preco = price;
                    else if (sku.equals(plano4)) plano4preco = price;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

}

