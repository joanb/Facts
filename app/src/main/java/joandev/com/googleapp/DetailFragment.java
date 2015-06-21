package joandev.com.googleapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import joandev.com.googleapp.data.FactsContract;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private Uri mUri = FactsContract.FactEntry.CONTENT_URI;
    String type;
    String params[];
    TextView title;
    TextView result;
    TextView lastResult;
    TextView olderResults;
    String text;
    String tit;
    String old = "";
    ScrollView scrollView = null;
    String last = "None yet!";
    SharedPreferences preferences;



    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    public static final String DETAIL_COLUMNS[] = {
            FactsContract.FactEntry.TABLE_NAME + "." + FactsContract.FactEntry._ID,
            FactsContract.FactEntry.COLUMN_DAY,
            FactsContract.FactEntry.COLUMN_MONTH,
            FactsContract.FactEntry.COLUMN_YEAR,
            FactsContract.FactEntry.COLUMN_MATH_NUMBER,
            FactsContract.FactEntry.COLUMN_TRIVIA_NUMBER,
            FactsContract.FactEntry.COLUMN_TEXT
    };


    public static final int COL_ID = 0;
    public static final int COL_TYPE = 1;
    public static final int COL_TEXT = 2;
    public static final int COL_YEAR = 3;
    public static final int COL_DAY = 4;
    public static final int COL_MONTH = 5;
    public static final int COL_MATH = 6;
    public static final int COL_TRIVIA = 7;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        result = (TextView) rootView.findViewById(R.id.resultTV);
        title = (TextView) rootView.findViewById(R.id.titleResultTV);
        if (getResources().getBoolean(R.bool.isTablet)){
            olderResults = (TextView) rootView.findViewById(R.id.olderResultsTV);
            olderResults.setText("Last fact you already found: ");
            preferences = getActivity().getApplicationContext().getSharedPreferences("lastResult", Context.MODE_PRIVATE);
            last = preferences.getString("last", "NONE");
            lastResult = (TextView) rootView.findViewById(R.id.lastResult);
            lastResult.setText(last);

        }

        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text");
            tit = savedInstanceState.getString("tit");
//            old = savedInstanceState.getString("old");
            type = savedInstanceState.getString("type");
            result.setText(text);
            title.setText(tit);
            if (getResources().getBoolean(R.bool.isTablet)){
//                lastResult.setText(last);
            }
        }
        else{
            Bundle arguments = getArguments();
            if (arguments != null) {
                type = arguments.getString("type");
                params = arguments.getStringArray("params");
                title.setText("Your " + type + " fact is");
                if (getResources().getBoolean(R.bool.isTablet)){
//                    lastResult.setText(last);
                }
            }
        }

        if (getResources().getBoolean(R.bool.isTablet)) old = olderResults.getText().toString();
        tit = title.getText().toString();

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String args[] = {type};
        if ( null != mUri ) {
            CursorLoader c = new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
            return  c;

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("Loader", "LOADEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEER");
        if (data != null) {
            // Read weather condition ID from cursor
            while (data.moveToNext()) {
                result.setText(data.getString(COL_TEXT));
                switch(type) {
                    case "Date":
                        result.setText("In " + data.getString(COL_DAY)+ "/" + data.getString(COL_MONTH) + "/"+ data.getString(COL_YEAR)+  " ," + data.getString(COL_TEXT));
                        break;
                    case "Year":
                        result.setText("In " + data.getString(COL_YEAR) + " " + data.getString(COL_TEXT));
                    break;
                    case "Trivia":
                        result.setText(data.getString(COL_TRIVIA) + " is " + data.getString(COL_TEXT));

                    break;
                    case "Math":
                        result.setText(data.getString(COL_MATH) + " is " + data.getString(COL_TEXT));
                    break;
                }

                text = result.getText().toString();
            }
            if (getResources().getBoolean(R.bool.isTablet) && data.moveToPrevious() && data.moveToPrevious()){
                String s = data.getString(COL_TYPE);
                switch(s) {
                    case "Date":
                        last = "In " + data.getString(COL_DAY)+ "/" + data.getString(COL_MONTH) + "/"+ data.getString(COL_YEAR)+  " ," + data.getString(COL_TEXT);
                        break;
                    case "Year":
                        last = "In " + data.getString(COL_YEAR) + " " + data.getString(COL_TEXT);
                        break;
                    case "Trivia":
                        last = data.getString(COL_TRIVIA) + " is " + data.getString(COL_TEXT);

                        break;
                    case "Math":
                        last = data.getString(COL_MATH) + " is " + data.getString(COL_TEXT);
                        break;
                }


                if (getResources().getBoolean(R.bool.isTablet)) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("last", last);
                    editor.apply();
                    lastResult.setText(last);
                }
            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", text);
        outState.putString("tit", tit);
        outState.putString("old", old);
        outState.putString("type",type);
        outState.putString("last",last);
    }

    @Override
    public void onDestroy() {

        Log.v("Destroy", "destroying");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.v("Destroy", "detaching");
        super.onDetach();
    }
}
