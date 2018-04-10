package scratch;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cmsc127project.mybudgetplanner.Budget;

public class DBHelper {
    private static final String TAG = "DBHelper";

    // DB fields for ActivityUnit table
    public static final String ACTIVITY_ROWID = "_id";
    public static final String ACTIVITY_BUDGET_UNIT_ID = "budget_id"; //this connects an activity unit to the budget where it belongs
    public static final String ACTIVITY_NAME = "activity_name";
    public static final String ACTIVITY_TYPE = "activity_type"; //1=spending, 2=adding
    public static final String ACTIVITY_DATECREATED = "date_created"; //TODO format is mm/dd/yy
    public static final String ACTIVITY_DATEMODIFIED = "date_modified"; //null when not modified
    public static final String ACTIVITY_MONEYSPENT = "money_spent"; //TODO there is a limit to the money, else display money like 2.2 B, 1.32 M
    public static final String ACTIVITY_MONEYADDED = "money_added";
    public static final String ACTIVITY_NOTE = "note"; //null when no note added
    public static final String ACTIVITY_PHOTOID = "photo_id"; //null when no photo is added


    public static final int ACTIVITY_COL_ROWID = 0;
    public static final int ACTIVITY_COL_BUDGET_UNIT_ID = 1;
    public static final int ACTIVITY_COL_NAME = 2;
    public static final int ACTIVITY_COL_TYPE = 3;
    public static final int ACTIVITY_COL_DATECREATED = 4;
    public static final int ACTIVITY_COL_DATEMODIFIED = 5;
    public static final int ACTIVITY_COL_MONEYSPENT = 6;
    public static final int ACTIVITY_COL_MONEYADDED = 7;
    public static final int ACTIVITY_COL_NOTE = 8;
    public static final int ACTIVITY_COL_PHOTOID = 9;

    public static final String[] ACTIVITY_ALL_KEYS = new String[] {
                ACTIVITY_ROWID, ACTIVITY_BUDGET_UNIT_ID, ACTIVITY_NAME, ACTIVITY_TYPE, ACTIVITY_DATECREATED,
                ACTIVITY_DATEMODIFIED, ACTIVITY_MONEYSPENT, ACTIVITY_MONEYADDED, ACTIVITY_NOTE, ACTIVITY_PHOTOID
            };

    // DB fields for Budget table
    public static final String BUDGET_ROWID = ACTIVITY_ROWID;
    public static final String BUDGET_NAME = "budget_name";
    public static final String BUDGET_DATECREATED = ACTIVITY_DATECREATED; //TODO format is mm/dd/yy
    public static final String BUDGET_DATEMODIFIED = ACTIVITY_DATEMODIFIED; //null when not modified
    public static final String BUDGET_DURATION = "duration"; //TODO could be break to start and end
    public static final String BUDGET_CURRENT = "current"; //TODO there is a limit to the money, else display money like 2.2 B, 1.32 M
    public static final String BUDGET_ORIGINAL = "original";
    public static final String BUDGET_ADDED = "added"; //null when no note added
    public static final String BUDGET_SPENT = "spent"; //null when no photo is added
    public static final String BUDGET_NOTE = ACTIVITY_NOTE; //null when no note added

    public static final int BUDGET_COL_ROWID = 0;
    public static final int BUDGET_COL_NAME = 1;
    public static final int BUDGET_COL_DATECREATED = 2;
    public static final int BUDGET_COL_DATEMODIFIED = 3;
    public static final int BUDGET_COL_DURATION = 4;
    public static final int BUDGET_COL_CURRENT = 5;
    public static final int BUDGET_COL_ORIGINAL = 6;
    public static final int BUDGET_COL_ADDED = 7;
    public static final int BUDGET_COL_SPENT = 8;
    public static final int BUDGET_COL_NOTE = 9;

    public static final String[] BUDGET_ALL_KEYS = new String[] {
            BUDGET_ROWID, BUDGET_NAME, BUDGET_DATECREATED, BUDGET_DATEMODIFIED, BUDGET_DURATION,
            BUDGET_CURRENT, BUDGET_ORIGINAL, BUDGET_ADDED, BUDGET_SPENT, BUDGET_NOTE
        };

    // DB info: it's name, and the tables we are using
    public static final String DATABASE_NAME = "myBudgetPlannerDB";
    public static final String TABLE_ACTIVITY = "ActivityUnit";
    public static final String TABLE_BUDGET = "Budget";

