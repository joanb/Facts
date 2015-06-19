package joandev.com.googleapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by joanbarroso on 19/6/15.
 */
public class FactsProvider extends ContentProvider {

    FactsHelper mOpenHelper;
    static final int FACT = 100;


    @Override
    public boolean onCreate() {
        mOpenHelper = new FactsHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        Cursor retCursor;
        retCursor = mOpenHelper.getReadableDatabase().query(
                FactsContract.FactEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                s1
        );

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        return FactsContract.FactEntry.CONTENT_TYPE;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long _id = db.insert(FactsContract.FactEntry.TABLE_NAME, null, contentValues);
        if ( _id > 0 )
            returnUri = FactsContract.FactEntry.buildFactsUri(_id);
        else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FactsContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, FactsContract.PATH_FACT + "/*", FACT);
        return matcher;
    }
}
