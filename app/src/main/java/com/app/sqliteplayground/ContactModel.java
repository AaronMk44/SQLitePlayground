package com.app.sqliteplayground;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContactModel extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "mydb";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "contacts";
    private static final String[] COLUMNS = {"id", "name", "phone_number"};

    public ContactModel(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DDL = "create table if not exists "+ TABLE_NAME + " (" +
                "id integer primary key," +
                "name text," +
                "phone_number text" +
                ")";

        db.execSQL(DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    void addContact(Contact c){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", c.getName());
        values.put("phone_number", c.getPhoneNumber());

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    Contact getContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(
                TABLE_NAME,
                COLUMNS,
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if(c != null) c.moveToFirst();

        Contact o = new Contact(
                Integer.parseInt(c.getString(0)),
                c.getString(1),
                c.getString(2));

        return o;
    }

    // code to get all contacts in a list view
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("phone_number", contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_NAME, values, "id" + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
