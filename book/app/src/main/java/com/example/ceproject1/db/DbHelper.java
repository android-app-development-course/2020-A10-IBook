package com.example.ceproject1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 数据库工具类
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context ctx) {
        super(ctx, "BookManageSystem", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //用户表
        db.execSQL("CREATE TABLE if not exists user(id integer PRIMARY KEY autoincrement,"
                + " nickName text, password text, url text )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static DbHelper dbManager;


    public static DbHelper getInstance(Context ctx) {
        if (dbManager == null) {
            synchronized (DbHelper.class) {
                if (dbManager == null) {
                    dbManager = new DbHelper(ctx);
                }
            }
        }
        return dbManager;
    }

    public boolean saveUser(UserBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            //注册之前先查询是否重复注册
            Cursor cursor = db.rawQuery("SELECT * FROM user WHERE nickName = ?", new String[]{bean.getName()});
            boolean hasUser = false;
            if (cursor.moveToNext()) {
                hasUser = true;
            }
            cursor.close();
            if (hasUser) {
                return true;
            }
            //如果不重复则注册
            db.execSQL("INSERT INTO user(nickName , password , url  ) " +
                    "VALUES ('" + bean.getName()
                    + "', '" + bean.getPassword()

                    + "', '" + bean.getUrl()
                    + "')");
        }
        return false;
    }





    /**
     * 查找用户（登录操作）
     *

     * @return 用户
     */
    public UserBean findUser( String[] args) {
        UserBean bean = new UserBean();
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            Cursor cursor = db.query("user", null, "nickName = ?", args, null, null, null);
            if (cursor.moveToNext()) {
                bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                bean.setName(cursor.getString(cursor.getColumnIndex("nickName")));

                bean.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                bean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            }
            cursor.close();

        }
        return bean;
    }
    /**
     * 更新
     *
     * @param record
     */
    public void updateUser(UserBean record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", record.getId());
        contentValues.put("nickName", record.getName());

        contentValues.put("password", record.getPassword());

        contentValues.put("url", record.getUrl());
        if (db != null) {
            db.update("user", contentValues, "id = ?", new String[]{record.getId()+""});
        }
    }

    /**
     * 删除信息
     *
     * @param id
     */
    public void deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.execSQL("DELETE FROM user WHERE id = " + id);
        }
    }
}
