package com.example.ejercicio3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ejercicio3.model.Member;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Database version
    private static final int DATABASE_VERSION =3;

    //Name
    private static final String DATABASE_NAME="ejercicio3_db";

    //Create Table

    private static final String CREATE_TABLE_MEMBERS =
            "CREATE TABLE MEMBERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, MATRICULA TEXT, DIRECTION TEXT, EXPRESION TEXT, IMAGE TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create MEMBERS TABLE
        db.execSQL(CREATE_TABLE_MEMBERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older member table if exists
        db.execSQL("DROP TABLE IF EXISTS MEMBERS");

        // Create member table again
        onCreate(db);

    }


    public List<Member> getAll(){
        List<Member> members = new ArrayList<>();
        String select = "SELECT * FROM MEMBERS";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select,null);

        if(cursor.moveToFirst()){
            do{
                Member member = new Member(cursor.getString(cursor.getColumnIndex("NAME")),
                        cursor.getString(cursor.getColumnIndex("MATRICULA")),cursor.getString(cursor.getColumnIndex("DIRECTION")),
                        cursor.getString(cursor.getColumnIndex("EXPRESION")),cursor.getString(cursor.getColumnIndex("IMAGE")));
                members.add(member);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return members;
    }

    public boolean deleteOne(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("MEMBERS","NAME = ?", new String[]{name}) > 0;

    }

    public boolean deleteAll(){
        boolean result;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("MEMBERS",null,null) > 0;

    }

    public long insertOne(Member member){
        SQLiteDatabase db = this.getWritableDatabase();
        long id;
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",member.getName());
        contentValues.put("MATRICULA", member.getMatricula());
        contentValues.put("DIRECTION",member.getAddress());
        contentValues.put("EXPRESION",member.getExpresion());
        contentValues.put("IMAGE",member.getImage());

        id = db.insert("MEMBERS",null,contentValues);
        db.close();
        return id;

    }

    public int Update(Member member){
        int result;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",member.getName());
        contentValues.put("MATRICULA", member.getMatricula());
        contentValues.put("DIRECTION",member.getAddress());
        contentValues.put("EXPRESION",member.getExpresion());
        contentValues.put("IMAGE",member.getImage());
        result = db.update("MEMBERS",contentValues,"NAME = ?", new String[]{member.getName()});
        db.close();
        return result;
    }

}
