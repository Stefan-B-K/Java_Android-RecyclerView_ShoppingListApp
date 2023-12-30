package com.istef.shoppinglist.data;

import static com.istef.shoppinglist.util.Config.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.istef.shoppinglist.model.Item;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.currentTimeMillis;


public class DBManager extends SQLiteOpenHelper {

    public DBManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + DB_TABLE_NAME + '(' +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_ITEM + " TEXT," +
                KEY_SIZE + " INTEGER," +
                KEY_QUANTITY + " INTEGER," +
                KEY_DATE_ADDED + " LONG)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(db);
    }

    //  C R U D

    public long addItem(Item item) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            return db.insert(DB_TABLE_NAME, null, contentValues(item));
        }
    }

    public Item getItem(long id) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(
                     DB_TABLE_NAME,
                     new String[]{KEY_ID, KEY_ITEM, KEY_SIZE, KEY_QUANTITY, KEY_DATE_ADDED},
                     KEY_ID + "=?",
                     new String[]{String.valueOf(id)},
                     null, null, null)
        ) {
            return !cursor.moveToFirst() ? null : itemAtCursor(cursor);
        }
    }


    public List<Item> getAllItems() {
        String SELECT_ALL = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY " + KEY_DATE_ADDED + " DESC";

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(SELECT_ALL, null)
        ) {
            List<Item> itemList = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    itemList.add(itemAtCursor(cursor));
                } while (cursor.moveToNext());
            }

            return itemList;
        }
    }

    public int updateItem(Item item) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            Log.d("===============", "ID: " + item.toString());
            return db.update(
                    DB_TABLE_NAME,
                    contentValues(item),
                    KEY_ID + "=?",
                    new String[]{String.valueOf(item.getId())});
        }
    }

    public int deleteItem(Item item) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            return db.delete(DB_TABLE_NAME,
                    KEY_ID + "=?",
                    new String[]{String.valueOf(item.getId())});
        }
    }

    public int itemCount() {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE_NAME, null)
        ) {
            return cursor.getCount();
        }
    }


    @NonNull
    private static ContentValues contentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM, item.getItem());
        values.put(KEY_SIZE, item.getSize());
        values.put(KEY_QUANTITY, item.getQuantity());
        values.put(KEY_DATE_ADDED, currentTimeMillis());
        return values;
    }

    @NonNull
    private static Item itemAtCursor(Cursor cursor) {
        return new Item(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3),
                DateFormat.getDateInstance().format(new Date(cursor.getLong(4)))
        );
    }

}