    // Track DB version if a new version of our app changes the format
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_ACTIVITY_TABLE_SQL =
            "create table if not exists " + TABLE_ACTIVITY
                    + " (" + ACTIVITY_ROWID + " integer primary key autoincrement, "
                    + ACTIVITY_BUDGET_UNIT_ID + " integer, "
                    + ACTIVITY_NAME + " text not null, "
                    + ACTIVITY_TYPE + " integer not null, "
                    + ACTIVITY_DATECREATED + " text not null, "
                    + ACTIVITY_DATEMODIFIED + " text, "
                    + ACTIVITY_MONEYSPENT + " real, "
                    + ACTIVITY_MONEYADDED + " real, "
                    + ACTIVITY_NOTE + " text, "
                    + ACTIVITY_PHOTOID + " integer autoincrement"
                    + ");";


    private static final String DATABASE_CREATE_BUDGET_TABLE_SQL =
            "create table if not exists " + TABLE_BUDGET
                    + " (" + BUDGET_ROWID + " integer primary key autoincrement, "
                    + BUDGET_NAME + " text not null, "
                    + BUDGET_DATECREATED + " text not null, "
                    + BUDGET_DATEMODIFIED + " text, "
                    + BUDGET_DURATION + " text, "
                    + BUDGET_ORIGINAL + " real not null, "
                    + BUDGET_CURRENT + " real not null, "
                    + BUDGET_ADDED + " real, "
                    + BUDGET_SPENT + " real, "
                    + BUDGET_NOTE + " text"
                    + ");";

    private final Context context; // Context of application who uses us.
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DBHelper(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
        //open the database
        try{
            openDB();
        }catch (SQLException e){
            Log.e(TAG, "SQLException on opening database", e);
            e.printStackTrace();
        }
    }

    //opening database
    public void openDB() throws SQLException{
        db = myDBHelper.getWritableDatabase();
    }

