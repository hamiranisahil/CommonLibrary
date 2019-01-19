package com.example.common.util

import android.util.Log
import com.example.library.BuildConfig

class MyLog {

    fun printLog(tag: String, message: String) {
        if (BuildConfig.DEBUG)
            Log.e(tag, message)
    }

}