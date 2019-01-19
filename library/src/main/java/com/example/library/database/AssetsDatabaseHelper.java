package com.example.library.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.*;

public class AssetsDatabaseHelper {

    // TABLE
    public static final String TABLE_USER_PRODUCT = "UserProduct";

    // COLUMNS
    public static final String COLUMN_ID = "id";

    private static String DATABASE_NAME = "name.db";

    private Context context;
    private String TAG = "AssetsDatabase";

    public AssetsDatabaseHelper(Context context) {
        this.context = context;
    }


    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                SQLiteDatabase checkDB = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
                if (checkDB != null) {
                    checkDB.close();
                }
                copyDatabase(dbFile);
            } catch (IOException ioe) {
                Log.e(TAG, "IOException: " + ioe.toString());
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }


    private void copyDatabase(File dbFile) throws IOException {
        InputStream dbInputStream = context.getAssets().open(DATABASE_NAME);
        OutputStream dbOutputStream = new FileOutputStream(dbFile);
        byte[] buffer = new byte[1024];
        while (dbInputStream.read(buffer) > 0) {
            dbOutputStream.write(buffer);
        }
        dbOutputStream.flush();
        dbOutputStream.close();
        dbInputStream.close();
    }

    public void deleteDatabase() {
        context.deleteDatabase(DATABASE_NAME);
    }
}