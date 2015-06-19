package joandev.com.googleapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by joanbarroso on 19/6/15.
 */
public class FactsContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "joandev.com.googleapp.data";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_FACT = "fact";

    /* Inner class that defines the table contents of the location table */
    public static final class FactEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FACT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FACT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FACT;

        // Table name
        public static final String TABLE_NAME = "facts";

        // The location setting string is what will be sent to openweathermap
        // as the location query.

        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_MATH_NUMBER = "math_number";
        public static final String COLUMN_TRIVIA_NUMBER = "trivia_number";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_YEAR = "year";

        public static Uri buildFactsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
