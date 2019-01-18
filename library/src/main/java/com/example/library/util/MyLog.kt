package com.example.common.util

import android.util.Log

class MyLog {

    fun printLog(tag: String, message: String) {
        if (BuildConfig.DEBUG)
            Log.e(tag, message)
    }

}