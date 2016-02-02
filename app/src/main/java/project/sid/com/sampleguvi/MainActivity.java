package project.sid.com.sampleguvi;

import java.io.InputStream;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class MainActivity extends Activity {



    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;


    private EditText email;
    private EditText password;
    private Button login;
    private Button signup;
    String json;
    String uemail;
    String upassword;
    String result;

    SessionManager session;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());

        email = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        login = (Button) findViewById(R.id.button);
        signup = (Button) findViewById(R.id.button2);






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uemail = email.getText().toString();
                upassword = password.getText().toString();


                new GetContacts().execute();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });


    }


    private class GetContacts extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                HttpGet httpget = new HttpGet("http://tchinmai.xyz/guvi/login.php?password=" + upassword + "&email=" + uemail);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entityres = response.getEntity();
                json = EntityUtils.toString(response.getEntity());
                Log.e("json", json + "");

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

                        session.createLoginSession(upassword, uemail);


                        Intent i = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(i);
                    }
                    else
                        Log.e("Logged in ","failure");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}