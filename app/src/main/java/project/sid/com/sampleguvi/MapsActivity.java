package project.sid.com.sampleguvi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,  GoogleMap.OnInfoWindowLongClickListener {

    private GoogleMap mMap;
    private int count=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(13.0524, 80.2508);
        LatLng ooty = new LatLng(11.4118, 76.6954);
        LatLng warren = new LatLng(11.931,79.7852);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Chennai")).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(ooty).title("Ooty")).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(warren).title("Warren"));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(13.0524, 80.2508)).zoom(9).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        int  MY_PERMISSIONS_REQUEST_READ_CONTACTS=0;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS)
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



     /*  mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

           @Override
           public boolean onMarkerClick(Marker arg0) {


               if (arg0.getTitle().equals("Ooty")) // if marker source is clicked
               {
                   String number = "9176734589";
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                   return true;
               } else if (arg0.getTitle().equals("Warren")) {
                   String number = "12345689";
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                   return true;

               } else {
                   String number = "9962102985";
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                   return true;

               }
           }


        });*/
    }

    @Override
    public void onInfoWindowLongClick(Marker arg0) {
        Log.e("please","come");


            if (arg0.getTitle().equals("Ooty")) // if marker source is clicked
            {
                String number = "9176734589";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
        }
        else
        {
            String number = "9176734555";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
        }



    }
}


