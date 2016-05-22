package avalanche.example.com.avalanche.DBPackage;

import android.provider.BaseColumns;


        import android.database.sqlite.*;
        import android.provider.BaseColumns;

/**
 *This class defines the schema of the ScheduleData table
 */
public final class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "scheduleData";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_WEEK = "week";
        public static final String COLUMN_NAME_HOME = "home";
        public static final String COLUMN_NAME_HOMESCORE = "homeScore";
        public static final String COLUMN_NAME_AWAY = "away";
        public static final String COLUMN_NAME_AWAYSCORE = "awayScore";
        public static final String COLUMN_NAME_RESULT = "result";



    }

    public static final String INT_TYPE = " INTEGER";
    public static final String VAR_TYPE = " VARCHAR(255)";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_SCHEDULEDATA =
            "CREATE TABLE " + DataEntry.TABLE_NAME + " (" +
                    DataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DataEntry.COLUMN_NAME_YEAR + INT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_WEEK + INT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_HOME + VAR_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_HOMESCORE + INT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_AWAY + VAR_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_AWAYSCORE + INT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_RESULT + VAR_TYPE +
                    ");";
    public static final String SQL_CREATE_PRESCHEDULEDATA =
            "CREATE TABLE Pre" + DataEntry.TABLE_NAME + " (" +
                    DataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DataEntry.COLUMN_NAME_YEAR + INT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_WEEK + INT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_HOME + VAR_TYPE + COMMA_SEP  +
                    DataEntry.COLUMN_NAME_HOMESCORE + INT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_AWAY + VAR_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_AWAYSCORE + INT_TYPE  + COMMA_SEP +
                    DataEntry.COLUMN_NAME_RESULT + VAR_TYPE +
                    ");";


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME + ";" +
                    "DROP TABLE IF EXISTS Pre" + DataEntry.TABLE_NAME + ";";

}