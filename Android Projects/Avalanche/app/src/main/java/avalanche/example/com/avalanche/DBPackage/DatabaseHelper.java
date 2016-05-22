package avalanche.example.com.avalanche.DBPackage;

/**
 * Created by Mahe on 4/10/2016.
 */
import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

/**
 * Created by Mitchell on 4/9/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "ScheduleData.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.SQL_CREATE_SCHEDULEDATA);
        db.execSQL(DatabaseContract.SQL_CREATE_PRESCHEDULEDATA);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
