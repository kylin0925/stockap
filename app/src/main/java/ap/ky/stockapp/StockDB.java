package ap.ky.stockapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kylin25 on 2016/8/24.
 */
public class StockDB {

    private static final String TAG = "StockDB";
    public static int TYPE_BUY = 0;
    public static int TYPE_SELL = 1;
    public static int TYPE_DIVIEND = 2;
    Context context;
    static  String STOCKTBL = "stock";
//    String CREATE_TABLE = "CREATE TABLE stock (" +
//            "ID INTEGER," +
//            "DATE TEXT," +
//            "company TEXT," +
//            "compnum TEXT," +
//            "stocks INTEGER,"+
//            "total INTEGER,"+
//            "stocktype INTEGER," +
//            "type INTEGER," +
//            "PRIMARY KEY(ID)" +
//            ")";
//    public class DBStruct implements Serializable {
//        private static final long serialVersionUID = 1L;
//        public int recid;
//        public String date;
//        public String company;
//        public String compnum;
//        public int stocks;
//        public float stockprice;
//        public int totalprice ;
//        public int type;
//        public DBStruct(){
//
//        }
//
//    }
    ArrayList<DBStruct> datalist = new ArrayList<DBStruct>();
    ArrayList<String> namelist = new ArrayList<String>();
    ArrayList<String> numberlist = new ArrayList<String>();
    public StockDB(Context context) {
        this.context = context;
    }
    void addData(String date,String company,String compnum,int stocks,float stockprice,int totalprice ,int type){
        SQLiteDatabase  db = DBHelper.getDatabase(context);
        ContentValues cv = new ContentValues();

        cv.put("date", date);
        cv.put("company", company);
        cv.put("compnum", compnum);
        cv.put("stocks", stocks);
        cv.put("stocksprice", stockprice);
        cv.put("total", totalprice);
        cv.put("stocktype", type);

        db.insert("stock", null, cv);
    }

    ArrayList<DBStruct> queryData(){
        SQLiteDatabase  db = DBHelper.getDatabase(context);
        ContentValues cv = new ContentValues();

        Cursor c = db.query("stock", null, null, null, null, null, null);
        datalist.clear();
        while (c.moveToNext()){
            DBStruct item = new DBStruct();
            item.recid = c.getInt(0);
            item.date = c.getString(1);
            item.company = c.getString(2);
            item.compnum = c.getString(3);
            item.stocks = c.getInt(4);
            item.stockprice = c.getInt(5);
            item.totalprice = c.getInt(6);
            item.type = c.getInt(7);
            datalist.add(item);
            Log.e(TAG,"" + c.getString(1) + " " + c.getString(2)  + " " +
                    c.getString(3) + " " +c.getInt(4) + " " + c.getInt(5) + " " + c.getInt(6) + " " + c.getInt(7));

        }
        return datalist;
    }

    int queryBenfit(){
        int benfit=0;
        SQLiteDatabase  db = DBHelper.getDatabase(context);
        ContentValues cv = new ContentValues();

        Cursor c = db.query("stock", null, null, null, null, null, null);

        while (c.moveToNext()){
            benfit += c.getInt(6);
        }
        return benfit;
    }

    ArrayList<String> queryName(){
        int benfit=0;
        SQLiteDatabase  db = DBHelper.getDatabase(context);
        ContentValues cv = new ContentValues();

        Cursor c = db.query(true,"stock",new String[]{"company"},null,null,null,null,null,null);
        namelist.clear();
        while (c.moveToNext()){
            namelist.add(c.getString(0));
        }

        return namelist;
    }
    ArrayList<String> queryCompNum(){
        int benfit=0;
        SQLiteDatabase  db = DBHelper.getDatabase(context);
        ContentValues cv = new ContentValues();

        Cursor c = db.query(true,"stock",new String[]{"compnum"},null,null,null,null,null,null);
        numberlist.clear();
        while (c.moveToNext()){
            numberlist.add(c.getString(0));
            Log.e(TAG,c.getString(0));
        }

        return numberlist;
    }
    void updateData(String date,String company,String compnum,int stocks,float stockprice,
                    int totalprice ,int type,int updateid){
        SQLiteDatabase  db = DBHelper.getDatabase(context);
        ContentValues cv = new ContentValues();

        cv.put("date", date);
        cv.put("company", company);
        cv.put("compnum", compnum);
        cv.put("stocks", stocks);
        cv.put("stocksprice", stockprice);
        cv.put("total", totalprice);
        cv.put("stocktype", type);

        String[] whereargs = new String[]{String.valueOf(updateid)};
        db.update(STOCKTBL,cv,"id=?", whereargs);

    }
    void delData(int recid){
        SQLiteDatabase  db = DBHelper.getDatabase(context);
        String[] whereargs = new String[]{String.valueOf(recid)};
        db.delete(STOCKTBL,"id=?", whereargs);

    }
}
