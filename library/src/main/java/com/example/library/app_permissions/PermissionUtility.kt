package com.example.common.app_permissions

import android.Manifest

class PermissionUtility {

    //    PERMISSIONS CODE
    var REQUEST_CALL_CODE = 1
    var REQUEST_READ_EXTERNAL_STORAGE = 2
    var REQUEST_WRITE_EXTERNAL_STORAGE = 3

    //    PERMISSIONS
    val CALL_PHONE = Manifest.permission.CALL_PHONE
    val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

}
