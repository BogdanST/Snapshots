package a300.cem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "user.db";
    public static final String DATABASE_TABLE = "user_table";


    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null , 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (uid, email, name, profile)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addContact(UserObjectDb userObjectDb) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", userObjectDb.getUid());
        contentValues.put("email", userObjectDb.getEmail());
        contentValues.put("name", userObjectDb.getName());
        contentValues.put("profile", userObjectDb.getProfile());

        long result = sqLiteDatabase.insert("userTable", null, contentValues);

        if (result > 0) {
            Log.d("dbhelper", "inserted successfully");
        } else {
            Log.d("dbhelper", "failed to insert");
        }
        sqLiteDatabase.close();
    }
}
