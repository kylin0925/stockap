package ap.ky.stockapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kylin25 on 2016/5/1.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbCount.db";
    public static final int VERSION = 1;
    private static SQLiteDatabase database;

    String CREATE_TABLE = "CREATE TABLE stock (" +
            "ID INTEGER," +
            "DATE TEXT," +
            "company TEXT," +
            "compnum TEXT," +
            "stocks INTEGER,"+
            "stocksprice INTEGER,"+
            "total INTEGER,"+
            "stocktype INTEGER," +
            "PRIMARY KEY(ID)" +
            ")";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null ) {
            DBHelper d =new DBHelper(context, DATABASE_NAME,null, VERSION);
            database = d.getWritableDatabase();
        }

        return database;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
