package joandev.com.googleapp;


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
import android.widget.Toast;

import joandev.com.googleapp.data.FactsContract;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private Uri mUri = FactsContract.FactEntry.CONTENT_URI;
    static final String DETAIL_URI = "URI";


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

//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
//        }

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v("onactivitycreated", "Creamos, init loader. mUri : " + mUri);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v("cursorloader", "enntro");
        if ( null != mUri ) {
            Log.v("cursorloader", "enntro2");

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            CursorLoader c = new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
            Log.v("cursorloader",c.toString());
            return  c;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            // Read weather condition ID from cursor
            String text = data.getString(1);
            Toast.makeText(getActivity(),text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
