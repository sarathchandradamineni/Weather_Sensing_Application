package com.example.sarath.weather_sensing_application;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main3Activity extends AppCompatActivity {
    TextView address_textview_java;
    TextView time_textview_java;
    TextView day_textview_java;
    TextView date_textview_java;
    TextView temp_val_java,max_min_val_java,sunrise_time_val_java,sunset_time_val_java,wind_speed_val_java,humidity_val,pressure_val;
    TextView weather_desc_textview;
    String pressure_string;
    String humidity_string;
    String weather_desc_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = sdf.format(new Date());

        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        String currentTime = sdf2.format(new Date());

        SimpleDateFormat sdf3 = new SimpleDateFormat("E");
        String currentDay = sdf3.format(new Date());

        address_textview_java = (TextView) findViewById(R.id.cityNameTextView);
        time_textview_java = (TextView) findViewById(R.id.time_at_location);
        day_textview_java = (TextView) findViewById(R.id.Day_at_location);
        date_textview_java = (TextView) findViewById(R.id.date_at_location);
        weather_desc_textview = (TextView) findViewById(R.id.description_weather);

        date_textview_java.setText(currentDate);
        time_textview_java.setText(currentTime);
        day_textview_java.setText(currentDay);

        temp_val_java = (TextView)findViewById(R.id.temp_val);
        max_min_val_java = (TextView) findViewById(R.id.max_min_val);
        sunrise_time_val_java = (TextView) findViewById(R.id.sunrise_time_val);
        sunset_time_val_java = (TextView) findViewById(R.id.sunset_time_val);
        wind_speed_val_java = (TextView) findViewById(R.id.wind_speed_val);
        humidity_val = (TextView) findViewById(R.id.humidity_val);
        pressure_val = (TextView) findViewById(R.id.pressure_val);
        Intent intent = getIntent();
        String address_from_location = getIntent().getStringExtra("address");
        address_textview_java.setText("Valhallavägen 1, 371 41 Karlskrona");
        DownloadTask task = new DownloadTask();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=karlskrona&appid=677f7c25514555db8caaee8d99026ac5";
        task.execute(url);


    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... urls) {

            String results = "",loc_results="";
            URL url,url2;

            HttpURLConnection urlConnection = null;
            HttpURLConnection urlConnection2 = null;
            try {

                url = new URL(urls[0]);


                urlConnection = (HttpURLConnection) url.openConnection();


                InputStream in = urlConnection.getInputStream();


                InputStreamReader reader = new InputStreamReader(in);


                int data = reader.read();


                while(data != -1)
                {
                    char current = (char)data;
                    results += current;
                    data = reader.read();
                }



                Log.i("Website Content",results);


                JSONObject jsonObject = new JSONObject(results);

                JSONArray jsonArray_weather = jsonObject.getJSONArray("weather");
                JSONObject jsonObject_weather = jsonArray_weather.getJSONObject(0);
                weather_desc_string = jsonObject_weather.getString("description");

                JSONObject jsonObject_temp_details = jsonObject.getJSONObject("main");
                String current_temp = jsonObject_temp_details.getString("temp");
                pressure_string = jsonObject_temp_details.getString("pressure");
                humidity_string = jsonObject_temp_details.getString("humidity");

                Log.i("temperature details",current_temp);
                Double current_temp_double = Double.parseDouble(current_temp);
                long current_temp_celsius = Math.round(kelvinToCelsius(current_temp_double));


                final String current_temp_celsius_string = Long.toString(current_temp_celsius);
                Log.i("temperature in celsius",current_temp_celsius_string);

                String current_max_temp = jsonObject_temp_details.getString("temp_max");
                String current_min_temp = jsonObject_temp_details.getString("temp_min");

                double current_max_temp_double = Double.parseDouble(current_max_temp);
                double current_min_temp_double = Double.parseDouble(current_min_temp);

                long current_max_temp_double_celsius = Math.round(kelvinToCelsius(current_max_temp_double));
                long current_min_temp_double_celsius = Math.round(kelvinToCelsius(current_min_temp_double));

                final String current_max_temp_double_celsius_string = String.valueOf(current_max_temp_double_celsius);
                final String current_min_temp_double_celsius_string = String.valueOf(current_min_temp_double_celsius);

                Log.i("max temp",current_max_temp_double_celsius_string);
                Log.i("min temp",current_min_temp_double_celsius_string);

                JSONObject jsonObject_sys = jsonObject.getJSONObject("sys");
                String sunrise = jsonObject_sys.getString("sunrise");
                String sunset = jsonObject_sys.getString("sunset");
                String timezone = jsonObject.getString("timezone");

                double sunrise_double = Double.parseDouble(sunrise);
                double sunset_double = Double.parseDouble(sunset);
                double timezone_double = Double.parseDouble(timezone);

                Log.i("sunrise sunset timezone",sunrise+" "+sunset+" "+timezone);
                final java.util.Date sunset_time=new java.util.Date((long)(sunset_double + timezone_double)*1000);
                final java.util.Date sunrise_time=new java.util.Date((long)(sunrise_double + timezone_double)*1000);

                final SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
                Log.i("sunset time:", sdf1.format(sunset_time)+"PM local time");
                Log.i("sunrise time",sdf1.format(sunrise_time)+"AM local time");


                JSONObject jsonObject_wind = jsonObject.getJSONObject("wind");
                String wind_speed = jsonObject_wind.getString("speed");
                Double wind_speed_double = Double.parseDouble(wind_speed);
                long wind_speed_kmperh = Math.round(wind_speed_double * 3.6);
                final String wind_speed_kmperh_string = Long.toString(wind_speed_kmperh);
                Log.i("wind speed in km/h",wind_speed_kmperh_string+" km/hr");



                /*String weatherInfo = jsonObject.getString("weather");
                String temperatureInfo = jsonObject.getString("main");
                String windInfo = jsonObject.getString("wind");
                String nameofplace = jsonObject.getString("name");
                String time_zone = jsonObject.getString("timezone");

                JSONObject temp_details = jsonObject.getJSONObject("main");
                String temp_now = jsonObject.getString("temp_details");
                Log.i("temperature right now",temp_now);

                Log.i("weather information", weatherInfo);
                Log.i("temperature Information",temperatureInfo);
                Log.i("wind information",windInfo);
                Log.i("name of the place",nameofplace);
                Log.i("time zone",time_zone);*/


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wind_speed_val_java.setText(wind_speed_kmperh_string+"km/hr");
                        sunset_time_val_java.setText(sdf1.format(sunset_time)+"PM");
                        sunrise_time_val_java.setText(sdf1.format(sunrise_time)+"AM");
                        max_min_val_java.setText(current_min_temp_double_celsius_string+"°C / "+current_max_temp_double_celsius_string+"°C");
                        temp_val_java.setText(current_temp_celsius_string+"°C");
                        humidity_val.setText(humidity_string+"%");
                        pressure_val.setText(pressure_string+"hPa");
                        weather_desc_textview.setText(weather_desc_string);
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        public double kelvinToCelsius(double kelvin)
        {
            double celsius = kelvin - 273.15;
            return celsius;
        }
    }
}
