package com.example.sarath.weather_sensing_application;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    Button checkWeatherButton;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitide;
    //TextView latlong;
    String city, country, premises, AddressLine, SubLocality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //latlong = (TextView) findViewById(R.id.latlong);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
    }

    public void checkWeather(View view)
    {

        EditText locationEditText = (EditText)findViewById(R.id.location);
        String enteredlocation = locationEditText.getText().toString();
        Toast.makeText(MainActivity.this,locationEditText.getText().toString(),Toast.LENGTH_SHORT).show();
        checkWeatherButton = (Button)findViewById(R.id.checkWeather);

        if(enteredlocation.equals(""))
        {
            Toast.makeText(MainActivity.this, "pleaase enter any location name", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(this,Main2Activity.class);
            intent.putExtra("city name",locationEditText.getText().toString());
            startActivity(intent);
        }


        Log.i("sarath","chandra");
    }

    public void getAutoAddress(View view)
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            OnGPS();
        }
        else
        {
            getLocation();
        }
    }

    public void getLocation()
    {
        if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if(LocationGps != null)
            {
                double lat = LocationGps.getLatitude();
                double longi  = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitide = String.valueOf(longi);

                Toast.makeText(MainActivity.this,latitude+"  "+longitide,Toast.LENGTH_LONG).show();


                try {
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addresses = null;
                    addresses = geocoder.getFromLocation(lat,longi,1);
                    city = addresses.get(0).getLocality();
                    country = addresses.get(0).getCountryName();
                    premises = addresses.get(0).getPremises();
                    AddressLine = addresses.get(0).getAddressLine(0);
                    SubLocality = addresses.get(0).getSubLocality();
                    //latlong.setText("city name: "+city+" country name: "+country+" premises: "+premises+" Address line: "+AddressLine+" sublocality "+SubLocality);



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(LocationNetwork != null)
            {
                double lat = LocationNetwork.getLatitude();
                double longi  = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitide = String.valueOf(longi);

                Toast.makeText(MainActivity.this,latitude+"  "+longitide,Toast.LENGTH_LONG).show();

                try {
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addresses = null;
                    addresses = geocoder.getFromLocation(lat,longi,1);
                    city = addresses.get(0).getLocality();
                    country = addresses.get(0).getCountryName();
                    premises = addresses.get(0).getPremises();
                    AddressLine = addresses.get(0).getAddressLine(0);
                    SubLocality = addresses.get(0).getSubLocality();
                    //latlong.setText("city name: "+city+" country name: "+country+" premises: "+premises+" Address line: "+AddressLine+" sublocality "+SubLocality);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(LocationPassive != null)
            {
                double lat = LocationPassive.getLatitude();
                double longi  = LocationPassive .getLongitude();

                latitude = String.valueOf(lat);
                longitide = String.valueOf(longi);

                Toast.makeText(MainActivity.this,latitude+"  "+longitide,Toast.LENGTH_LONG).show();

                try {
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addresses = null;
                    addresses = geocoder.getFromLocation(lat,longi,1);
                    city = addresses.get(0).getLocality();
                    country = addresses.get(0).getCountryName();
                    premises = addresses.get(0).getPremises();
                    AddressLine = addresses.get(0).getAddressLine(0);
                    SubLocality = addresses.get(0).getSubLocality();
                    //latlong.setText("city name: "+city+", country name: "+country+", premises: "+premises+", Address line: "+AddressLine+", sublocality "+SubLocality);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(this, "can'nt get your location", Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(this,Main3Activity.class);
            intent.putExtra("address",AddressLine+","+SubLocality);
            startActivity(intent);

        }


    }

    public void OnGPS()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}
