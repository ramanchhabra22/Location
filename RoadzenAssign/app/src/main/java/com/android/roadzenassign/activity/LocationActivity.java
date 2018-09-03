package com.android.roadzenassign.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.roadzenassign.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.android.roadzenassign.constants.AppConstant.GPS_TIME_INTERVAL;
import static com.android.roadzenassign.constants.AppConstant.LOCATION_PERMISSION_CODE;

public class LocationActivity extends AppCompatActivity {


  private LocationManager locationManager;
  private Location location;

  private TextView customerAddress;
  private TextView customerAddressView;
  private TextView customerZipcode;
  private TextView customerZipcodeView;
  private Button trackButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initComponent();
    initListener();

  }

  private void initComponent(){
    customerAddress = findViewById(R.id.cl_address);
    customerAddressView = findViewById(R.id.cl_address_view);
    customerZipcode = findViewById(R.id.cl_zipcode);
    customerZipcodeView = findViewById(R.id.cl_zipcode_view);
    trackButton = findViewById(R.id.cl_track_location);
  }

  private void initListener(){
    trackButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        checkLocationPermission();
      }
    });
  }

  private void checkLocationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      int locationPermission = ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
      if (locationPermission != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
      } else {
        startHandler();
      }
    } else {
      startHandler();
    }
  }

  private void startHandler() {
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      public void run() {
        obtainLocation();
        handler.postDelayed(this, 300000);
      }
    }, 100);
  }

  private void obtainLocation() {
    if (locationManager == null)
      locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
      }
      location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      getAddress(location);
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, 0, locationListener);
    }
  }

  private LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
      locationManager.removeUpdates(locationListener);
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  };

  private void getAddress(Location location) {
    if (location != null) {
      double longitude = location.getLongitude();
      double latitude = location.getLatitude();
      Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
      try {
        List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (listAddresses != null && listAddresses.size() > 0) {

          setAddress(listAddresses);

        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void setAddress(List<Address> listAddresses) {
    String addressLine = listAddresses.get(0).getAddressLine(0);
    Address address = listAddresses.get(0);

    String city = address.getLocality();
    String area = address.getSubLocality();
    String postalCode = address.getPostalCode();
    String country = address.getCountryName();
    String state = address.getAdminArea();

    customerAddress.setVisibility(View.VISIBLE);
    customerAddressView.setVisibility(View.VISIBLE);
    customerZipcode.setVisibility(View.VISIBLE);
    customerZipcodeView.setVisibility(View.VISIBLE);

    customerAddressView.setText(addressLine);
    customerZipcodeView.setText(postalCode);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == LOCATION_PERMISSION_CODE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        startHandler();
      } else {
        Toast.makeText(this, getResources().getString(R.string.location_permission_denied), Toast.LENGTH_LONG).show();
      }
    }
  }

}
