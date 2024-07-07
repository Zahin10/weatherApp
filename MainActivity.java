package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    StringBuffer strbufAll = new StringBuffer();
    EditText zipText;

    public static String zipcode = "";

    static int tempCur;
    static TextView date1;
    static TextView date2;
    static TextView date3;
    static TextView date4;
    static TextView date5;
    static TextView weather1;
    static TextView weather2;
    static TextView weather3;
    static TextView weather4;
    static TextView weather5;
    static TextView min1, temp, funText;
    static TextView min2;
    static TextView min3;
    static TextView min4;
    static TextView min5;
    static TextView max1;
    static TextView max2;
    static TextView max3;
    static TextView max4;
    static TextView max5;

    static TextView longtiude;
    static TextView latitude;
    static TextView locationOne;

    static ImageView bigOne;
    static ImageView smallOne;
    static ImageView smallTwo;
    static ImageView smallThree;
    static ImageView smallFour;
    static ImageView smallFive;

    static String globalLatVal;
    static String globalLongVal;
    static String globalLocationVal;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        date3 = findViewById(R.id.date3);
        date4 = findViewById(R.id.date4);
        date5 = findViewById(R.id.date5);
        weather1 = findViewById(R.id.weather1);
        weather2 = findViewById(R.id.weather2);
        weather3 = findViewById(R.id.weather3);
        weather4 = findViewById(R.id.weather4);
        weather5 = findViewById(R.id.weather5);
        temp = findViewById(R.id.currentTemp);
        min1 = findViewById(R.id.min1);
        min2 = findViewById(R.id.min2);
        min3 = findViewById(R.id.min3);
        min4 = findViewById(R.id.min4);
        min5 = findViewById(R.id.min5);
        max1 = findViewById(R.id.max1);
        max2 = findViewById(R.id.max2);
        max3 = findViewById(R.id.max3);
        max4 = findViewById(R.id.max4);
        max5 = findViewById(R.id.max5);
        bigOne = findViewById(R.id.bigOne);
        smallOne = findViewById(R.id.smallOne);
        smallTwo = findViewById(R.id.smallTwo);
        smallThree = findViewById(R.id.smallThree);
        smallFour = findViewById(R.id.smallFour);
        smallFive = findViewById(R.id.smallFive);
        longtiude = findViewById(R.id.Longitude);
        latitude = findViewById(R.id.Latitude);
        locationOne = findViewById(R.id.Location);
        funText = findViewById(R.id.funnyText);


        zipText = findViewById(R.id.zipCode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipcode = zipText.getText().toString();
                if (isValidZipCode(zipcode)) {
                    AsyncThread task = new AsyncThread();
                    task.execute();
                } else {
                    Toast.makeText(MainActivity.this, "This is an invalid zip code", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    public boolean isValidZipCode(String zipCode) {

        String regex = "^[0-9]{5}$";
        return zipCode.matches(regex);
    }
    public class AsyncThread extends AsyncTask<String, Void, String> {

        String inputLine;
        String temp2;

        ArrayList<String> tempVal;
        ArrayList<String> tempMin;
        ArrayList<String> tempMax;
        ArrayList<String> tempFeelLike;


        String location;
        String latVal;
        String longVal;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL first = new URL("https://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode + ",US&appid=0cf334ff5272a0e8c471c0d91dbebf9c");
                URLConnection urlConnection1 = first.openConnection();
                InputStream stream1 = urlConnection1.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(stream1));
                StringBuilder data1 = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    data1.append(line);
                }
                JSONObject obj = new JSONObject(data1.toString());
                latVal = obj.get("lat").toString();
                longVal = obj.get("lon").toString();
                location = obj.getString("name");
                globalLatVal = latVal;
                globalLongVal = longVal;
                globalLocationVal = location;

                Log.d("tag", latVal);
                Log.d("tag", longVal);

            } catch (FileNotFoundException e) {
                return "invalid_zip";
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }

            try {
                URL second2 = new URL("https://api.openweathermap.org/data/2.5/forecast?lat=" + latVal + "&lon=" + longVal + "&appid=0cf334ff5272a0e8c471c0d91dbebf9c");
                URLConnection urlConnection2 = second2.openConnection();
                InputStream stream2 = urlConnection2.getInputStream();
                BufferedReader second = new BufferedReader(new InputStreamReader(stream2));
                StringBuilder data2 = new StringBuilder();
                String line;
                while ((line = second.readLine()) != null) {
                    data2.append(line);
                }
                return data2.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if ("invalid_zip".equals(result)) {
                Toast.makeText(MainActivity.this, "This zip code does not exist.", Toast.LENGTH_LONG).show();
            } else if (result != null) {
                try {
                    JSONObject weatherInfo = new JSONObject(result);
                    JSONArray weather = weatherInfo.getJSONArray("list");
                    getWeather(weather);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Error parsing weather data.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, "An error occurred while fetching weather data.", Toast.LENGTH_LONG).show();
            }
        }


    }

    @SuppressLint("SetTextI18n")
    protected static void getWeather(JSONArray weather) {
        HashSet<String> processedDates = new HashSet<>();
        ArrayList<String> dailyDates = new ArrayList<>();
        ArrayList<JSONObject> dailyForecasts = new ArrayList<>();

        try {
            for (int i = 0; i < 40; i+=8) {
                JSONObject forecast = weather.getJSONObject(i);
                String dtTxt = forecast.getString("dt_txt");
                String[] parts = dtTxt.split(" ");
                String dateStr = parts[0];
                String[] dateComponents = dateStr.split("-");
                String monthAndDay = dateComponents[1] + "-" + dateComponents[2];
                if (!processedDates.contains(monthAndDay)) {
                    processedDates.add(monthAndDay);
                    dailyDates.add(monthAndDay);
                    dailyForecasts.add(forecast);

                    if (dailyForecasts.size() == 5) {
                        break;
                    }
                }
            }
            for (int i = 0; i < dailyForecasts.size(); i++) {
                JSONObject forecast = dailyForecasts.get(i);
                JSONObject mainArray = forecast.getJSONObject("main");
                ImageView[] imageViews = new ImageView[]{bigOne, smallOne, smallTwo, smallThree, smallFour, smallFive};

                double maxTempInKelvin = mainArray.getDouble("temp_max");
                double maxTempInFahrenheit = (9.0/5) * (maxTempInKelvin - 273.15) + 32;
                String maxTemp = String.format(Locale.US, "%.2f°F", maxTempInFahrenheit);

                double minTempInKelvin = mainArray.getDouble("temp_min");
                double minTempInFahrenheit = (9.0/5) * (minTempInKelvin - 273.15) + 32;
                String minTemp = String.format(Locale.US, "%.2f°F", minTempInFahrenheit);

                JSONArray weatherArray = forecast.getJSONArray("weather");
                String description = weatherArray.getJSONObject(0).getString("description");
                String spongeBobQuote = getSpongeBobQuote(description);
                int imageResource = getImageResourceForWeather(description);
                imageViews[i].setImageResource(imageResource);

                String formattedDate = dailyDates.get(i);

                TextView[] dateViews = new TextView[]{date1, date2, date3, date4, date5};
                TextView[] weatherViews = new TextView[]{weather1, weather2, weather3, weather4, weather5};
                TextView[] minViews = new TextView[]{min1, min2, min3, min4, min5};
                TextView[] maxViews = new TextView[]{max1, max2, max3, max4, max5};

                if(i == 4) {
                    imageViews[5].setImageResource(imageResource);
                }


                dateViews[i].setText(formattedDate);
                weatherViews[i].setText(description);
                minViews[i].setText("Min:"+minTemp);
                maxViews[i].setText("Max:"+maxTemp);
                double tempCur = mainArray.getDouble("temp");
                double tempCurFar = (9.0/5) * (tempCur - 273.15) + 32;
                String tempSetFar = String.format(Locale.US, "%.2f", tempCurFar);
                funText.setText(spongeBobQuote);
                temp.setText("Current Temp: " + tempSetFar + "°F");
                longtiude.setText("Longitude: " + globalLongVal);
                latitude.setText("Latitude:" + globalLatVal);
                locationOne.setText("Location: " + globalLocationVal);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static int getImageResourceForWeather(String description) {
        if(description.contains("clear sky")) {
            Log.d("tag1" , description);
            return R.drawable.happysponge_prev_ui;
        } else if(description.contains("rain")) {
            Log.d("tag2" , description);
            return R.drawable.sadsponge;
        } else if(description.contains("clouds")) {
            Log.d("tag3" , description);
            return R.drawable.brehsponge;
        } else if(description.contains("clear")) {
            Log.d("tag4" , description);
            return R.drawable.monkesponge;
        }  {
            return R.drawable.sportsponge_prev_ui;
        }
    }

    private static String getSpongeBobQuote(String description) {
        if (description.contains("clear sky")) {
            return "Gee, Patrick, isn't it beautiful when the sky's as clear as Squidward's schedule?";
        } else if (description.contains("rain")) {
            return "Looks like it's raining again. Good thing I've got my bubble umbrella!";
        } else if (description.contains("snow")) {
            return "Snow in Bikini Bottom? Time to break out the ol' snowball cannon, Patrick!";
        } else if (description.contains("clouds")) {
            return "Even when the sky is filled with clouds, Bikini Bottom is full of sunny smiles!";
        } else if (description.contains("clear")) {
            // Assuming "clear" is used for sunny/clear weather
            return "Oh boy, oh boy, oh boy! Nothing beats flipping Krabby Patties under the bright and shiny sun!";
        } else {
            // Default quote for unspecified weather conditions
            return "Another day, another adventure at Bikini Bottom!";
        }
    }

}




















/*

            try
            {
                JSONObject weatherInfo = new JSONObject(temp2);
                JSONArray weather = weatherInfo.getJSONArray("list");
                for(int i=0; i<weather.length(); i+=8)
                {
                    JSONObject arrayWeb = weather.getJSONObject(i);
                    JSONObject mainArray = arrayWeb.getJSONObject("main");
                    String temp = String.valueOf(mainArray.getDouble("temp"));
                    tempVal.add(temp);

                    String feelsLike = String.valueOf(mainArray.getDouble("feels_like"));
                    tempFeelLike.add(feelsLike);

                    String minTemp = String.valueOf(mainArray.getDouble("temp_min"));
                    tempMin.add(minTemp);

                    String maxTemp = String.valueOf(mainArray.getDouble("temp_max"));
                    tempMax.add(maxTemp);
                    Log.d("tempVal", tempVal.toString());
                    Log.d("tempFeelLike", tempFeelLike.toString());
                    Log.d("tempMin", tempMin.toString());
                    Log.d("tempMax", tempMax.toString());
                }



            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
 */

/*
protected String doInBackground(String... strings) {
            // This is where you will download your data.
            // You will need to override another method to update the UI
            tempVal = new ArrayList<>();
            tempMin = new ArrayList<>();
            tempMax = new ArrayList<>();
            tempFeelLike = new ArrayList<>();

            StringBuilder data = new StringBuilder();

            String API = "https://api.openweathermap.org/geo/1.0/zip?zip=08824,US&appid=0cf334ff5272a0e8c471c0d91dbebf9c";
            try
            {
                URL mineWeather = new URL(API);
                URLConnection weather2 = mineWeather.openConnection();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(weather2.getInputStream()))) {
                    while ((inputLine = in.readLine()) != null) {
                        data.append(inputLine);

                        Log.d("WeatherData", inputLine);
                    }

                    String info1 = new String();
                    for (String lines; (lines = in.readLine()) != null; info1 += lines);

                    try {
                        JSONObject obj = new JSONObject(info1);
                        //gets information and sets variables
                        lat = obj.get("lat").toString();
                        longit = obj.get("lon").toString();
                        location = obj.getString("name");
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        return null;
                    }
                    in.close();
                } catch (Exception e) {
                    Log.e("TAG",  e.toString());
                    Thread.dumpStack();
                }

            }
            catch (Exception e)
            {
                Log.e("TAG", "Error reading data2", e);
            }

            Log.d("TAG","Thread ");

            String JSONData = data.toString();
            String longVal = "";
            String latVal = "";

            try
            {
                Log.d("tag", data.toString());
                JSONObject firstData = new JSONObject(JSONData);
                Log.d("tag", firstData.toString(4));
                Log.d("LongVAL", longVal);
                Log.d("latVal", latVal);


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            String inputLine2;
            StringBuilder data2_ = new StringBuilder();
            String API5DAY = "https://api.openweathermap.org/data/2.5/forecast?lat="+latVal+"&lon="+longVal+"&appid=0cf334ff5272a0e8c471c0d91dbebf9c";
            try
            {
                URL mineWeather2 = new URL(API5DAY);
                URLConnection weather2 = mineWeather2.openConnection();
                try (BufferedReader second = new BufferedReader(new InputStreamReader(weather2.getInputStream()))) {
                    String data2 = new String();

                    for (String line; (line = second.readLine()) != null; data2 += line);

                    return data2;

                } catch (IOException e) {
                    Log.e("TAG", "Error reading data", e);
                }

            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException(e);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            Log.d("TAG","Thread ");
            //return null;

            return null;
        }

 */