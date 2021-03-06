package com.br.leanlife;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Raphael on 13/06/2016.
 */
public class TutorialActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout pager_indicator;

    String urlf = "https://leanlifeapp.com/api/public/";
    ProgressDialog progress = null;
    Activity ct1 = null;
    FuncoesBasicas fb = new FuncoesBasicas();

    private CallbackManager mCallbackManager;

    private FirebaseAnalytics mFirebaseAnalytics;

    private boolean mSignInClicked;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private static final int RC_SIGN_IN = 500;
    private static final int PERMISSION_GPLUS = 1700;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fb.configuralinguaini(this);
        setContentView(R.layout.activity_tutorial);

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
                TutorialActivity.class.getSimpleName()
        );
        mFirebaseAnalytics.logEvent(
                "entrou_tutorial",
                screenView
        );

        SharedPreferences preferences =
                getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String preenchido = preferences.getString("preenchido", "");
        if(token != null && !token.equals("") && !token.equals("null"))
        {
            if(preenchido.equals("1"))
            {
                Intent myIntent = new Intent(TutorialActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
            else
            {
                Intent myIntent = new Intent(TutorialActivity.this, VideoActivity.class);
                startActivity(myIntent);
                finish();
            }
        }
        else
        {
            List<Fragment> list = new ArrayList<Fragment>();
            list.add(new Tut1Fragment());
            list.add(new Tut2Fragment());
            list.add(new Tut3Fragment());
            list.add(new Tut4Fragment());
            ViewPagerAdapter a = new ViewPagerAdapter(getSupportFragmentManager(), list);
            ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            pager.setAdapter(a);
            pager.addOnPageChangeListener(this);
            pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

            dotsCount = a.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot,null));
                }
                else
                {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(8, 0, 8, 0);

                pager_indicator.addView(dots[i], params);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot,null));
            }
            else
            {
                dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

            loginface();


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        }
    }

    public void entroupelogoogle()
    {
        if (ActivityCompat.checkSelfPermission(ct1, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},
                        PERMISSION_GPLUS);
            }
            else
            {
                signInWithGplus();
            }
        } else {
            signInWithGplus();
        }
    }

    public void entroupeloface()
    {
        LoginManager.getInstance().logInWithReadPermissions(ct1, Arrays.asList("email","public_profile"));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_GPLUS) {
            if(grantResults!=null&&grantResults.length>0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signInWithGplus();
                }
            }
        }
    }
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    private void resolveSignInError() {
        if (mConnectionResult!=null&&mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result!=null&&!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {



        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        else
        {
            super.onActivityResult(requestCode, responseCode, intent);
            mCallbackManager.onActivityResult(requestCode,
                    responseCode, intent);
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;

        getProfileInformation();

    }
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                logingoogle3(personGooglePlusProfile, email, personName, personPhotoUrl.substring(0,personPhotoUrl.lastIndexOf("=")+1)+"200");

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            mGoogleApiClient.connect();
                        }

                    });
        }
    }


    public void loginface()
    {

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        //Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {

                                            String email = object.getString("email");
                                            String id = object.getString("id");
                                            String name = object.getString("name");
                                            String foto = "https://graph.facebook.com/" + id + "/picture?type=large";

                                            loginface3(id, email, name, foto);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        //String birthday = object.getString("birthday");
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                        //Log.d("teste", loginResult.getAccessToken().getUserId());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
    }
    public void logingoogle3(String id,String email,String name,String foto)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", id);
        params.put("id_google", id);
        params.put("name", name);
        params.put("photo", foto);
        params.put("tipodevice", "android");

        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.tut4_pos3));
        progress.setMessage(getString(R.string.tut4_pos4));
        progress.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"creategoogle", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            progress.dismiss();
                            JSONObject jsonArray = response.getJSONObject("data");

                            //salvando sessão
                            SharedPreferences preferences =
                                    getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", jsonArray.getString("token"));
                            editor.putString("name", jsonArray.getString("name") );
                            editor.putString("id", jsonArray.getString("id") );
                            editor.putString("email", jsonArray.getString("email") );
                            editor.putString("photo", jsonArray.getString("photo") );
                            editor.putString("preenchido", jsonArray.getString("preenchido") );
                            editor.putString("type_weight", jsonArray.getString("type_weight") );
                            editor.commit();

                            if(jsonArray.getString("preenchido").equals("1"))
                            {
                                Intent myIntent = new Intent(TutorialActivity.this, MainActivity.class);
                                startActivity(myIntent);
                                signOutFromGplus();
                                finish();
                            }
                            else
                            {
                                Intent myIntent = new Intent(TutorialActivity.this, VideoActivity.class);
                                startActivity(myIntent);
                                signOutFromGplus();
                                finish();
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            fb.mensagemerro(getString(R.string.tut4_pos1), getString(R.string.tut4_pos2), ct1);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        fb.mensagemerro(getString(R.string.tut4_pos1), getString(R.string.tut4_pos2), ct1);
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    public void loginface3(String id,String email,String name,String foto)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", id);
        params.put("id_face", id);
        params.put("name", name);
        params.put("photo", foto);
        params.put("tipodevice", "android");

        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.tut4_pos3));
        progress.setMessage(getString(R.string.tut4_pos4));
        progress.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, urlf+"createface", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            progress.dismiss();
                            JSONObject jsonArray = response.getJSONObject("data");

                            //salvando sessão
                            SharedPreferences preferences =
                                    getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", jsonArray.getString("token"));
                            editor.putString("name", jsonArray.getString("name") );
                            editor.putString("id", jsonArray.getString("id") );
                            editor.putString("email", jsonArray.getString("email") );
                            editor.putString("photo", jsonArray.getString("photo") );
                            editor.putString("preenchido", jsonArray.getString("preenchido") );
                            editor.putString("type_weight", jsonArray.getString("type_weight") );
                            editor.commit();

                            if(jsonArray.getString("preenchido").equals("1"))
                            {
                                Intent myIntent = new Intent(TutorialActivity.this, MainActivity.class);
                                startActivity(myIntent);
                                finish();
                            }
                            else
                            {
                                Intent myIntent = new Intent(TutorialActivity.this, VideoActivity.class);
                                startActivity(myIntent);
                                finish();
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            fb.mensagemerro(getString(R.string.tut4_pos1), getString(R.string.tut4_pos2), ct1);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        fb.mensagemerro(getString(R.string.tut4_pos1), getString(R.string.tut4_pos2), ct1);
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    public static boolean ValidaEmail(String str)
    {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(str).matches();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageSelected(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < dotsCount; i++) {
               dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot, null));
            }
            dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot, null));
        }
        else
        {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }
            dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        }

    }
}
