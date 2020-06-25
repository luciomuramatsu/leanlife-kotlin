package com.br.leanlife;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.DatePicker;
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

/**
 * Created by Raphael on 04/05/2016.
 */
public class Step4Activity extends AppCompatActivity {
    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Context ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private FirebaseAnalytics mFirebaseAnalytics;

    CharSequence[] items3;
    CharSequence[] items1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4);
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
                Step4Activity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_step4",
                screenView
        );

        items3 = new CharSequence[]{"Kg and m", "lb and ft"};
        items1 = new CharSequence[]{getString(R.string.step4_pos2), getString(R.string.step4_pos3)};

        ImageView fab = (ImageView) findViewById(R.id.volta1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView fab2 = (ImageView) findViewById(R.id.frente1);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edt1 = (EditText) findViewById(R.id.edit1);
                EditText edt2 = (EditText) findViewById(R.id.edit2);
                EditText edt3 = (EditText) findViewById(R.id.edit3);
                EditText edt4 = (EditText) findViewById(R.id.edit4);
                EditText edt5 = (EditText) findViewById(R.id.edit5);
                if(edt1.getText().toString().equals(""))
                {
                    fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos5), ct1);
                }
                else if(edt2.getText().toString().equals(""))
                {
                    fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos6), ct1);
                }
                else if(edt3.getText().toString().equals(""))
                {
                    fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos7), ct1);
                }
                else if(edt4.getText().toString().equals(""))
                {
                    fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos8), ct1);
                }
                else if(edt5.getText().toString().equals(""))
                {
                    fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos9), ct1);
                }
                else
                {
                    String valor1 = edt1.getText().toString();
                    String valor2 = edt2.getText().toString();
                    String valor3 = edt3.getText().toString();
                    String valor4 = edt4.getText().toString();
                    String valor5 = edt5.getText().toString();

                    if(valor3.equals("Kg and m"))
                    {
                        valor3 = "1";
                    }
                    else
                    {
                        valor3 = "2";
                    }

                    if(valor1.equals("Female") || valor1.equals("Feminino") || valor1.equals("Femenino"))
                    {
                        valor1 = "2";
                    }
                    else
                    {
                        valor1 = "1";
                    }
                    SharedPreferences preferences =
                            getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("step4_1", valor1);
                    editor.putString("step4_2", valor2);
                    editor.putString("step4_3", valor3);
                    editor.putString("type_weight", valor3);
                    editor.putString("step4_4", valor4);
                    editor.putString("step4_5", valor5);
                    editor.commit();

                    Intent myIntent = new Intent(Step4Activity.this, Step5Activity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(myIntent);
                }
            }
        });
        inicia();
    }
    public void inicia()
    {
        final EditText dt1 = (EditText) findViewById(R.id.edit2);
        dt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Step4Activity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(selectedyear, selectedmonth, selectedday);
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        dt1.setText(sdf.format(date));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle(getString(R.string.step4_pos1));
                mDatePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", mDatePicker);
                mDatePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.tab1_14), mDatePicker);
                mDatePicker.show();
            }
        });

        final EditText choice1 = (EditText) findViewById(R.id.edit1);
        choice1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice1.setText(items1[which].toString());
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        final EditText choice3 = (EditText) findViewById(R.id.edit3);
        choice3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice3.setText(items3[which].toString());
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String step4_1 = preferences.getString("step4_1", "");
        String step4_2 = preferences.getString("step4_2", "");
        String step4_3 = preferences.getString("step4_3", "");
        String step4_4 = preferences.getString("step4_4", "");
        String step4_5 = preferences.getString("step4_5", "");

        EditText edt1 = (EditText) findViewById(R.id.edit1);
        EditText edt2 = (EditText) findViewById(R.id.edit2);
        EditText edt3 = (EditText) findViewById(R.id.edit3);
        EditText edt4 = (EditText) findViewById(R.id.edit4);
        EditText edt5 = (EditText) findViewById(R.id.edit5);
        if(!step4_1.equals("") && !step4_1.equals("null"))
        {
            if(step4_1.equals("1"))
            {
                edt3.setText("Kg and m");
            }
            else
            {
                edt3.setText("lb and ft");
            }
        }
        if(!step4_2.equals("") && !step4_2.equals("null"))
        {
            edt2.setText(step4_2);
        }
        if(!step4_3.equals("") && !step4_3.equals("null"))
        {
            if(step4_3.equals("1"))
            {
                edt1.setText(getString(R.string.step4_pos2));
            }
            else
            {
                edt1.setText(getString(R.string.step4_pos3));
            }
        }
        if(!step4_4.equals("") && !step4_4.equals("null"))
        {
            edt4.setText(step4_4);
        }
        if(!step4_5.equals("") && !step4_5.equals("null"))
        {
            edt5.setText(step4_5);
        }

    }
}
