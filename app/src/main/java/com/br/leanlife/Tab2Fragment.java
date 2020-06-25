package com.br.leanlife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
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


public class Tab2Fragment extends Fragment{

    int value;
    Tab2Fragment minhatab;

    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Activity ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    CharSequence[] items3;
    CharSequence[] items1;
    String step5_2 = "";
    String step5_2b = "";
    String step5_3 = "";
    String step5_3b = "";
    CharSequence[] items1b;
    public Tab2Fragment() {
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
        return inflater.inflate(R.layout.tab2, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        items3 = new CharSequence[]{"Kg and m", "lb and ft"};
        items1 = new CharSequence[]{getString(R.string.step4_pos2), getString(R.string.step4_pos3)};
        items1b = new CharSequence[]{"30","45","60","90"};
        ct1 = getActivity();

        LinearLayout fab3 = (LinearLayout) view.findViewById(R.id.foiemphasize);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), AreaEmphasizeActivity2.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("tipo", "0");
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
                editou("1");
            }
        });
        LinearLayout fab3b = (LinearLayout) view.findViewById(R.id.foiemphasize2);
        fab3b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), AreaEmphasizeActivity2.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("tipo", "1");
                myIntent.putExtras(mBundle);
                startActivity(myIntent);
                editou("1");
            }
        });
        LinearLayout fab3c = (LinearLayout) view.findViewById(R.id.foiobjetivoprinc);
        fab3c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), ObjetivoPrincipal.class);
                startActivity(myIntent);
                editou("1");
            }
        });
        LinearLayout fab3d = (LinearLayout) view.findViewById(R.id.foinivelatividades);
        fab3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), NivelAtividades.class);
                startActivity(myIntent);
                editou("1");
            }
        });

        final EditText dt1 = (EditText) view.findViewById(R.id.edit2);
        dt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(selectedyear, selectedmonth, selectedday);
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        dt1.setText(sdf.format(date));
                        editou("1");
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle(getString(R.string.step4_pos1));
                mDatePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", mDatePicker);
                mDatePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.tab1_14), mDatePicker);
                mDatePicker.show();
            }
        });

        final EditText choice1 = (EditText) view.findViewById(R.id.edit1);
        choice1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice1.setText(items1[which].toString());
                        dialog.dismiss();
                        editou("1");
                    }
                });
                builder.show();
            }
        });

        final EditText choice3 = (EditText) view.findViewById(R.id.edit3);
        choice3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice3.setText(items3[which].toString());
                        dialog.dismiss();
                        editou("1");
                    }
                });
                builder.show();
            }
        });
        final EditText choice1c = (EditText) view.findViewById(R.id.edit4);
        choice1c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editou("1");
            }
        });
        final EditText choice1d = (EditText) view.findViewById(R.id.edit5);
        choice1d.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editou("1");
            }
        });

        final EditText choice4 = (EditText) view.findViewById(R.id.edit6);
        choice4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ct1);
                builder.setItems(items1b, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice4.setText(items1b[which].toString());
                        dialog.dismiss();
                        editou("1");
                    }
                });
                builder.show();
            }
        });

        //inicia();

    }
    public void editou(String valor)
    {
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("trocou", valor);
        editor.commit();
    }
    public void inicia()
    {
        progress = new ProgressDialog(getActivity());
        progress.setTitle(getString(R.string.nomeemail_pos_4));
        progress.setMessage(getString(R.string.nomeemail_pos_5));
        progress.show();


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, urlf+"profile?token="+token ,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            if(getView() != null) {
                                JSONObject item = response.getJSONObject("data");
                                JSONObject profile = item.getJSONObject("profile");
                                JSONArray emphasize = item.getJSONArray("emphasize");
                                JSONArray exclude = item.getJSONArray("exclude");

                                String strf1 = "";
                                String strf2 = "";
                                for (int i = 0; i < emphasize.length(); i++) {
                                    JSONObject empaux = emphasize.getJSONObject(i);
                                    if (strf1.equals("")) {
                                        strf1 = strf1 + empaux.getString("muscular_group_id");
                                    } else {
                                        strf1 = strf1 + "," + empaux.getString("muscular_group_id");
                                    }
                                }
                                for (int i = 0; i < exclude.length(); i++) {
                                    JSONObject empaux = exclude.getJSONObject(i);
                                    if (strf2.equals("")) {
                                        strf2 = strf2 + empaux.getString("muscular_group_id");
                                    } else {
                                        strf2 = strf2 + "," + empaux.getString("muscular_group_id");
                                    }
                                }

                                SharedPreferences preferences =
                                        getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("step2", profile.getString("id_main_goal"));
                                editor.putString("step3", profile.getString("id_activity_level"));
                                editor.putString("step4_1", item.getString("gender"));
                                editor.putString("step4_2", item.getString("birthday"));
                                editor.putString("step4_3", item.getString("type_weight"));
                                editor.putString("step4_4", item.getString("height"));
                                editor.putString("step4_5", item.getString("weight"));
                                editor.putString("step5_1", profile.getString("minutes_per_workout"));
                                editor.putString("step5_2", strf1);
                                editor.putString("step5_3", strf2);
                                editor.commit();

                                progress.dismiss();
                                editou("0");
                                inicia2();
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            progress.dismiss();
                            inicia2();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        inicia2();
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

        View view = getView();
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String step4_1 = preferences.getString("step4_1", "");
        String step4_2 = preferences.getString("step4_2", "");
        String step4_3 = preferences.getString("step4_3", "");
        String step4_4 = preferences.getString("step4_4", "");
        String step4_5 = preferences.getString("step4_5", "");
        String step5_1 = preferences.getString("step5_1", "");
        String step2 = preferences.getString("step2", "");
        String step3 = preferences.getString("step3", "");
        step5_2 = preferences.getString("step5_2", "");
        step5_2b = preferences.getString("step5_2b", "");
        step5_3 = preferences.getString("step5_3", "");
        step5_3b = preferences.getString("step5_3b", "");

        EditText edt1 = (EditText) view.findViewById(R.id.edit1);
        EditText edt2 = (EditText) view.findViewById(R.id.edit2);
        EditText edt3 = (EditText) view.findViewById(R.id.edit3);
        EditText edt4 = (EditText) view.findViewById(R.id.edit4);
        EditText edt5 = (EditText) view.findViewById(R.id.edit5);
        if(!step4_1.equals("") && !step4_1.equals("null"))
        {
            if(step4_1.equals("1"))
            {
                edt1.setText(getString(R.string.step4_pos2));
            }
            else
            {
                edt1.setText(getString(R.string.step4_pos3));
            }
        }
        if(!step4_3.equals("") && !step4_3.equals("null"))
        {
            if(step4_3.equals("1"))
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
        if(!step4_4.equals("") && !step4_4.equals("null"))
        {
            edt4.setText(step4_4);
        }
        if(!step4_5.equals("") && !step4_5.equals("null"))
        {
            edt5.setText(step4_5);
        }
        EditText edt6 = (EditText) view.findViewById(R.id.edit6);
        if(!step5_1.equals("") && !step5_1.equals("null"))
        {
            edt6.setText(step5_1);
        }

        TextView objetivoprinc = (TextView) view.findViewById(R.id.objetivoprinc);
        if(!step2.equals("") && !step2.equals("null"))
        {

            objetivoprinc.setText(transformaobjetivos(step2));
        }
        else
        {
            objetivoprinc.setText(getString(R.string.step5_pos4));
        }

        TextView nivelatividades = (TextView) view.findViewById(R.id.nivelatividades);
        if(!step3.equals("") && !step3.equals("null"))
        {

            nivelatividades.setText(transformaatividades(step3));
        }
        else
        {
            nivelatividades.setText(getString(R.string.step5_pos4));
        }

        TextView areasemphasize = (TextView) view.findViewById(R.id.areasemphasize);
        if(!step5_2b.equals("") && !step5_2b.equals("null"))
        {

            areasemphasize.setText(step5_2b);
        }
        else
        {
            areasemphasize.setText(getString(R.string.step5_pos4));
        }
        TextView areasemphasize2 = (TextView) view.findViewById(R.id.areasemphasize2);
        if(!step5_3b.equals("") && !step5_3b.equals("null"))
        {

            areasemphasize2.setText(step5_3b);
        }
        else
        {
            areasemphasize2.setText(getString(R.string.step5_pos4));
        }
    }

    public void salvavariaveis()
    {
        String step4_1 = "";
        String step4_2 = "";
        String step4_3 = "";
        String step4_4 = "";
        String step4_5 = "";
        String step5_1 = "";
        EditText edt1 = (EditText) getActivity().findViewById(R.id.edit1);
        EditText edt2 = (EditText) getActivity().findViewById(R.id.edit2);
        EditText edt3 = (EditText) getActivity().findViewById(R.id.edit3);
        EditText edt4 = (EditText) getActivity().findViewById(R.id.edit4);
        EditText edt5 = (EditText) getActivity().findViewById(R.id.edit5);
        EditText edt6 = (EditText) getActivity().findViewById(R.id.edit6);
        String valor1 = edt1.getText().toString();
        step4_2 = edt2.getText().toString();
        String valor3 = edt3.getText().toString();
        step4_4 = edt4.getText().toString();
        step4_5 = edt5.getText().toString();
        step5_1 = edt6.getText().toString();
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
        step4_1 = valor1;
        step4_3 = valor3;


        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("step4_1", step4_1);
        editor.putString("step4_2", step4_2);
        editor.putString("step4_3", step4_3);
        editor.putString("type_weight", step4_3);
        editor.putString("step4_4", step4_4);
        editor.putString("step4_5", step4_5);
        editor.putString("step5_1", step5_1);
        editor.commit();
    }
    public void enviasettings()
    {
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String step2 = preferences.getString("step2", "");
        String step3 = preferences.getString("step3", "");

        String step4_1 = "";
        String step4_2 = "";
        String step4_3 = "";
        String step4_4 = "";
        String step4_5 = "";
        String step5_1 = "";
        EditText edt1 = (EditText) getActivity().findViewById(R.id.edit1);
        EditText edt2 = (EditText) getActivity().findViewById(R.id.edit2);
        EditText edt3 = (EditText) getActivity().findViewById(R.id.edit3);
        EditText edt4 = (EditText) getActivity().findViewById(R.id.edit4);
        EditText edt5 = (EditText) getActivity().findViewById(R.id.edit5);
        EditText edt6 = (EditText) getActivity().findViewById(R.id.edit6);
        if(edt1.getText().toString().equals(""))
        {
            fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos5), ct1);
            return;
        }
        else if(edt2.getText().toString().equals(""))
        {
            fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos6), ct1);
            return;
        }
        else if(edt3.getText().toString().equals(""))
        {
            fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos7), ct1);
            return;
        }
        else if(edt4.getText().toString().equals(""))
        {
            fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos8), ct1);
            return;
        }
        else if(edt5.getText().toString().equals(""))
        {
            fb.mensagemerro(getString(R.string.step4_pos4), getString(R.string.step4_pos9), ct1);
            return;
        }
        else if(edt6.getText().toString().equals(""))
        {
            fb.mensagemerro(getString(R.string.step5_pos1), getString(R.string.step5_pos2), ct1);
            return;
        }
        else
        {
            String valor1 = edt1.getText().toString();
            step4_2 = edt2.getText().toString();
            String valor3 = edt3.getText().toString();
            step4_4 = edt4.getText().toString();
            step4_5 = edt5.getText().toString();
            step5_1 = edt6.getText().toString();
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
            step4_1 = valor1;
            step4_3 = valor3;
        }


        String step5_2 = preferences.getString("step5_2", "");
        String step5_2b = preferences.getString("step5_2b", "");
		
		String step5_3 = preferences.getString("step5_3", "");
        String step5_3b = preferences.getString("step5_3b", "");

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
        params.put("id_main_goal", step2);
        params.put("id_activity_level", step3);
        params.put("gender", step4_1);
        params.put("birthday", step4_2);
        params.put("type_weight", step4_3);
        params.put("height", step4_4);
        params.put("weight", step4_5);
        params.put("minutes_per_workout", step5_1);
        params.put("treinos", treinosjson);

        String[] separated = step5_2.split(",");
        for(int i=0; i<separated.length; i++)
        {
            String strf = separated[i];
            params.put("emphasize["+i+"]", strf);
        }
		
		String[] separated2 = step5_3.split(",");
        for(int i=0; i<separated2.length; i++)
        {
            String strf = separated2[i];
            params.put("exclude["+i+"]", strf);
        }
        params.put("contemp1", ""+separated.length);
        params.put("contemp2", ""+separated2.length);

        progress = new ProgressDialog(getActivity());
        progress.setTitle(getString(R.string.esqueci_pos1));
        progress.setMessage(getString(R.string.esqueci_pos2));
        progress.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"profile?token=" + token, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(getView() != null) {
                            progress.dismiss();
                            //salvando sessão
                            SharedPreferences preferences =
                                    getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("preenchido", "1");
                            editor.putString("modificoutreino", "1");
                            editor.commit();
                            editou("0");

                            salvavariaveis();

                            progress.dismiss();
                            fb.mensagemerro(getString(R.string.step6_pos3), getString(R.string.step6_pos4), ct1);
                        }
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
    public String transformaatividades(String str1)
    {
        String aux1 = "";
        if(str1.equals("1"))
        {
            aux1 = getString(R.string.step3_3);
        }
        if(str1.equals("2"))
        {
            aux1 = getString(R.string.step3_5);
        }
        if(str1.equals("3"))
        {
            aux1 = getString(R.string.step3_7);
        }
        if(str1.equals("4"))
        {
            aux1 = getString(R.string.step3_9);
        }
        return aux1;
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
            if(separated[i].equals("6"))
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
}