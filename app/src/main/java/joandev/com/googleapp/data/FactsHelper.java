package joandev.com.googleapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joanbarroso on 19/6/15.
 */
public class FactsHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "facts.db";
    private static final int DATABASE_VERSION = 1;



    public FactsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_FACTS_TABLE = "CREATE TABLE " + FactsContract.FactEntry.TABLE_NAME + " (" +
                FactsContract.FactEntry._ID + " INTEGER PRIMARY KEY," +
                FactsContract.FactEntry.COLUMN_TYPE + " TEXT  NOT NULL, " +
                FactsContract.FactEntry.COLUMN_TEXT + " TEXT  NOT NULL, " +
                FactsContract.FactEntry.COLUMN_YEAR + " TEXT  NOT NULL, " +
                FactsContract.FactEntry.COLUMN_DAY + " INTEGER, " +
                FactsContract.FactEntry.COLUMN_MONTH + " INTEGER, " +
                FactsContract.FactEntry.COLUMN_MATH_NUMBER + " INTEGER, " +
                FactsContract.FactEntry.COLUMN_TRIVIA_NUMBER + " INTEGER " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FactsContract.FactEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
