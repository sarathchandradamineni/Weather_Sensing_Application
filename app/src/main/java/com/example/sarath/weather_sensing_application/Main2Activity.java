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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Main2Activity extends AppCompatActivity {
    TextView citynameTextViewJava;
    TextView time_at_location_text_view;
    TextView temp_val_java,max_min_val_java,sunrise_time_val_java,sunset_time_val_java,wind_speed_val_java,weather_desc,humidity_val,pressure_val;
    TextView weather_desc_textview;
    String city_name;
    String country_name;
    String time_zone_code;
    String time_zone;
    String pressure_string;
    String humidity_string;
    String weather_desc_string;
    //DecimalFormat numberFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //citynameTextViewJava = (TextView) findViewById(R.id.cityNameTextView);
        Intent intent = getIntent();
        String cityName = getIntent().getStringExtra("city name");
        temp_val_java = (TextView)findViewById(R.id.temp_val);
        max_min_val_java = (TextView) findViewById(R.id.max_min_val);
        sunrise_time_val_java = (TextView) findViewById(R.id.sunrise_time_val);
        sunset_time_val_java = (TextView) findViewById(R.id.sunset_time_val);
        wind_speed_val_java = (TextView) findViewById(R.id.wind_speed_val);
        weather_desc = (TextView) findViewById(R.id.description_weather);
        humidity_val = (TextView) findViewById(R.id.humidity_val);
        pressure_val = (TextView) findViewById(R.id.pressure_val);
        weather_desc_textview = (TextView) findViewById(R.id.description_weather);

        //citynameTextViewJava.setText(cityName);
        DownloadTask task = new DownloadTask();
        //numberFormat = new DecimalFormat("##.00");
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=677f7c25514555db8caaee8d99026ac5";
        //String url = "http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=677f7c25514555db8caaee8d99026ac5";
        //(ganesh)http://dataservice.accuweather.com/locations/v1/search?apikey=9kZsrJUuiejXesmY9a4f38aGkANp7Jlu&q=mysore
        //(otg)http://dataservice.accuweather.com/locations/v1/search?apikey=%090todhGjgIPwjSVWQxo0RYoOZNoqvprbG&q=karlskrona
        String url2 = "http://dataservice.accuweather.com/locations/v1/search?apikey=9kZsrJUuiejXesmY9a4f38aGkANp7Jlu&q="+cityName;
        task.execute(url,url2);
        //task.execute(url2);

        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt") );
        String gmtTime = df.format(new Date());
        Toast.makeText(this,gmtTime,Toast.LENGTH_LONG).show();

    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

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
                url2 = new URL(urls[1]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection2 = (HttpURLConnection) url2.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStream in2 = urlConnection2.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);
                InputStreamReader reader2 = new InputStreamReader(in2);

                int data = reader.read();
                int data2 = reader2.read();

                while(data != -1)
                {
                    char current = (char)data;
                    results += current;
                    data = reader.read();
                }

                while(data2 != -1)
                {
                    char current2 = (char)data2;
                    loc_results += current2;
                    data2 = reader2.read();
                }

                Log.i("Website Content",results);
                Log.i("location results",loc_results);

                JSONObject jsonObject = new JSONObject(results);
                JSONArray jsonArray_location = new JSONArray(loc_results);

                JSONObject jsonObject2 = jsonArray_location.getJSONObject(0);
                city_name = jsonObject2.getString("EnglishName");
                Log.i("city name",city_name);

                JSONObject country_details = jsonObject2.getJSONObject("Country");
                country_name = country_details.getString("EnglishName");

                JSONObject time_zone_details = jsonObject2.getJSONObject("TimeZone");
                time_zone_code = time_zone_details.getString("Code");
                time_zone = time_zone_details.getString("Name");

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
                citynameTextViewJava = (TextView) findViewById(R.id.cityNameTextView);
                time_at_location_text_view = (TextView) findViewById(R.id.time_at_location);
                citynameTextViewJava.setText(city_name+","+country_name);
                time_at_location_text_view.setText("Time zone: "+time_zone_code+", "+time_zone);
                humidity_val.setText(humidity_string+"%");
                pressure_val.setText(pressure_string+"hPa");
                weather_desc_textview.setText(weather_desc_string);
        }

        public double kelvinToCelsius(double kelvin)
        {
            double celsius = kelvin - 273.15;
            return celsius;
        }
    }

}
