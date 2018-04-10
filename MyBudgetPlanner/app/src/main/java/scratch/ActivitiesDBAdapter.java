package scratch;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ActivitiesDBAdapter {
    // For logging:
    private static final String TAG = "AcitivitiesDBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAMEOFACTIVITY = "activityname";
    public static final String KEY_TYPEOFACTIVITY = "activitytype"; //1=spending, 2=adding
    public static final String KEY_DATECREATED = "datecreated"; //TODO format is mm/dd/yy
    public static final String KEY_DATEMODIFIED = "datemodified"; //null when not modified
    public static final String KEY_MONEYSPENT = "moneyspent"; //TODO there is a limit to the money, else display money like 2.2 B, 1.32 M
    public static final String KEY_MONEYADDED = "moneyadded";
    public static final String KEY_NOTE = "note"; //null when no note added
    public static final String KEY_PHOTOID = "photoid"; //null when no photo is added

    public static final int COL_ROWID = 0;
    public static final int COL_TYPEOFACTIVITY = 1;
    public static final int COL_DATECREATED = 2;
    public static final int COL_DATEMODIFIED = 3;
    public static final int COL_MONEYSPENT = 4;
    public static final int COL_MONEYADDED = 5;
    public static final int COL_DESCRIPTION = 6;
    public static final int COL_PHOTOID = 7;


    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAMEOFACTIVITY, KEY_TYPEOFACTIVITY, KEY_DATECREATED, KEY_DATEMODIFIED, KEY_MONEYSPENT, KEY_MONEYADDED, KEY_NOTE, KEY_PHOTOID};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "activityUnitsDB";
    public static final String DATABASE_TABLE = "activityUnitsTable";

    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_NAMEOFACTIVITY + " text not null,"
                    + KEY_TYPEOFACTIVITY + " integer not null,"
                    + KEY_DATECREATED + " text not null,"
                    + KEY_DATEMODIFIED + " text,"
                    + KEY_MONEYSPENT + " text,"
                    + KEY_MONEYADDED + " text,"
                    + KEY_NOTE + " text,"
                    + KEY_PHOTOID + " integer autoincrement"
                    + ");";


    private final Context context; // Context of application who uses us.
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public ActivitiesDBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public ActivitiesDBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    public long insertRow(String label, int activityType, String dateCreated, String dateModified, String moneyAdded, String moneySpent, String note) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAMEOFACTIVITY, label);
        initialValues.put(KEY_TYPEOFACTIVITY, activityType);
        initialValues.put(KEY_DATECREATED, dateCreated);
        initialValues.put(KEY_DATEMODIFIED, dateModified);
        initialValues.put(KEY_MONEYADDED, moneyAdded);
        initialValues.put(KEY_MONEYSPENT, moneySpent);
        initialValues.put(KEY_NOTE, note);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor search(String key) {
        Cursor c = db.rawQuery("SELECT * FROM activityUnitsDB WHERE name LIKE ?",
                new String[]{"%" + key + "%"});
        return c;
    }

    public boolean updateRow(String label, long rowId, int activityType, String dateCreated, String dateModified, String moneyAdded, String moneySpent, String note) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAMEOFACTIVITY, label);
        newValues.put(KEY_TYPEOFACTIVITY, activityType);
        newValues.put(KEY_DATECREATED, dateCreated);
        newValues.put(KEY_DATEMODIFIED, dateModified);
        newValues.put(KEY_MONEYADDED, moneyAdded);
        newValues.put(KEY_MONEYSPENT, moneySpent);
        newValues.put(KEY_NOTE, note);
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
