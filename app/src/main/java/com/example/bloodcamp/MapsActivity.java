package com.example.bloodcamp;

import androidx.fragment.app.FragmentActivity;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.bloodcamp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.bloodcamp.DirectionHelpers.FetchURL;
import com.example.bloodcamp.DirectionHelpers.TaskLoadedCallback;
import com.google.android.gms.maps.model.Polyline;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double longitude;
    private double lattitude;
    private Polyline currentPolyline;
    private String name;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 1000; // 1 second
    private final long MIN_DIST = 5; // 5 Meters
    private final int PLACE_PICKER_REQUEST = 1;
    private LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        longitude = getIntent().getDoubleExtra("long", 0.0);
        lattitude = getIntent().getDoubleExtra("lat", 0.0);
        name = getIntent().getStringExtra("name");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       // PlacePicker.I
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_map_api);
        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationListener = location -> {
            try{
                latLng = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("My position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                LatLng bloodCamp = new LatLng(lattitude, longitude);
                mMap.addMarker(new MarkerOptions().position(bloodCamp).title(name));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(bloodCamp));
                //new FetchURL(MapsActivity.this).execute(getUrl(bloodCamp, latLng, "driving"), "driving");
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }

    }
}