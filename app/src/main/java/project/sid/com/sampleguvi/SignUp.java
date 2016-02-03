package project.sid.com.sampleguvi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText cpassword;
    private EditText phone;
    private AutoCompleteTextView autoCompleteTextView;
    private Switch teacher;
    private Button button;
    String uname;
    String uemail;
    String upassword;
    String ucpassword;
    String uphone;
    Boolean uteacher;
    String ulanguage;


    String[] languages={"Android ","java","IOS","SQL","JDBC","Web services" ,"Clojure","Haskell"};
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        session = new SessionManager(getApplicationContext());
        name = (EditText) findViewById(R.id.editText7);
        email = (EditText) findViewById(R.id.editText3);
        password = (EditText) findViewById(R.id.editText4);
        cpassword = (EditText) findViewById(R.id.editText5);
        phone = (EditText) findViewById(R.id.editText6);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        teacher = (Switch) findViewById(R.id.switch1);
        button = (Button) findViewById(R.id.button3);
        uname = name.getText().toString();
        uemail = email.getText().toString();
        upassword = password.getText().toString();
        ucpassword = cpassword.getText().toString();
        uphone = phone.getText().toString();
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);
        autoCompleteTextView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
               {
                Log.e("give this app ", "a permission already");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        0);

                GPSTracker gps = new GPSTracker(SignUp.this);
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

                // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        }


        else {
            GPSTracker gps = new GPSTracker(SignUp.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                uname = name.getText().toString();
                uemail = email.getText().toString();
                upassword = password.getText().toString();
                ucpassword = cpassword.getText().toString();
                uphone = phone.getText().toString();
                ulanguage = autoCompleteTextView.getText().toString();

                if(teacher.isChecked())
                {
                    uteacher = true;
                }
                else
                uteacher = false;
                if(upassword.equals(ucpassword)) {
                    AsyncT asyncT = new AsyncT();
                    asyncT.execute();

                }
                else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Wrong Password";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });


    }


    class AsyncT extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... voids) {
          //  HttpClient httpclient = new DefaultHttpClient();
            String url = "http://tchinmai.xyz/guvi/signup.php?"+"name="+uname+"&email="+uemail+"&mobile="+uphone+"&password="+upassword+"&teacher="+uteacher+"&languages="+ulanguage+"&location="+latitude+","+longitude;
            Log.e("url",url);

          //  HttpGet httppost = new HttpGet("http://requestb.in/1e0enfv1?"+"&name="+uname+"&email="+uemail+"&mobile="+uphone+"&password="+upassword+"&teacher="+uteacher+"&languages="+ulanguage+"&location="+latitude+","+longitude);
           try {
              /* List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
               nameValuePairs.add(new BasicNameValuePair("teacher",uteacher.toString()));
               nameValuePairs.add(new BasicNameValuePair("email", uemail));
               nameValuePairs.add(new BasicNameValuePair("languages",ulanguage));
               nameValuePairs.add(new BasicNameValuePair("mobile", uphone));*/
               //HttpResponse response = httpclient.execute(httppost);
               HttpGet httpget = new HttpGet(url);
               HttpClient httpclient = new DefaultHttpClient();
               HttpResponse response = httpclient.execute(httpget);
               Log.e("url", url);
               session.createLoginSession(upassword, uemail);
               Intent i = new Intent(SignUp.this,MapsActivity.class);
               startActivity(i);


               return null;
           }
         catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
           e.printStackTrace();
        }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);




        }
    }
    }














