package joandev.com.googleapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener, Dialog.OnCompleteListener {
    TextView tv;
    String result[];

    String type = "";
    String url = "";
    int typeCase;
    CardView dateCard;
    CardView dateCard2;
    CardView dateCard3;
    CardView dateCard4;

    private final String factType[] = {"Date","Math","random","Trivia","Year"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        dateCard = (CardView) findViewById(R.id.card_view);
        dateCard2 = (CardView) findViewById(R.id.card_view2);
        dateCard3 = (CardView) findViewById(R.id.card_view3);
        dateCard4 = (CardView) findViewById(R.id.card_view4);

        dateCard.setOnClickListener(this);
        dateCard2.setOnClickListener(this);
        dateCard3.setOnClickListener(this);
        dateCard4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_view:
                type = factType[0];
                typeCase = 0;
                break;
            case R.id.card_view2:
                type = factType[1];
                typeCase = 1;
                break;
            case R.id.card_view3:
                type = factType[3];
                typeCase = 3;
                break;
            case R.id.card_view4:
                type = factType[4];
                typeCase = 4;
                break;
        }
        dialogShow(type);
    }

    private void dialogShow(String id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialogFragment = Dialog.newInstance(id);
        dialogFragment.show(ft, "dialog");
    }

    private void callApi(String url) {
        Intent intent;
        Bundle mBundle = new Bundle();
        mBundle.putString("url", url);
        mBundle.putString("type", type);
        mBundle.putStringArray("params", result);

        intent = new Intent(Intent.ACTION_SYNC, null, this, MyService.class);
        intent.putExtras(mBundle);
        startService(intent);
//        Log.v("params", url);
//        Api api = new Api();
//        api.execute(url);
        startActivity(new Intent(this, DetailActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String[] res) {
        result = res;
        setUrls();
    }

    private void setUrls() {
        switch (typeCase) {
            case 0:
                url = "https://numbersapi.p.mashape.com/" + result[1] + "/" + result[0] + "/date?fragment=true&json=true";
                break;
            case 1:
                url = "https://numbersapi.p.mashape.com/" + result[0] +"/math?fragment=true&json=true";
                break;
            case 3:
                url = "https://numbersapi.p.mashape.com/" + result[0] + "/trivia?fragment=true&json=true&notfound=floor";
                break;
            case 4:
                url = "https://numbersapi.p.mashape.com/"+ result[0] + "/year?fragment=true&json=true";
                break;
        }
        callApi(url);
    }


    public class Api extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = Api.class.getSimpleName();
        String forecastJsonStr = null;

        @Override
        protected Void doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                String targetUrl =  params[0];
                Log.v("params", params[0]);
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
                    return null;
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
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity.this.showResults(forecastJsonStr);
        }
    }

    private void showResults(String s) {
        tv.setText(s);
//        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
