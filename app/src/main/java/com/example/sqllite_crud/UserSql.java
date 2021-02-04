package com.example.sqllite_crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSql extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "User";//資料整個庫的名稱
    private static final String TAG = "UserSql";
    public static int version = 1;//版本
    public static String UserName = "UserName";//資料庫名稱
    public UserSql(@Nullable Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // SQL 語法
        String sql = "CREATE TABLE IF NOT EXISTS "+UserName+"("+
                " id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "FirstName TEXT,LastName TEXT,Time TEXT);";
        //使他建立
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {//更新版本用的
        // 把資料表刪除並重新建立
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+UserName);
        onCreate(sqLiteDatabase);
    }

    public long InsertData(String firstName,String lastName,String time){
        //ContentValues以鍵值對的形式存放資料
        ContentValues cv = new ContentValues();
        cv.put("FirstName",firstName); //這邊的字串要跟AddItem那邊的Key一樣
        cv.put("LastName",lastName);
        cv.put("Time",time);
        long date = getWritableDatabase().insert(UserName,null,cv);
        return date;
    }
    public long updateData(String firstName,String lastName,String time,int position) {//更新資料用的
        ContentValues cv = new ContentValues();
        cv.put("FirstName",firstName); //這邊的字串要跟AddItem那邊的Key一樣
        cv.put("LastName",lastName);
        cv.put("Time",time);
        long update = getWritableDatabase().update(UserName,cv,"id="+position,null);
        return update;
    }
    public boolean deleteData(String firstName){
        long num = getWritableDatabase().delete(UserName,"FirstName='"+firstName+"'",null);
        //注意這邊字串裡面會有 ‘  '這個'要記得，所以
        if(num!=0)
            return false;
        else
            return true;
    }
    public ArrayList<HashMap<String,String>> getData(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();

        String sql = " SELECT * FROM "+UserName+" ORDER BY ID ASC ";
        //從UserName裡面取資料，並排序，由小到大。
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        //getWritable就是可以讀取 也可以寫入
        int num = c.getCount();
        if (num!=0){
            c.moveToFirst();
        }
        for (int i=0;i<num;i++){
            HashMap<String,String> map = new HashMap<>();//<key,value>
            map.put("ID",c.getInt(0)+"");
            map.put("FirstName",c.getString(1));
            map.put("LastName",c.getString(2));
            map.put("Time",c.getString(3));
            list.add(map);
            c.moveToNext();
        }
        return list;
    }

}
