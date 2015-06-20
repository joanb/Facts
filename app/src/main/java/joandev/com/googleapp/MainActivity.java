package joandev.com.googleapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtras(mBundle);
        startActivity(i);
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



    private void showResults(String s) {
        tv.setText(s);
//        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
