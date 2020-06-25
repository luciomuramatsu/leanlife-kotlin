package com.br.leanlife;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.br.leanlife.adapters.ChatAdapter;
import com.br.leanlife.adapters.TreinoAdapter;
import com.br.leanlife.models.Chat;
import com.br.leanlife.models.Exercicio;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rapha on 05/09/2017.
 */
public class ChatBotActivity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Activity ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private List<Chat> msglista4 = new ArrayList<>();
    private RecyclerView recyclerView4;
    private ChatAdapter mAdapter4;

    String resp1 = "";
    String resp2 = "";

    CharSequence items[];
    CharSequence items2[];
    CharSequence items3[];
    CharSequence items4[];
    CharSequence items5[];
    CharSequence items6[];
    CharSequence items7[];

    CharSequence itemsnew[];

    Map<String,Integer> itemsmap = new HashMap<String,Integer>();
    Map<String,Integer> itemsmap2 = new HashMap<String,Integer>();
    Map<String,Integer> itemsmap3 = new HashMap<String,Integer>();
    Map<String,Integer> itemsmap4 = new HashMap<String,Integer>();
    Map<String,Integer> itemsmap5 = new HashMap<String,Integer>();
    Map<String,Integer> itemsmap6 = new HashMap<String,Integer>();
    Map<String,Integer> itemsmap7 = new HashMap<String,Integer>();

    Map<String,Integer> itemsmapnew = new HashMap<String,Integer>();

    int tipoagora = 0;

    Integer opt1a = 0;
    Integer opt2a = 0;
    Integer opt3a = 0;
    Integer opt4a = 0;
    Integer opt5a = 0;

    Integer opt1b = 0;
    Integer opt2b = 0;
    Integer opt3b = 0;
    Integer opt4b = 0;
    Integer opt5b = 0;
    Integer opt6b = 0;
    Integer opt7b = 0;
    Integer opt8b = 0;

    String tipoaviso = "-1";
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finalizar tela ao clicar no voltar
                if(tipoaviso.equals("-1"))
                {
                    perguntavoltar();
                }
                else
                {
                    finish();
                }



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

        Bundle b = this.getIntent().getExtras();
        if(b != null)
        {
            if(b.containsKey("numeroaviso"))
            {
                tipoaviso = b.getString("numeroaviso");
            }
        }

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenView = new Bundle();
        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                ChatBotActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_chatbot",
                screenView
        );

        iniciapickers();

        ImageButton img1x = (ImageButton) findViewById(R.id.sendMessageButton);
        img1x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escrevecoment();
            }
        });


        EditText dt1 = (EditText) findViewById(R.id.messageEditText);
        dt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aparecetecladoespecial();
            }
        });

        recyclerView4 = (RecyclerView) findViewById(R.id.lista1);

        recyclerView4.removeAllViews();
        msglista4.clear();

        mAdapter4 = new ChatAdapter(msglista4,ct1);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ct1);
        //mLayoutManager.setStackFromEnd(true);
        //mLayoutManager.setReverseLayout(true);
        recyclerView4.setLayoutManager(mLayoutManager);
        recyclerView4.setItemAnimator(new DefaultItemAnimator());
        recyclerView4.setAdapter(mAdapter4);




        if(tipoaviso.equals("-1"))
        {


            Chat chat0 = new Chat(0,-1,"");
            msglista4.add(chat0);

            mAdapter4.notifyDataSetChanged();
            escreverobo(1);
        }
        else
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            Chat chat0 = new Chat(0,-1,"x");
            msglista4.add(chat0);

            mAdapter4.notifyDataSetChanged();
            escreveespecial(1,"0");
        }

    }
    @Override
    public void onBackPressed() {
        perguntavoltar();
    }
    public void perguntavoltar()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        finish();

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked

                }
            }
        };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ct1);
        builder.setMessage(getString(R.string.exercicio_pos2b)).setPositiveButton(getString(R.string.exercicio_pos3), dialogClickListener)
                .setNegativeButton(getString(R.string.exercicio_pos4), dialogClickListener).show();
    }
    public void iniciapickers()
    {
        //1
        itemsmap.put(getString(R.string.step2_4),1);
        itemsmap.put(getString(R.string.step2_5),2);
        itemsmap.put(getString(R.string.step2_6),3);
        itemsmap.put(getString(R.string.step2_7),4);
        itemsmap.put(getString(R.string.step2_8),5);
        List<String> listItemsest = new ArrayList<String>();
        listItemsest.add(getString(R.string.step2_4));
        listItemsest.add(getString(R.string.step2_5));
        listItemsest.add(getString(R.string.step2_6));
        listItemsest.add(getString(R.string.step2_7));
        listItemsest.add(getString(R.string.step2_8));
        items = listItemsest.toArray(new CharSequence[listItemsest.size()]);


        //2
        itemsmap2.put(getString(R.string.chatbot_pre1),1);
        itemsmap2.put(getString(R.string.chatbot_pre2),2);
        itemsmap2.put(getString(R.string.chatbot_pre3),3);
        itemsmap2.put(getString(R.string.chatbot_pre4),4);
        listItemsest = new ArrayList<String>();
        listItemsest.add(getString(R.string.chatbot_pre1));
        listItemsest.add(getString(R.string.chatbot_pre2));
        listItemsest.add(getString(R.string.chatbot_pre3));
        listItemsest.add(getString(R.string.chatbot_pre4));
        items2 = listItemsest.toArray(new CharSequence[listItemsest.size()]);

        //3
        itemsmap3.put("kg and m",1);
        itemsmap3.put("lb and ft",2);
        listItemsest = new ArrayList<String>();
        listItemsest.add("kg and m");
        listItemsest.add("lb and ft");
        items3 = listItemsest.toArray(new CharSequence[listItemsest.size()]);

        //4
        itemsmap4.put(getString(R.string.step4_pos2),1);
        itemsmap4.put(getString(R.string.step4_pos3),2);
        listItemsest = new ArrayList<String>();
        listItemsest.add(getString(R.string.step4_pos2));
        listItemsest.add(getString(R.string.step4_pos3));
        items4 = listItemsest.toArray(new CharSequence[listItemsest.size()]);

        //5
        itemsmap5.put("30 min",30);
        itemsmap5.put("45 min",45);
        itemsmap5.put("60 min",60);
        itemsmap5.put("90 min",90);
        listItemsest = new ArrayList<String>();
        listItemsest.add("30 min");
        listItemsest.add("45 min");
        listItemsest.add("60 min");
        listItemsest.add("90 min");
        items5 = listItemsest.toArray(new CharSequence[listItemsest.size()]);

        //6
        itemsmap6.put(getString(R.string.area_1),1);
        itemsmap6.put(getString(R.string.area_2),2);
        itemsmap6.put(getString(R.string.area_3),3);
        itemsmap6.put(getString(R.string.area_4),4);
        itemsmap6.put(getString(R.string.area_5),5);
        itemsmap6.put(getString(R.string.area_6),6);
        itemsmap6.put(getString(R.string.area_7),7);
        itemsmap6.put(getString(R.string.area_8),8);
        listItemsest = new ArrayList<String>();
        listItemsest.add(getString(R.string.area_1));
        listItemsest.add(getString(R.string.area_2));
        listItemsest.add(getString(R.string.area_3));
        listItemsest.add(getString(R.string.area_4));
        listItemsest.add(getString(R.string.area_5));
        listItemsest.add(getString(R.string.area_6));
        listItemsest.add(getString(R.string.area_7));
        listItemsest.add(getString(R.string.area_8));
        items6 = listItemsest.toArray(new CharSequence[listItemsest.size()]);

        //7
        itemsmap7.put(getString(R.string.chatbot_localtreino1),1);
        itemsmap7.put(getString(R.string.chatbot_localtreino2),2);
        itemsmap7.put(getString(R.string.chatbot_localtreino3),3);
        itemsmap7.put(getString(R.string.chatbot_localtreino4),4);
        itemsmap7.put(getString(R.string.chatbot_localtreino5),5);
        listItemsest = new ArrayList<String>();
        listItemsest.add(getString(R.string.chatbot_localtreino1));
        listItemsest.add(getString(R.string.chatbot_localtreino2));
        listItemsest.add(getString(R.string.chatbot_localtreino3));
        listItemsest.add(getString(R.string.chatbot_localtreino4));
        listItemsest.add(getString(R.string.chatbot_localtreino5));
        items7 = listItemsest.toArray(new CharSequence[listItemsest.size()]);

        //new
        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String resp1en = preferences.getString("resp1en", "");
        String resp2en = preferences.getString("resp2en", "");
        String resp3en = preferences.getString("resp3en", "");

        String resp1es = preferences.getString("resp1es", "");
        String resp2es = preferences.getString("resp2es", "");
        String resp3es = preferences.getString("resp3es", "");

        String resp1pt = preferences.getString("resp1pt", "");
        String resp2pt = preferences.getString("resp2pt", "");
        String resp3pt = preferences.getString("resp3pt", "");


        String language = preferences.getString("language", "");
        listItemsest = new ArrayList<String>();
        if(language.equals("1"))
        {

            itemsmapnew.put(resp1en,1);
            itemsmapnew.put(resp2en,2);
            itemsmapnew.put(resp3en,3);
            itemsmapnew.put(getString(R.string.chatbot_new3),4);

            listItemsest.add(resp1en);
            listItemsest.add(resp2en);
            listItemsest.add(resp3en);
            listItemsest.add(getString(R.string.chatbot_new3));
        }
        else if(language.equals("2"))
        {

            itemsmapnew.put(resp1es,1);
            itemsmapnew.put(resp2es,2);
            itemsmapnew.put(resp3es,3);
            itemsmapnew.put(getString(R.string.chatbot_new3),4);

            listItemsest.add(resp1es);
            listItemsest.add(resp2es);
            listItemsest.add(resp3es);
            listItemsest.add(getString(R.string.chatbot_new3));
        }
        else
        {
            itemsmapnew.put(resp1pt,1);
            itemsmapnew.put(resp2pt,2);
            itemsmapnew.put(resp3pt,3);
            itemsmapnew.put(getString(R.string.chatbot_new3),4);

            listItemsest.add(resp1pt);
            listItemsest.add(resp2pt);
            listItemsest.add(resp3pt);
            listItemsest.add(getString(R.string.chatbot_new3));
        }


        itemsnew = listItemsest.toArray(new CharSequence[listItemsest.size()]);
    }
    public void escrevecoment()
    {
        EditText edt1 = (EditText) findViewById(R.id.messageEditText);
        String strf = edt1.getText().toString();
        if(!strf.equals("") && !strf.equals("null"))
        {
            Chat chat1 = new Chat(tipoagora,1,strf);
            msglista4.add(chat1);
            mAdapter4.notifyDataSetChanged();
            edt1.setText("");

            recyclerView4.scrollToPosition(msglista4.size() - 1);

            String resp1f = "";
            if(tipoaviso.equals("-1"))
            {
                //salva variaveis
                if (tipoagora == 1) {
                    resp1f = "" + resp1;
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step2", resp1f);
                    editor.commit();
                } else if (tipoagora == 2) {
                    resp1f = "" + itemsmap2.get(strf);
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step3", resp1f);
                    editor.commit();
                } else if (tipoagora == 3) {
                    resp1f = "" + itemsmap3.get(strf);
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step4_3", resp1f);
                    editor.commit();
                } else if (tipoagora == 4) {
                    resp1f = "" + strf;
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step4_4", resp1f);
                    editor.commit();
                } else if (tipoagora == 5) {
                    resp1f = "" + strf;
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step4_5", resp1f);
                    editor.commit();
                } else if (tipoagora == 6) {
                    resp1f = "" + itemsmap4.get(strf);
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step4_1", resp1f);
                    editor.commit();
                } else if (tipoagora == 7) {
                    resp1f = "" + strf;
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step4_2", resp1f);
                    editor.commit();
                } else if (tipoagora == 8) {
                    resp1f = "" + itemsmap5.get(strf);
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step5_1", resp1f);
                    editor.commit();
                } else if (tipoagora == 9) {
                    resp1f = "" + resp2;
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step5_2", resp1f);
                    editor.putString("step5_2b", strf);
                    editor.commit();
                } else if (tipoagora == 10) {
                    resp1f = "" + itemsmap7.get(strf);
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("local_treino", resp1f);
                    editor.commit();
                }

                if (tipoagora <= 11) {
                    escreverobo(tipoagora + 1);
                } else {

                }
            }
            else
            {
                if (tipoagora % 2 == 1) {

                    resp1f = "" + itemsmapnew.get(strf);
                    if(resp1f.equals("4"))
                    {
                        escreveespecial(tipoagora + 1,resp1f);
                    }
                    else
                    {
                        SharedPreferences preferences =
                                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                        String message1 = preferences.getString("messageentooltip", "");
                        String message2 = preferences.getString("messagepttooltip", "");
                        String message3 = preferences.getString("messageestooltip", "");
                        enviaemail(message2, strf);

                        escreveespecial(tipoagora + 2,resp1f);
                    }
                } else {
                    enviaemail("Outro", strf);
                    escreveespecial(tipoagora + 1,"5");

                }

            }

        }
    }
    public void enviatudoant()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enviatudo();
            }
        }, 3000);
    }
    public void aparecetecladoespecial()
    {
        if(tipoaviso.equals("-1")) {
            if (tipoagora == 1) {
                opt1a = 0;
                opt2a = 0;
                opt3a = 0;
                opt4a = 0;
                opt5a = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setTitle(getString(R.string.step2_pos2))
                        .setMultiChoiceItems(items, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                        if (isChecked) {
                                            if (item == 0) opt1a = 1;
                                            if (item == 1) opt2a = 1;
                                            if (item == 2) opt3a = 1;
                                            if (item == 3) opt4a = 1;
                                            if (item == 4) opt5a = 1;
                                        } else {
                                            if (item == 0) opt1a = 0;
                                            if (item == 1) opt2a = 0;
                                            if (item == 2) opt3a = 0;
                                            if (item == 3) opt4a = 0;
                                            if (item == 4) opt5a = 0;
                                        }
                                    }
                                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int total = qtdselecionados();
                                if (total == 0) {
                                    Toast.makeText(ct1, getString(R.string.step2_pos4), Toast.LENGTH_SHORT).show();
                                } else if (total > 2) {
                                    Toast.makeText(ct1, getString(R.string.step2_pos2), Toast.LENGTH_SHORT).show();
                                } else {
                                    EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                                    String strfinal = "";
                                    if (opt1a == 1) {
                                        strfinal = "1";
                                    }
                                    if (opt2a == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "2";
                                        } else {
                                            strfinal += ",2";
                                        }
                                    }
                                    if (opt3a == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "3";
                                        } else {
                                            strfinal += ",3";
                                        }
                                    }
                                    if (opt4a == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "4";
                                        } else {
                                            strfinal += ",4";
                                        }
                                    }
                                    if (opt5a == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "5";
                                        } else {
                                            strfinal += ",5";
                                        }
                                    }
                                    resp1 = strfinal;
                                    String resp1a = transformaobjetivos(resp1);
                                    dt1.setText(resp1a);
                                    escrevecoment();
                                }

                            }
                        });

                builder.show();
            } else if (tipoagora == 2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                        dt1.setText(items2[which].toString());
                        dialog.dismiss();
                        escrevecoment();
                    }
                });
                builder.show();
            } else if (tipoagora == 3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                        dt1.setText(items3[which].toString());
                        dialog.dismiss();
                        escrevecoment();
                    }
                });
                builder.show();
            } else if (tipoagora == 6) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items4, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                        dt1.setText(items4[which].toString());
                        dialog.dismiss();
                        escrevecoment();
                    }
                });
                builder.show();
            } else if (tipoagora == 7) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ChatBotActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(selectedyear, selectedmonth, selectedday);
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        dt1.setText(sdf.format(date));

                        escrevecoment();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDatePicker.setTitle(getString(R.string.step4_pos1));
                mDatePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", mDatePicker);
                mDatePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.tab1_14), mDatePicker);
                mDatePicker.show();
            } else if (tipoagora == 8) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items5, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                        dt1.setText(items5[which].toString());
                        dialog.dismiss();
                        escrevecoment();
                    }
                });
                builder.show();
            } else if (tipoagora == 9) {
                opt1b = 0;
                opt2b = 0;
                opt3b = 0;
                opt4b = 0;
                opt5b = 0;
                opt6b = 0;
                opt7b = 0;
                opt8b = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setTitle(getString(R.string.step4b_3))
                        .setMultiChoiceItems(items6, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                        if (isChecked) {
                                            if (item == 0) opt1b = 1;
                                            if (item == 1) opt2b = 1;
                                            if (item == 2) opt3b = 1;
                                            if (item == 3) opt4b = 1;
                                            if (item == 4) opt5b = 1;
                                            if (item == 5) opt6b = 1;
                                            if (item == 6) opt7b = 1;
                                            if (item == 7) opt8b = 1;
                                        } else {
                                            if (item == 0) opt1b = 0;
                                            if (item == 1) opt2b = 0;
                                            if (item == 2) opt3b = 0;
                                            if (item == 3) opt4b = 0;
                                            if (item == 4) opt5b = 0;
                                            if (item == 5) opt6b = 0;
                                            if (item == 6) opt7b = 0;
                                            if (item == 7) opt8b = 0;
                                        }
                                    }
                                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int total = qtdselecionados2();
                                if (total != 3) {
                                    Toast.makeText(ct1, getString(R.string.step4b_6), Toast.LENGTH_SHORT).show();
                                } else {
                                    EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                                    String strfinal = "";
                                    String strfinal2 = "";
                                    if (opt1b == 1) {
                                        strfinal = "1";
                                        strfinal2 = getString(R.string.area_1);
                                    }
                                    if (opt2b == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "2";
                                            strfinal2 = getString(R.string.area_2);
                                        } else {
                                            strfinal += ",2";
                                            strfinal2 += ", " + getString(R.string.area_2);
                                        }
                                    }
                                    if (opt3b == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "3";
                                            strfinal2 = getString(R.string.area_3);
                                        } else {
                                            strfinal += ",3";
                                            strfinal2 += ", " + getString(R.string.area_3);
                                        }
                                    }
                                    if (opt4b == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "4";
                                            strfinal2 = getString(R.string.area_4);
                                        } else {
                                            strfinal += ",4";
                                            strfinal2 += ", " + getString(R.string.area_4);
                                        }
                                    }
                                    if (opt5b == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "5";
                                            strfinal2 = getString(R.string.area_5);
                                        } else {
                                            strfinal += ",5";
                                            strfinal2 += ", " + getString(R.string.area_5);
                                        }
                                    }
                                    if (opt6b == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "6";
                                            strfinal2 = getString(R.string.area_6);
                                        } else {
                                            strfinal += ",6";
                                            strfinal2 += ", " + getString(R.string.area_6);
                                        }
                                    }
                                    if (opt7b == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "7";
                                            strfinal2 = getString(R.string.area_7);
                                        } else {
                                            strfinal += ",7";
                                            strfinal2 += ", " + getString(R.string.area_7);
                                        }
                                    }
                                    if (opt8b == 1) {
                                        if (strfinal.equals("")) {
                                            strfinal = "8";
                                            strfinal2 = getString(R.string.area_8);
                                        } else {
                                            strfinal += ",8";
                                            strfinal2 += ", " + getString(R.string.area_8);
                                        }
                                    }
                                    resp2 = strfinal;
                                    String resp2b = strfinal2;
                                    dt1.setText(resp2b);
                                    escrevecoment();
                                }

                            }
                        });

                builder.show();
            }
            else if (tipoagora == 10) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items7, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                        dt1.setText(items7[which].toString());
                        dialog.dismiss();
                        escrevecoment();
                    }
                });
                builder.show();
            }
        }
        else
        {
            if (tipoagora % 2 == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(itemsnew, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dt1 = (EditText) findViewById(R.id.messageEditText);
                        dt1.setText(itemsnew[which].toString());
                        dialog.dismiss();
                        escrevecoment();
                    }
                });
                builder.show();
            }
        }
        recyclerView4.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView4.scrollToPosition(msglista4.size() - 1);
            }
        }, 400);

    }

    public int qtdselecionados()
    {
        int numf = 0;
        if(opt1a == 1)       numf += 1;
        if(opt2a == 1)       numf += 1;
        if(opt3a == 1)       numf += 1;
        if(opt4a == 1)       numf += 1;
        if(opt5a == 1)       numf += 1;

        return numf;
    }
    public int qtdselecionados2()
    {
        int numf = 0;
        if(opt1b == 1)       numf += 1;
        if(opt2b == 1)       numf += 1;
        if(opt3b == 1)       numf += 1;
        if(opt4b == 1)       numf += 1;
        if(opt5b == 1)       numf += 1;
        if(opt6b == 1)       numf += 1;
        if(opt7b == 1)       numf += 1;
        if(opt8b == 1)       numf += 1;

        return numf;
    }
    public void bloqueiateclado()
    {
        EditText edt1 = (EditText) findViewById(R.id.messageEditText);
        edt1.setInputType(InputType.TYPE_CLASS_TEXT);
        edt1.setFocusableInTouchMode(false);
        edt1.clearFocus();
        if(getCurrentFocus()!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    public void mudateclado()
    {
        //fecha o teclado
        if(tipoaviso.equals("-1")) {
            bloqueiateclado();
            EditText edt1 = (EditText) findViewById(R.id.messageEditText);
            if (tipoagora == 1 || tipoagora == 9) {
                //multipla seleção
                edt1.setFocusableInTouchMode(false);
            } else if (tipoagora == 2 || tipoagora == 3 || tipoagora == 6 || tipoagora == 8 || tipoagora == 10) {
                //seleção unica
                edt1.setFocusableInTouchMode(false);
            } else if (tipoagora == 4 || tipoagora == 5) {
                //teclado númerico
                edt1.setFocusableInTouchMode(true);
                edt1.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (tipoagora == 7) {
                //calendário
                edt1.setFocusableInTouchMode(false);
            }
        }
        else
        {
            if (tipoagora % 2 == 1)
            {
                bloqueiateclado();
                EditText edt1 = (EditText) findViewById(R.id.messageEditText);
                edt1.setFocusableInTouchMode(false);
            }
            else
            {
                EditText edt1 = (EditText) findViewById(R.id.messageEditText);
                edt1.setFocusableInTouchMode(true);
                edt1.setInputType(InputType.TYPE_CLASS_TEXT);
            }

        }
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
        String local_treino = preferences.getString("local_treino", "");


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
        params.put("workout_place", local_treino);

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

                        Intent myIntent = new Intent(ChatBotActivity.this, MainActivity.class);
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    public void escreveespecial(int tipof, final String resp1)
    {
        tipoagora = tipof;
        bloqueiateclado();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Chat chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_new1));
                if(tipoagora % 2 == 1)
                {
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    String message1 = preferences.getString("messageentooltip", "");
                    String message2 = preferences.getString("messagepttooltip", "");
                    String message3 = preferences.getString("messageestooltip", "");


                    String respopt1en = preferences.getString("respopt1en", "");
                    String respopt1pt = preferences.getString("respopt1pt", "");
                    String respopt1es = preferences.getString("respopt1es", "");

                    String respopt2en = preferences.getString("respopt2en", "");
                    String respopt2pt = preferences.getString("respopt2pt", "");
                    String respopt2es = preferences.getString("respopt2es", "");

                    String respopt3en = preferences.getString("respopt3en", "");
                    String respopt3pt = preferences.getString("respopt3pt", "");
                    String respopt3es = preferences.getString("respopt3es", "");

                    String language = preferences.getString("language", "");
                    if(language.equals("1"))
                    {
                        String messagef = "";
                        if(resp1.equals("0"))
                        {
                            messagef = message1;
                        }
                        else if(resp1.equals("1"))
                        {
                            messagef = respopt1en;
                        }
                        else if(resp1.equals("2"))
                        {
                            messagef = respopt2en;
                        }
                        else if(resp1.equals("3"))
                        {
                            messagef = respopt3en;
                        }
                        else if(resp1.equals("4"))
                        {
                            messagef = getString(R.string.chatbot_new2);
                        }
                        else if(resp1.equals("5"))
                        {
                            messagef = getString(R.string.chatbot_new2);
                        }

                        chat1 = new Chat(tipoagora,0,messagef);
                    }
                    else if(language.equals("2"))
                    {
                        String messagef = "";
                        if(resp1.equals("0"))
                        {
                            messagef = message3;
                        }
                        else if(resp1.equals("1"))
                        {
                            messagef = respopt1es;
                        }
                        else if(resp1.equals("2"))
                        {
                            messagef = respopt2es;
                        }
                        else if(resp1.equals("3"))
                        {
                            messagef = respopt3es;
                        }
                        else if(resp1.equals("4"))
                        {
                            messagef = getString(R.string.chatbot_new2);
                        }
                        else if(resp1.equals("5"))
                        {
                            messagef = getString(R.string.chatbot_new2);
                        }
                        chat1 = new Chat(tipoagora,0,messagef);
                    }
                    else
                    {
                        String messagef = "";
                        if(resp1.equals("0"))
                        {
                            messagef = message2;
                        }
                        else if(resp1.equals("1"))
                        {
                            messagef = respopt1pt;
                        }
                        else if(resp1.equals("2"))
                        {
                            messagef = respopt2pt;
                        }
                        else if(resp1.equals("3"))
                        {
                            messagef = respopt3pt;
                        }
                        else if(resp1.equals("4"))
                        {
                            messagef = getString(R.string.chatbot_new2);
                        }
                        else if(resp1.equals("5"))
                        {
                            messagef = getString(R.string.chatbot_new2);
                        }
                        chat1 = new Chat(tipoagora,0,messagef);
                    }

                }
                else
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_new1));
                }

                msglista4.add(chat1);
                mAdapter4.notifyDataSetChanged();

                recyclerView4.scrollToPosition(msglista4.size() - 1);

                mudateclado();
            }
        }, 1000);
    }
    public void escreverobo(int tipof)
    {
        tipoagora = tipof;
        bloqueiateclado();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Chat chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3a));
                if(tipoagora == 1)
                {
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    String token = preferences.getString("token", "");
                    String name = preferences.getString("name", "");
                    String email = preferences.getString("email", "");
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_pre0) + " " + name + ",\n" + getString(R.string.chatbot_pre) + "\n" + getString(R.string.chatbot_3a));
                }
                else if(tipoagora == 2)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3b));
                }
                else if(tipoagora == 3)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3c));
                }
                else if(tipoagora == 4)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3d));
                }
                else if(tipoagora == 5)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3e));
                }
                else if(tipoagora == 6)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3f));
                }
                else if(tipoagora == 7)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3g));
                }
                else if(tipoagora == 8)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3h));
                }
                else if(tipoagora == 9)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3i));
                }
                else if(tipoagora == 10)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_localtreino));
                }
                else if(tipoagora == 11)
                {
                    chat1 = new Chat(tipoagora,0,getString(R.string.chatbot_3j));
                    enviatudoant();
                }
                msglista4.add(chat1);
                mAdapter4.notifyDataSetChanged();

                recyclerView4.scrollToPosition(msglista4.size() - 1);

                mudateclado();
            }
        }, 1000);
    }

    public String transformaobjetivos(String str1)
    {
        String auxf = "";
        String[] separated = str1.split(",");
        for(int i=0;i<separated.length;i++)
        {
            String aux1 = "";
            if(separated[i].equals("1"))
            {
                aux1 = getString(R.string.step2_4);
            }
            if(separated[i].equals("2"))
            {
                aux1 = getString(R.string.step2_5);
            }
            if(separated[i].equals("3"))
            {
                aux1 = getString(R.string.step2_6);
            }
            if(separated[i].equals("4"))
            {
                aux1 = getString(R.string.step2_7);
            }
            if(separated[i].equals("5"))
            {
                aux1 = getString(R.string.step2_8);
            }

            if(auxf.equals(""))
            {
                auxf = aux1;
            }
            else
            {
                auxf += ", " + aux1;
            }
        }

        return auxf;
    }
    public void enviaemail(String pergunta, String resposta)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pergunta", pergunta);
        params.put("resposta", resposta);

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"enviaemailchatbot?token="+token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //sucesso

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //erro
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

