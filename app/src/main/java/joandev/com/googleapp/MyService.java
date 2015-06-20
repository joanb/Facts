package joandev.com.googleapp;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import joandev.com.googleapp.data.FactsContract;

/**
 * Created by joanbarroso on 19/6/15.
 */
public class MyService extends IntentService {

    public static final String LOG_TAG = "ASD";
    String forecastJsonStr = null;
    String type;
    String params[];

    public MyService() {
        super("aligual");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

    Bundle extras = intent.getExtras();
    String myUrl = extras.getString("url");
        type =extras.getString("type");
        params = extras.getStringArray("params");
    Log.v("params", myUrl);
    // These two need to be declared outside the try/catch
    // so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

        // Will contain the raw JSON response as a string.

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            String targetUrl =  myUrl;
            URL url = new URL(targetUrl);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Mashape-Key", "h9m5lwLBczmshPrWvMvNKciNBVUWp1FjEHDjsnXnJCwgGsiVyr");
            urlConnection.setRequestProperty("Accept", "text/plain");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Log.v("JSOOON", "Entrando al JSON parser");
            Log.v("JSOOON", forecastJsonStr);

            getDataFromJson(forecastJsonStr, type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromJson(String NewzJsonStr, String type) throws JSONException {

        final String LIST = "results";
            Log.v("JSOOON", NewzJsonStr);
            JSONObject facts = new JSONObject(NewzJsonStr);

            // Insert the new weather information into the database
            ContentValues contentValues = new ContentValues();
            String text = "null";
            String year = "null";
            String number =  "null";
            String day = "null";
            String month = "null";
            String math_number = "null";
            String trivia_number = "";
            facts.get("text");
            facts.get("text");
            facts.get("text");
            facts.get("text");
            switch (type) {
                case "Date":
                    day = params[0];
                    month = params[1];
                    year = facts.getString("year");
                    break;
                case "Math":
                    math_number = facts.getString("number");
                    break;
                case "Year":
                    year = facts.getString("number");
                    break;
                case "Trivia":
                    trivia_number = facts.getString("number");
                    break;

            }
            text = facts.getString("text");
            contentValues.put(FactsContract.FactEntry.COLUMN_DAY, day);
            contentValues.put(FactsContract.FactEntry.COLUMN_MONTH, month);
            contentValues.put(FactsContract.FactEntry.COLUMN_YEAR, year);
            contentValues.put(FactsContract.FactEntry.COLUMN_MATH_NUMBER, math_number);
            contentValues.put(FactsContract.FactEntry.COLUMN_TRIVIA_NUMBER, trivia_number);
            contentValues.put(FactsContract.FactEntry.COLUMN_TYPE, type);
            contentValues.put(FactsContract.FactEntry.COLUMN_TEXT, text);

            getApplicationContext().getContentResolver().insert(FactsContract.FactEntry.CONTENT_URI, contentValues);

            }
        }



