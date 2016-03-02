package com.example.johnw.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


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
    //SQL ver 2.0 code
    public boolean updateAfterAdapter(ArrayAdapter itemAdapter,ArrayList<String> items){
        SQLiteDatabase db = getWritableDatabase();
        String[] itemls = new String[items.size()];
        itemls = items.toArray(itemls);
        db.execSQL("delete from "+"todo");
        for(int i=0;i<itemAdapter.getCount();i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("id",i);
            contentValues.put("name",itemls[i]);
            contentValues.put("date","");
            db.insert("todo",null,contentValues);
        }
        return true;
    }
    public boolean refreshTaskList(ArrayAdapter itemAdapter) {
        while(true){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
        Cursor Data = db.rawQuery("select * from todo", null);

        if (Data.isClosed()){
            Data.close();
            return true;
        }
        itemAdapter.clear();
        itemAdapter.notifyDataSetChanged();
        for (Data.moveToFirst(); !Data.isAfterLast(); Data.moveToNext()) {
            itemAdapter.add(String.valueOf(Data.getString(1)));
        }
        Data.close();
        break;
        }catch(Exception e){db.execSQL("create table todo " +
                "(id integer,name text,date text)");
        }}
        return true;

    }
    //SQL ver 1.0 code
   /* public Integer deleteTask(ArrayAdapter itemAdapter,int pos){
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
    */
     /* public boolean insertTask(ArrayAdapter<String> itemAdapter,String name,String date){
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
        contentValues.put("id", itemAdapter.getCount()+1);
        contentValues.put("name",name);
        contentValues.put("date", date);
        db.insert("todo", null, contentValues);
        return true;
    }*/
}