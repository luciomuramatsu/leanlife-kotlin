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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Tab5Fragment extends Fragment{

    String urlf = "https://leanlifeapp.com/api/public/";
    private RelativeLayout mainLayout;

    private RelativeLayout mainLayout2;
    private BarChart mChart2;
    // we're going to display pie chart for smartphones martket shares
    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<IBarDataSet> dataSets = null;
    public Tab5Fragment() {
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
        return inflater.inflate(R.layout.tab5, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        creategraficos2();
        iniciaform();
    }
    public void iniciaform()
    {

        final EditText dt1 = (EditText) getView().findViewById(R.id.input_data1);
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
                        creategraficos2();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Selecione a data");
                mDatePicker.show();
            }
        });

        final EditText dt2 = (EditText) getView().findViewById(R.id.input_data2);
        dt2.setOnClickListener(new View.OnClickListener() {

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
                        dt2.setText(sdf.format(date));
                        creategraficos2();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Selecione a data");
                mDatePicker.show();
            }
        });
    }
    private void creategraficos2()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        EditText dt1 = (EditText) getView().findViewById(R.id.input_data1);
        EditText dt2 = (EditText) getView().findViewById(R.id.input_data2);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, urlf+"getgrafico?ini="+dt1.getText()+"&fim="+dt2.getText()+"&token="+token ,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            if(getView() != null) {

                                JSONObject dias = null;
                                if(!response.isNull("data"))
                                {
                                    dias = response.getJSONObject("data");
                                }
                                mainLayout2 = (RelativeLayout) getActivity().findViewById(R.id.grafico2a);

                                if(mChart2!=null)
                                {
                                    mChart2.getLegend().setEnabled(false);
                                    mChart2.removeAllViews();
                                    mainLayout2.removeAllViews();
                                }
                                mChart2 = new BarChart(getActivity());
                                // add pie chart to main layout
                                mChart2.setLayoutParams(new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT,
                                        800));
                                mainLayout2.addView(mChart2);


                                dataSets = null;
                                valueSet1 = new ArrayList<>();
                                xVals = new ArrayList<String>();
                                if(dias != null)
                                {
                                    Iterator<?> keys = dias.keys();

                                    Integer cont = 0;
                                    while( keys.hasNext() ) {
                                        String key = (String)keys.next();
                                        if ( dias.getString(key) instanceof String ) {
                                            xVals.add(key);
                                        }
                                    }
                                    Collections.sort(xVals, new Comparator<String>() {
                                        @Override
                                        public int compare(String data1, String data2)
                                        {
                                            DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            String dateAsString = data1;
                                            String dateAsString2 = data2;
                                            try {
                                                Date date = sourceFormat.parse(dateAsString);
                                                Date date2 = sourceFormat.parse(dateAsString2);
                                                return date.compareTo(date2);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    for(int i =0; i< xVals.size(); i++)
                                    {
                                        String meuarray2 = dias.getString(xVals.get(i));

                                        BarEntry v1e1 = new BarEntry(Integer.valueOf(meuarray2), cont);
                                        valueSet1.add(v1e1);

                                        cont++;
                                    }
                                }

                                BarDataSet barDataSet1 = new BarDataSet(valueSet1, getString(R.string.desafiobaixo10));
                                barDataSet1.setColor(Color.parseColor("#ff0000"));

                                dataSets = new ArrayList<>();
                                dataSets.add(barDataSet1);

                                BarData data = new BarData( xVals, dataSets );
                                mChart2.setData(data);
                                mChart2.setDescription("");

                                mChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

                                    @Override
                                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                                        // display msg when value selected
                                        if (e == null)
                                            return;

                                        String tipof = "kg";
                                        SharedPreferences preferences =
                                                getActivity().getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                                        String step4_3 = preferences.getString("step4_3", "");
                                        if(!step4_3.equals("") && !step4_3.equals("null"))
                                        {
                                            if(step4_3.equals("1"))
                                            {
                                                tipof = "kg";
                                            }
                                            else
                                            {
                                                tipof = "lb";
                                            }
                                        }
                                        TextView t1 = (TextView) getActivity().findViewById(R.id.containerestrelas2);
                                        t1.setText(Html.fromHtml(getString(R.string.desafiobaixo11)+ "<br/>"+ Math.round(e.getVal()) + " " + tipof));

                                    }

                                    @Override
                                    public void onNothingSelected() {

                                        TextView t1 = (TextView) getActivity().findViewById(R.id.containerestrelas2);
                                        t1.setText(Html.fromHtml(""));

                                    }
                                });

                                mChart2.animateXY(2000, 2000);
                                mChart2.invalidate();


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