package com.phamhuong.library.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phamhuong.library.model.UserLoginInfo;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "login_info.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_LOGIN = "login";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_USERID = "user_id";
    public static final String COLUMN_FULLNAME = "full_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TOKEN = "token";
    public static final String COLUMN_AVATAR_URL = "avatar_url";
    public static final String PHONE_NUMBER = "phone_number";

    private static final String SQL_CREATE_LOGIN_TABLE =
            "CREATE TABLE " + TABLE_LOGIN + " (" +
                    COLUMN_USERID + " INTEGER, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_FULLNAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_TOKEN + " TEXT, " +
                    COLUMN_AVATAR_URL + " TEXT,"+
                    PHONE_NUMBER + " TEXT"+ ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + COLUMN_AVATAR_URL + " TEXT");
        }
        // Xử lý nâng cấp cơ sở dữ liệu nếu cần
        // Trong trường hợp đơn giản này, chúng ta có thể xóa bảng cũ và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);
    }

    public long saveLoginInfoSQLite(String username, String password, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_TOKEN, token);

        // Chèn một hàng mới vào bảng login và trả về ID của hàng vừa được chèn
        long newRowId = db.insert(TABLE_LOGIN, null, values);
        db.close(); // Đóng kết nối cơ sở dữ liệu sau khi hoàn tất
        return newRowId;
    }
    public void saveGuestLoginInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "guest");
        values.put(COLUMN_PASSWORD, "");
        values.put(COLUMN_TOKEN, "");

        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }
    public UserLoginInfo getLoginInfoSQLite() {
        SQLiteDatabase db = this.getReadableDatabase();
        UserLoginInfo loginInfo = null;
        Cursor cursor = db.query(
                TABLE_LOGIN,     // Table name
                null,            // All columns
                null,            // WHERE clause
                null,            // WHERE args
                null,            // GROUP BY
                null,            // HAVING
                null,  // ORDER BY (tuỳ chọn)
                "1"              // LIMIT 1
        );
        if (cursor!=null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USERID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            String token = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOKEN));
            String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVATAR_URL)); // Lấy avatarUrl
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(PHONE_NUMBER));
            loginInfo = new UserLoginInfo(userId, username, password, fullName, email, token, avatarUrl, phoneNumber);
            cursor.close();
            db.close();
        }
        return loginInfo;
    }

    public int updateLoginInfoSQLite(int userId, String fullName, String email, String usernameToUpdate, String avatarUrl, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_USERID, userId);
        values.put(COLUMN_AVATAR_URL, avatarUrl);
        values.put(PHONE_NUMBER, phoneNumber);

        // Cập nhật hàng trong bảng login dựa trên username
        int rowsAffected = db.update(
                TABLE_LOGIN,
                values,
                COLUMN_USERNAME + "=?",
                new String[]{usernameToUpdate}
        );
        db.close(); // Đóng kết nối cơ sở dữ liệu sau khi hoàn tất
        return rowsAffected;
    }

    public int updateInfo(String usernameToUpdate, String fullName, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, fullName);
        values.put(PHONE_NUMBER, phoneNumber);

        // Cập nhật hàng trong bảng login dựa trên username
        int rowsAffected = db.update(
                TABLE_LOGIN,
                values,
                COLUMN_USERNAME + "=?",
                new String[]{usernameToUpdate}
        );
        db.close(); // Đóng kết nối cơ sở dữ liệu sau khi hoàn tất
        return rowsAffected;
    }

    public int updateAvatarUrl(String usernameToUpdate, String avatarUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AVATAR_URL, avatarUrl);

        // Cập nhật hàng trong bảng login dựa trên username
        int rowsAffected = db.update(
                TABLE_LOGIN,
                values,
                COLUMN_USERNAME + "=?",
                new String[]{usernameToUpdate}
        );
        db.close(); // Đóng kết nối cơ sở dữ liệu sau khi hoàn tất
        return rowsAffected;
    }

    public void clearAllLoginData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}
