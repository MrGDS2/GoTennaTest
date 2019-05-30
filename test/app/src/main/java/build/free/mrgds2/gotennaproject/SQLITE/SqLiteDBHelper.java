package build.free.mrgds2.gotennaproject.SQLITE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqLiteDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MapData.db";
    private static final int DATABASE_VERSION = 1;
    public static final String PLACE_TABLE_NAME = "place";
    public static final String PLACE_COLUMN_ID = "_id";
    public static final String PLACE_COLUMN_NAME = "name";
    public static final String PLACE_COLUMN_DESCRIPTION = "des";
    public static final String PLACE_COLUMN_LAT = "lat";
    public static final String PLACE_COLUMN_LNG = "lng";


    public SqLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PLACE_TABLE_NAME + "(" +
                PLACE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PLACE_COLUMN_NAME + " TEXT, " +
                PLACE_COLUMN_DESCRIPTION + " TEXT," +
                PLACE_COLUMN_LAT + " DOUBLE," +
                PLACE_COLUMN_LNG + "LONG)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public boolean insertPin(String name, String des, String lat ,String lng) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //insert values into table for-each column(s)
        contentValues.put(PLACE_COLUMN_NAME, name);
        contentValues.put(PLACE_COLUMN_DESCRIPTION, des);
        contentValues.put(PLACE_COLUMN_LAT, lat);
//        contentValues.put(PLACE_COLUMN_LAT,"Lat/Lng:\n"+
//                lat+ "\n"+ lng);


       long result= db.insert(PLACE_TABLE_NAME, null, contentValues);
       if(result==-1) //nothing is in the database
           return false;
        db.close();//close resources
        return true;
    }


    public boolean updatePin(Integer id, String name, String description, Double lat, Double lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLACE_COLUMN_NAME, name);
        contentValues.put(PLACE_COLUMN_DESCRIPTION, description);
        contentValues.put(PLACE_COLUMN_LAT, lat);
        contentValues.put(PLACE_COLUMN_LNG, lng);
        db.update(PLACE_COLUMN_NAME, contentValues,
                PLACE_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)}); //if same id update
        return true;
    }


    public Cursor getPin(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + PLACE_TABLE_NAME + " WHERE " +
                PLACE_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllPin() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + PLACE_TABLE_NAME, null);
        return res;
    }

}
