package com.example.common.util

import android.content.Context

class SharedPreferenceUtility {

    companion object {
        var SHARED_PREFERENCE_NAME = ""
    }

    fun setData(context: Context, key: String, value: Any) {
        if (value is String)
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putString(key, value).apply()
        else if (value is Int)
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putInt(key, value).apply()
        else if (value is Long)
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putLong(key, value).apply()
        else if (value is Float)
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putFloat(key, value).apply()
        else if (value is Boolean)
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putBoolean(key, value).apply()
        else
            MyLog().printLog("SharedPreferenceUtility", "Value Type is Not Allowed")

    }

    fun getData(context: Context, key: String, defValue: Any): Any {
        if (defValue is Int)
            return context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getInt(key, defValue)
        else if (defValue is Long)
            return context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getLong(key, defValue)
        else if (defValue is Float)
            return context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getFloat(key, defValue)
        else if (defValue is Boolean)
            return context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getBoolean(key, defValue)
        else
            return context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getString(key, defValue.toString())
    }
}