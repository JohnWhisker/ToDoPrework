package com.example.johnw.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;


public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TODO.db";
    public static final String TODO_COLUMN_ID = "id";
    public static final String TODO_COLUMN_TASKNAME ="name";
    public static final String TODO_COLUMN_TASKDATE ="date";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table todo " +
                        "(id integer,name text,date text)"
        );
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST todo");
        onCreate(db);
    }
    public boolean insertTask(String name,String date){
        int id;
        SQLiteDatabase dbr = this.getReadableDatabase();
        Cursor Data = dbr.rawQuery("select * from todo", null);
        Data.moveToLast();
        if(Data.getString(0)== null||"".equals(Data.getString(0))){
            id = 0;
        }
        else {

            id =  Integer.parseInt(Data.getString(0));
        }
        Data.close();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id+1);
        contentValues.put("name",name);
        contentValues.put("date", date);
        db.insert("todo", null, contentValues);
        return true;
    }
    public boolean refreshTaskList(ArrayAdapter itemAdapter) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor Data = db.rawQuery("select * from todo", null);
        if (Data.isClosed()){
            Data.close();
            return true;
        }
        itemAdapter.clear();
        itemAdapter.notifyDataSetChanged();
        for (Data.moveToFirst(); !Data.isAfterLast(); Data.moveToNext()) {
            itemAdapter.add(Data.getString(1));
        }
        Data.close();
        return true;
    }
    public Integer deleteTask(ArrayAdapter itemAdapter,int pos){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("todo","id = ? ",new String[] {Integer.toString(pos)});
    }
    public boolean modifiedTaskList(ArrayAdapter itemAdapter,int possition,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        db.update("todo",contentValues,"id = ?",new String[]{Integer.toString(possition)});
        return true;
    }
}