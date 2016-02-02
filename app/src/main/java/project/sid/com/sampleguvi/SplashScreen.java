
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sidha_000 on 2/1/2016.
 */

public class SplashScreen extends Activity {

    SessionManager session;
    String email;
    double latitude,longitude;
    String json;
    JSONArray users = null;
    public static String[] name;
    public static String[] mobile;
    public static double[] alatitude;
    public static double[] alongitude;
    public static String[] languages;
    public static String[] teacher;
    public static int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        session = new SessionManager(getApplicationContext());
         name = new String[100];
         mobile = new String[100];
         alatitude = new double[100];
        alongitude = new double[100];
         languages = new String[100];
         teacher = new String[100];

        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
         email = user.get(SessionManager.KEY_EMAIL);
        GPSTracker gps = new GPSTracker(SplashScreen.this);
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        new LocationUpdate().execute();






    }

    private class LocationUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            int count=2;

            try {
                // http://tchinmai.xyz/guvi/locationupdate.php?location=13.0336,80.2687&email=sid2@abc.com
                HttpGet httpget = new HttpGet("http://tchinmai.xyz/guvi/locationupdate.php?location="+latitude+","+longitude+"&email="+email);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entityres = response.getEntity();
                json = EntityUtils.toString(response.getEntity());
                Log.e("in spalsh","email is  "+ email);
                Log.e("json", json + "");
                // Getting JSON Array node
                JSONObject jsonObj = new JSONObject(json);
                users = jsonObj.getJSONArray("users");
                for ( i = 0; i < users.length(); i++)
                {
                    count=2;
                    JSONObject c = users.getJSONObject(i);
                   name[i]=c.getString("Name");
                    mobile[i]=c.getString("mobile");
                    teacher[i]=c.getString("teacher");
                    languages[i]=c.getString("languages");
                    String location = c.getString("Location");
                    for (String retval: location.split(",", 2)){
                        if(count % 2 == 0)
                        alatitude[i]=Double.parseDouble(retval);
                        else
                            alongitude[i]=Double.parseDouble(retval);
                        count = count + 1;

                    };
                 Log.e(i+name[i]+mobile[i]+location+teacher[i],"+"+mobile[i]+alatitude[i]+alongitude[i]);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i = new Intent(SplashScreen.this,MapsActivity.class);
            startActivity(i);
        }

    }

    }
