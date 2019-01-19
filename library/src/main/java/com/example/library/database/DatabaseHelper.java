package com.example.library.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.gson.Gson;

import java.util.List;


public class DatabaseHelper {

    static AssetsDatabaseHelper assetsDatabaseHelper;
    static SQLiteDatabase sqLiteDatabase;
    private static DatabaseHelper databaseHelper;
    Context context;
    private String TAG = "DatabaseHelper";

    private DatabaseHelper() {
    }

    public static DatabaseHelper getInstance(Context context) {

        if (databaseHelper == null)
            databaseHelper = new DatabaseHelper();

        if (assetsDatabaseHelper == null)
            assetsDatabaseHelper = new AssetsDatabaseHelper(context);

        openDatabase();

        return databaseHelper;
    }


    private static void openDatabase() {
        closeDatabase();
        sqLiteDatabase = assetsDatabaseHelper.openDatabase();
    }

    private static void closeDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
            sqLiteDatabase.close();
    }


    public long insert(String tableName, List<String> columnNames, List columnValues) {
        Log.e(TAG, "insert: " + new Gson().toJson(columnValues));
        try {
            long id = sqLiteDatabase.insert(tableName, null, getContentValues(columnNames, columnValues));
            return id;
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.toString());
            return 0;
        }
    }

    public boolean update(String tableName, List<String> columnNames, List columnValues, String where, List whereArgs) {
        try {
            boolean isUpdate = sqLiteDatabase.update(tableName, getContentValues(columnNames, columnValues), where, getStringArgs(whereArgs)) != -1 ? true : false;
            return isUpdate;
        } catch (Exception e) {
//            MyLogs.printLog('e', TAG, "Exception: "+e.toString());
            return false;
        }
    }

    public boolean delete(String tableName, String where, List whereArgs) {
        try {
            boolean isDelete = sqLiteDatabase.delete(tableName, where, getStringArgs(whereArgs)) > 0 ? true : false;
            return isDelete;
        } catch (Exception e) {
//            MyLogs.printLog('e', TAG, "Exception: "+e.toString());
            return false;
        }
    }

    public Cursor getWhere(String tableName, List<String> columns, String selection, List selectionArgs, String order, int limitStart, int limitEnd) {
        try {
            Cursor cursor = sqLiteDatabase.query(tableName, getStringArgs(columns), selection, getStringArgs(selectionArgs), null, null, order, limitStart == -1 ? null : "" + limitStart + "," + limitEnd);
            if (cursor != null && cursor.getCount() > 0) {
                return cursor;
            } else {
                return null;
            }
        } catch (Exception e) {
//            MyLogs.printLog('e', TAG, "Exception: "+e.toString());
            return null;
        }
    }

    public int clearTable(String tableName) {
        int rows = sqLiteDatabase.delete(tableName, null, null);
        return rows;
    }

    public Cursor getSearch(String tableName, String idColumnName, String where, List selectionArgs) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + idColumnName + " _id, * " + " FROM " + tableName + " WHERE " + where, getStringLikeArgs(selectionArgs));
        return cursor;
    }

    public boolean isRowExists(String tableName, String selection, List selectionArgs) {
        Cursor cursor = sqLiteDatabase.query(tableName, null, selection, getStringArgs(selectionArgs), null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    private String[] getStringLikeArgs(List columnNames) {
        String[] stringArray = null;
        if (columnNames != null) {
            stringArray = new String[columnNames.size()];
            for (int i = 0; i < columnNames.size(); i++) {
                stringArray[i] = "%" + String.valueOf(columnNames.get(i)) + "%";
            }
        }
        return stringArray;
    }

    private String[] getStringArgs(List columnNames) {
        String[] stringArray = null;
        if (columnNames != null) {
            stringArray = new String[columnNames.size()];
            for (int i = 0; i < columnNames.size(); i++) {
                stringArray[i] = String.valueOf(columnNames.get(i));
            }
        }
        return stringArray;
    }

    private ContentValues getContentValues(List<String> columnNames, List columnValues) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnValues.get(i) instanceof String) {
                values.put(columnNames.get(i), (String) columnValues.get(i));
            } else if (columnValues.get(i) instanceof Integer) {
                values.put(columnNames.get(i), (Integer) columnValues.get(i));
            } else if (columnValues.get(i) instanceof Long) {
                values.put(columnNames.get(i), (Long) columnValues.get(i));
            } else if (columnValues.get(i) instanceof Float) {
                values.put(columnNames.get(i), (Float) columnValues.get(i));
            } else if (columnValues.get(i) instanceof Boolean) {
                values.put(columnNames.get(i), (Boolean) columnValues.get(i) ? 1 : 0);
            }
        }
        return values;
    }
}