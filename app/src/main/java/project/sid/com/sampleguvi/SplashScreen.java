
package project.sid.com.sampleguvi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sidha_000 on 2/1/2016.
 */

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    SessionManager session;
    private SharedPreferences sharedPreferences;
    String uemail, upassword;
    String json, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.contains("email")) {
            uemail = sp.getString("email", "");
            upassword = sp.getString("password", "");
          new GetContacts().execute();
        }
        else
        {Intent i = new Intent(SplashScreen.this,MainActivity.class);
        startActivity(i);}
    }
    private class GetContacts extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                Log.e(uemail,"lol  "+upassword);
                HttpGet httpget = new HttpGet("http://tchinmai.xyz/guvi/login.php?password=" + upassword + "&email=" + uemail);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entityres = response.getEntity();
                json = EntityUtils.toString(response.getEntity());
                Log.e("jsonfgsv", json + "");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (json != null) {
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    result = jsonObject.getString("result");
                    Log.e("result", result);
                    if (result.equals("success")) {



                        Intent i = new Intent(SplashScreen.this, MapsActivity.class);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}