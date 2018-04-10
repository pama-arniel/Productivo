package scratch;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Search for "TODO", and make the appropriate changes.
public class AUDBScratch {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "AUDBScratch";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    // TODO: Setup your fields here:
    public static final String KEY_NAMEOFACTIVITY = "activityname";
    public static final String KEY_TYPEOFACTIVITY = "activitytype";
    public static final String KEY_DATECREATED = "datecreated";
    public static final String KEY_DATEMODIFIED = "datemodified";
    public static final String KEY_MONEYSPENT = "moneyspent";
    public static final String KEY_MONEYADDED = "moneyadded";
    public static final String KEY_NOTE = "note";
    public static final String KEY_PHOTOID = "photoid";

    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
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

			/*
			 * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_NAMEOFACTIVITY + " text not null"
                    + KEY_TYPEOFACTIVITY + " integer not null"
                    + KEY_DATECREATED + " text not null"
                    + KEY_DATEMODIFIED + " text"
                    + KEY_MONEYSPENT + " text"
                    + KEY_MONEYADDED + " text"
                    + KEY_NOTE + " text"
                    + KEY_PHOTOID + " integer autoincrement"
//                    + KEY_NAME + " text not null, "
//                    + KEY_STUDENTNUM + " integer not null, "
//                    + KEY_FAVCOLOUR + " string not null"

                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public AUDBScratch(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public AUDBScratch open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
//    public long insertRow(String name, int studentNum, String favColour) {
//		/*
//		 * CHANGE 3:
//		 */
//        // TODO: Update data in the row with new fields.
//        // TODO: Also change the function's arguments to be what you need!
//        // Create row's data:
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_NAME, name);
//        initialValues.put(KEY_STUDENTNUM, studentNum);
//        initialValues.put(KEY_FAVCOLOUR, favColour);
//
//        // Insert it into the database.
//        return db.insert(DATABASE_TABLE, null, initialValues);
//    }

    public long insertRow(String label, int activityType, String dateCreated, String dateModified, String moneyAdded, String moneySpent, String note) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAMEOFACTIVITY, label);
        initialValues.put(KEY_TYPEOFACTIVITY, activityType);
        initialValues.put(KEY_DATECREATED, dateCreated);
        initialValues.put(KEY_DATEMODIFIED, dateModified);
        initialValues.put(KEY_MONEYADDED, moneyAdded);
        initialValues.put(KEY_MONEYSPENT, moneySpent);
        initialValues.put(KEY_NOTE, note);
//        initialValues.put(KEY_STUDENTNUM, studentNum);
//        initialValues.put(KEY_FAVCOLOUR, favColour);
//        initialValues.put(KEY_NAME, name);
//        initialValues.put(KEY_STUDENTNUM, studentNum);
//        initialValues.put(KEY_FAVCOLOUR, favColour);

        // Insert it into the database.
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
        // || is the concatenation operation in SQLite
        Cursor c = db.rawQuery("SELECT * FROM activityUnitsDB WHERE name LIKE ?",
                new String[]{"%" + key + "%"});
        return c;
    }

//    public String Search(String n)
//    {
//        String info = "";
//        Cursor cu = HRdb.rawQuery("SELECT * FROM HRinfotbl WHERE " +
//    "lname LIKE '%' || ? || '%'  OR fname LIKE '%' || ? || '%'",
//            new String[] { n, n });
//        int count = cu.getCount();
//        cu.moveToFirst();
//        for (Integer j = 0; j < count; j++)
//        {
//            info = info +
//                    "Surname : "+cu.getString(cu.getColumnIndex("lname"))+"\n"+
//                    "Name : "+cu.getString(cu.getColumnIndex("fname"))+"\n"+
//                    "Email : "+cu.getString(cu.getColumnIndex("email"))+"\n"+
//                    "Contact : "+cu.getString(cu.getColumnIndex("contact"))+"\n\n";
//            cu.moveToNext() ;
//        }
//        HRdb.close();
//        return info;
//    }

//    // Change an existing row to be equal to new data.
//    public boolean updateRow(long rowId, String name, int studentNum, String favColour) {
//        String where = KEY_ROWID + "=" + rowId;
//
//		/*
//		 * CHANGE 4:
//		 */
//        // TODO: Update data in the row with new fields.
//        // TODO: Also change the function's arguments to be what you need!
//        // Create row's data:
//        ContentValues newValues = new ContentValues();
//        newValues.put(KEY_NAME, name);
//        newValues.put(KEY_STUDENTNUM, studentNum);
//        newValues.put(KEY_FAVCOLOUR, favColour);
//
//        // Insert it into the database.
//        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
//    }

    public boolean updateRow(String label, long rowId, int activityType, String dateCreated, String dateModified, String moneyAdded, String moneySpent, String note) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:

        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAMEOFACTIVITY, label);
        newValues.put(KEY_TYPEOFACTIVITY, activityType);
        newValues.put(KEY_DATECREATED, dateCreated);
        newValues.put(KEY_DATEMODIFIED, dateModified);
        newValues.put(KEY_MONEYADDED, moneyAdded);
        newValues.put(KEY_MONEYSPENT, moneySpent);
        newValues.put(KEY_NOTE, note);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }



    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

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
