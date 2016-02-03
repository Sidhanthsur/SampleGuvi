package project.sid.com.sampleguvi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private int count=2;
    SessionManager session;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        session = new SessionManager(getApplicationContext());

        session.checkLogin();
        button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                session.logoutUser();
            }
        });


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // password
        String password = user.get(SessionManager.KEY_PASSWORD);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        Log.e(password,"+"+email);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng[] places = new LatLng[100];
        LocationManager mng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = mng.getLastKnownLocation(mng.getBestProvider(new Criteria(), false));


        for(int i =0 ; i < SplashScreen.i;i++)
        {
            places[i] = new LatLng(SplashScreen.alatitude[i],SplashScreen.alongitude[i]);
            if(SplashScreen.teacher[i].equals("true")) {
                mMap.addMarker(new MarkerOptions().position(places[i]).title(SplashScreen.name[i]).snippet("Teacher:" + SplashScreen.languages[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();

            }
                else
                mMap.addMarker(new MarkerOptions().position(places[i]).title(SplashScreen.name[i]).snippet("Learner:" + SplashScreen.languages[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();


        }
       // LatLng sydney = new LatLng(13.0524, 80.2508);
      //  LatLng ooty = new LatLng(11.4118, 76.6954);
      //  LatLng warren = new LatLng(11.931,79.7852);
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Chennai")).showInfoWindow();
      //  mMap.addMarker(new MarkerOptions().position(ooty).title("Ooty")).showInfoWindow();
      //  mMap.addMarker(new MarkerOptions().position(warren).title("Warren"));
       /* CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(13.0524, 80.2508)).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 16);
        mMap.animateCamera(cameraUpdate);

        int  MY_PERMISSIONS_REQUEST_READ_CONTACTS=0;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("give this app ", "a permission already");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                googleMap.setMyLocationEnabled(true);
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        }
        googleMap.setMyLocationEnabled(true);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                arg0.showInfoWindow();


                return true;
            }


        });
        mMap.setOnInfoWindowLongClickListener(new OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                Log.e("yess","woo");

                for(int i = 0 ; i< SplashScreen.i;i++)
                {
                    if(marker.getTitle().equals(SplashScreen.name[i]))
                    {
                        String number = SplashScreen.mobile[i];

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)).putExtra("sms_body", "Hi, I would like to learn "+SplashScreen.languages[i]+" from you . Can you help me ?"));


                    }
                }





            }
        });



    }

}