    // closing database
    public void closeDB() {
        db = myDBHelper.getReadableDatabase();
        if (db != null && db.isOpen()){
            db.close();
        }
    }

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    protected static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_ACTIVITY_TABLE_SQL);
            db.execSQL(DATABASE_CREATE_BUDGET_TABLE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);

            // Recreate new database:
            onCreate(db);
        }
    }


    // ------------------------------- "Budget" table methods ----------------------------//
    public long createBudget(String name, String duration, BigDecimal origMoney, String note){
        return insertBudget( new Budget(name, duration, origMoney, note) );
    }

    public long insertBudget(Budget bu) {
        openDB();
        ContentValues values = new ContentValues();
        values.put(BUDGET_NAME, bu.getName());
        values.put(BUDGET_DATECREATED, bu.getDateCreated());
        values.put(BUDGET_DATEMODIFIED, bu.getDateModified());
        values.put(BUDGET_DURATION, bu.getDuration());
        values.put(BUDGET_CURRENT, bu.getCurrentMoney().toString() );
        values.put(BUDGET_ORIGINAL, bu.getOrigMoney().toString() );
        values.put(BUDGET_ADDED, bu.getAddedMoney().toString() );
        values.put(BUDGET_SPENT, bu.getSpentMoney().toString() );
        values.put(BUDGET_NOTE, bu.getNote());

        // insert row
        long budget_rowid = db.insert(TABLE_BUDGET, null, values);
        closeDB();
        return budget_rowid;
    }

    // Get a specific budget, this actually converts a cursor into a budget unit
    public Budget getBudgetByCursor(Cursor c) {
        Budget bu = new Budget();
        if(c != null){
            bu.setRow_id(c.getLong(BUDGET_COL_ROWID));
            bu.setName(c.getString(BUDGET_COL_NAME));
            bu.setDateCreated(c.getString(BUDGET_COL_DATECREATED));
            bu.setDateModified(c.getString(BUDGET_COL_DATEMODIFIED));
            bu.setDuration(c.getString(BUDGET_COL_DURATION));
            bu.setCurrentMoney( new BigDecimal(c.getString(BUDGET_COL_CURRENT)) );
            bu.setOrigMoney( new BigDecimal(c.getString(BUDGET_COL_ORIGINAL)) );
            bu.setAddedMoney( new BigDecimal(c.getString(BUDGET_COL_ADDED)) );
            bu.setSpentMoney( new BigDecimal(c.getString(BUDGET_COL_SPENT)) );
            bu.setNote(c.getString(BUDGET_COL_NOTE));
        }
        return bu;
    }

    // Get a specific budget's row
    public Cursor getBudgetRowByID(long budget_rowid) {
        db = myDBHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BUDGET + " WHERE "
                + BUDGET_ROWID + " = " + budget_rowid;
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }
        closeDB();
        return c;
    }

    public List<Budget> getAllBudgets() {
        List<Budget> budgetUnits = new ArrayList<>();
        db = myDBHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_BUDGET;
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Budget bu = getBudgetByCursor(c);
                budgetUnits.add(bu);
            } while (c.moveToNext());
        }
        closeDB();

        return budgetUnits;
    }

    public int getTotalNumberOfBudgets() {
        String countQuery = "SELECT * FROM " + TABLE_BUDGET;
        db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;

        if(cursor!=null){
            count = cursor.getCount();
            cursor.close();
        }
        closeDB();

        return count;
    }

    public List<Budget> searchBudgetUnitsByKey(String key) {
        List<Budget> budgetUnits = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_BUDGET + " WHERE name LIKE ?",
                new String[]{"%" + key + "%"});

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Budget bu = getBudgetByCursor(c);
                budgetUnits.add(bu);
            } while (c.moveToNext());
        }
        closeDB();
        return budgetUnits;
    }

    /*public int updateBudgetByID(long budget_rowid) {
        db = myDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        Cursor c = getBudgetRowByID(budget_rowid);
        Budget bu = new Budget();
        bu.setDateModified();
        if(c != null){
            bu = getBudgetByCursor(c);
            values.put(BUDGET_NAME, bu.getName());
            values.put(BUDGET_DATECREATED, bu.getDateCreated());
            values.put(BUDGET_DATEMODIFIED, bu.getDateModified());
            values.put(BUDGET_DURATION, bu.getDuration());
            values.put(BUDGET_CURRENT, String.valueOf(bu.getCurrentMoney()) );
            values.put(BUDGET_ORIGINAL, String.valueOf(bu.getOrigMoney()) );
            values.put(BUDGET_ADDED, String.valueOf(bu.getOrigMoney()) );
            values.put(BUDGET_SPENT, String.valueOf(bu.getSpentMoney()) );
            values.put(BUDGET_NOTE, bu.getNote());
        }

        // updating row
        return db.update(TABLE_BUDGET, values, BUDGET_ROWID + " = ?",
                new String[] { String.valueOf( bu.getRow_id() ) });
    }*/

    public long updateBudget(Budget bu) {
        db = myDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        bu.setDateModified();
        values.put(BUDGET_NAME, bu.getName());
        values.put(BUDGET_DATECREATED, bu.getDateCreated());
        values.put(BUDGET_DATEMODIFIED, bu.getDateModified());
        values.put(BUDGET_DURATION, bu.getDuration());
        values.put(BUDGET_CURRENT, String.valueOf(bu.getCurrentMoney()) );
        values.put(BUDGET_ORIGINAL, String.valueOf(bu.getOrigMoney()) );
        values.put(BUDGET_ADDED, String.valueOf(bu.getOrigMoney()) );
        values.put(BUDGET_SPENT, String.valueOf(bu.getSpentMoney()) );
        values.put(BUDGET_NOTE, bu.getNote());


        // updating row
        long row_id = db.update(TABLE_BUDGET, values, BUDGET_ROWID + " = ?",
                new String[] { String.valueOf( bu.getRow_id() ) });
        closeDB();
        return row_id;
    }

    public void deleteBudgetByID(long budget_rowid) {
        db = myDBHelper.getWritableDatabase();
        db.delete(TABLE_BUDGET, BUDGET_ROWID + " = ?",
                new String[] { String.valueOf(budget_rowid) });
        closeDB();
    }

    // ------------------------------- "ActivityUnit" table methods ----------------------------//
    public long createActivity(long budget_unit_id, String name, int type, BigDecimal spentMoney, BigDecimal addedMoney, String note, long photoid){
        return insertActivity( new Activity(budget_unit_id, name, type, spentMoney, addedMoney, note, photoid) );
    }

    public long insertActivity(Activity ac) {
        openDB();
        ContentValues values = new ContentValues();
        values.put(ACTIVITY_BUDGET_UNIT_ID, ac.getBudget_unit_id());
        values.put(ACTIVITY_NAME, ac.getName());
        values.put(ACTIVITY_TYPE, ac.getType());
        values.put(ACTIVITY_DATECREATED, ac.getDateCreated());
        values.put(ACTIVITY_DATEMODIFIED, ac.getDateModified());
        values.put(ACTIVITY_MONEYSPENT, String.valueOf(ac.getSpentMoney()));
        values.put(ACTIVITY_MONEYADDED, String.valueOf(ac.getAddedMoney()));
        values.put(ACTIVITY_NOTE, ac.getNote());
        values.put(ACTIVITY_PHOTOID, ac.getPhotoid());

        // insert row
        long activity_rowid = db.insert(TABLE_ACTIVITY, null, values);
        return activity_rowid;
    }

    // Get a specific activity, this actually converts a cursor into an activity unit
    public Activity getActivityByCursor(Cursor c) {
        Activity ac = new Activity();
        if(c != null){
            ac.setRow_id(c.getLong(ACTIVITY_COL_ROWID));
            ac.setBudget_unit_id(c.getLong(ACTIVITY_COL_BUDGET_UNIT_ID));
            ac.setName(c.getString(ACTIVITY_COL_NAME));
            ac.setType(c.getInt(ACTIVITY_COL_TYPE));
            ac.setDateCreated(c.getString(ACTIVITY_COL_DATECREATED));
            ac.setDateModified(c.getString(ACTIVITY_COL_DATEMODIFIED));
            ac.setAddedMoney( new BigDecimal(c.getString(ACTIVITY_COL_MONEYADDED)) );
            ac.setSpentMoney( new BigDecimal(c.getString(ACTIVITY_COL_MONEYSPENT)) );
            ac.setNote(c.getString(ACTIVITY_COL_NOTE));
            ac.setPhotoid(c.getLong(ACTIVITY_COL_PHOTOID));
        }
        return ac;
    }

    // Get a specific activity's row
    public Cursor getActivityRowByID(long activity_rowid) {
        db = myDBHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE "
                + ACTIVITY_ROWID + " = " + activity_rowid;
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public List<Activity> getAllActivitiesInBudgetUnitID(long activity_budget_unit_id) {
        List<Activity> activityUnits = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE "
                + ACTIVITY_BUDGET_UNIT_ID + " = " + activity_budget_unit_id;

        Log.e(TAG, selectQuery);

        db = myDBHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Activity ac = getActivityByCursor(c);
                activityUnits.add(ac);
            } while (c.moveToNext());
        }

        return activityUnits;
    }

    public List<Activity> getAllActivities() {
        List<Activity> activityUnits = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY;

        Log.e(TAG, selectQuery);

        db = myDBHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Activity ac = getActivityByCursor(c);
                activityUnits.add(ac);
            } while (c.moveToNext());
        }

        return activityUnits;
    }

    public int getTotalNumberOfActivities() {
        String countQuery = "SELECT * FROM " + TABLE_ACTIVITY;
        db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if(cursor != null){
            count = cursor.getCount();
            cursor.close();
        }

        return count;
    }

    public List<Activity> searchActivityUnitsByKey(String key) {
        List<Activity> activities = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ACTIVITY + " WHERE name LIKE ?",
                new String[]{"%" + key + "%"});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Activity ac = getActivityByCursor(c);
                activities.add(ac);
            } while (c.moveToNext());
        }
        return activities;
    }

    /*public int updateActivityByID(long activity_rowid) {
        db = myDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        Cursor c = getBudgetRowByID(activity_rowid);
        ActivityUnit ac = new ActivityUnit();
        if(c != null){
            ac = getActivityByCursor(c);
            values.put(ACTIVITY_BUDGET_UNIT_ID,ac.getBudget_unit_id());
            values.put(ACTIVITY_NAME, ac.getName());
            values.put(ACTIVITY_TYPE, ac.getType());
            values.put(ACTIVITY_DATECREATED, ac.getDateCreated());
            values.put(ACTIVITY_DATEMODIFIED, ac.getDateModified());
            values.put(ACTIVITY_MONEYSPENT, String.valueOf(ac.getSpentMoney()));
            values.put(ACTIVITY_MONEYADDED, String.valueOf(ac.getAddedMoney()));
            values.put(ACTIVITY_NOTE, ac.getNote());
            values.put(ACTIVITY_PHOTOID, ac.getPhotoid());
        }

        // updating row
        return db.update(TABLE_ACTIVITY, values, ACTIVITY_ROWID + " = ?",
                new String[] { String.valueOf( ac.getRow_id() ) });
    }*/

    public int updateActivity(Activity ac) {
        db = myDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        ac.setDateModified();
        values.put(ACTIVITY_BUDGET_UNIT_ID,ac.getBudget_unit_id());
        values.put(ACTIVITY_NAME, ac.getName());
        values.put(ACTIVITY_TYPE, ac.getType());
        values.put(ACTIVITY_DATECREATED, ac.getDateCreated());
        values.put(ACTIVITY_DATEMODIFIED, ac.getDateModified());
        values.put(ACTIVITY_MONEYSPENT, String.valueOf(ac.getSpentMoney()));
        values.put(ACTIVITY_MONEYADDED, String.valueOf(ac.getAddedMoney()));
        values.put(ACTIVITY_NOTE, ac.getNote());
        values.put(ACTIVITY_PHOTOID, ac.getPhotoid());


        // updating row
        return db.update(TABLE_ACTIVITY, values, ACTIVITY_ROWID + " = ?",
                new String[] { String.valueOf( ac.getRow_id() ) });
    }

    public void deleteActivity(long activity_rowid) {
        db = myDBHelper.getWritableDatabase();
        db.delete(TABLE_ACTIVITY, ACTIVITY_ROWID + " = ?",
                new String[] { String.valueOf(activity_rowid) });
    }

}
