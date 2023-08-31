package com.example.lms.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "LMSDB.db";

    public DBHelper(Context context) {
        super(context, "LMSDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {


        MyDB.execSQL("CREATE TABLE Manager(managername TEXT  ,  email TEXT primary key , password TEXT)");
        MyDB.execSQL("CREATE TABLE Employee(empID TEXT ,  email TEXT primary key , password TEXT)");

        MyDB.execSQL("INSERT INTO Manager (managername,email,password)VALUES" +
                "('manager','manager@gmail.com','manager123')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists Employee ");
        MyDB.execSQL("drop Table if exists Manager ");
    }

    public Boolean managervalidation(String usname, String paswd){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String query = "SELECT email, password FROM Manager WHERE email = ? AND password = ?";
        Cursor cursor1 = MyDB.rawQuery(query, new String[]{usname, paswd});
        boolean result = cursor1.moveToFirst();
        cursor1.close();
        return result;
    }
    public Boolean insertData(String empID, String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("empID", empID);
        contentValues.put("email", email);
        contentValues.put("password", password);

        long result = MyDB.insert("Employee", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    //get user
    public Boolean checkUser(String email){
        SQLiteDatabase MyDB =this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from Employee where email=?",new String[]{email});
        if(cursor.getCount()>0){
            return  true;
        }
        else {
            return false;
        }
    }
    // get all data from register table
    public Cursor getAllData() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor result = MyDB.rawQuery("select * from Employee", null);
        return result;
    }
    // check user passwd
    public Boolean checkUserPasswd(String username, String passwd){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String query = "SELECT email, password FROM Employee WHERE email = ? AND password = ?";
        Cursor cursor = MyDB.rawQuery(query, new String[]{username, passwd});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }
    public boolean getUser(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String query = "SELECT empID FROM Employee WHERE email = ? ";
        Cursor cursor = MyDB.rawQuery(query, new String[]{email});
        boolean result = cursor.moveToFirst();
        return result;
    }
    // update the register table
    public boolean updateData(String empID, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("empID", empID);
        contentValues.put("email", email);
        contentValues.put("password", password);


        db.update("Employee", contentValues, "email = ? ", new String[] {email});
        return  true;
    }
    // delete the data form the register table
    public Integer deleteData(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete("Employee", "email = ? ", new String[]{email});
    }

    // code for the data retrieve

    public Cursor getLoginDetails(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor result = MyDB.rawQuery("SELECT email,password FROM Employee WHERE email = ?", new String[]{email});

        return result;
    }

    // check if the email is already exits
    public boolean checkEmailAllReadyReg(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Employee where email = ?", new String[] {email});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

}

