package com.example.common.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

class FileUtility {
    fun getExt(string: String): String {
        var strLength = string.lastIndexOf('.')
        if (strLength > 0) {
            return string.substring(strLength + 1).toLowerCase()
        }
        return ""
    }

    fun getMimeType(context: Context, uri: Uri): String? {
        var mimeType: String? = null
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            mimeType = context.contentResolver.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
        }
        return mimeType
    }
}