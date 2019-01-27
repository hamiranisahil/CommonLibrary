package com.example.common.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.*
import java.text.DecimalFormat

class FileUtility {

    fun copyFile(context: Context, inputFilePath: String, outputFilePath: String, outputFileName: String) {
        var inputStream: InputStream?
        var outputStream: OutputStream?

        try {
            val dir = File(outputFilePath)
            if (dir.exists()) {
                dir.mkdirs()
            }
            inputStream = FileInputStream(inputFilePath)

            var outputFilePathFinal = outputFilePath
            var outputFileNameFinal = outputFileName

            if (outputFilePathFinal.substring(outputFilePathFinal.length - 1) != "/") {
                outputFilePathFinal += "/"
            }

            if (outputFileNameFinal.lastIndexOf('.') < 0) {
                outputFileNameFinal += inputFilePath.substring(inputFilePath.lastIndexOf('.'))
            }

            if (!File(outputFilePathFinal).exists()) {
                File(outputFilePathFinal).mkdirs()
            }

            outputStream = FileOutputStream(outputFilePathFinal + outputFileNameFinal)

            val buffer = ByteArray(1024)
            var read = 0
            while (true) {
                read = inputStream.read(buffer)
                if (read == -1) {
                    break
                }
                outputStream.write(buffer, 0, read)
            }
            inputStream.close()
            inputStream = null

            outputStream.flush()
            outputStream.close()
            outputStream = null

        } catch (fnfe: FileNotFoundException) {
            Log.e("FileUtility", fnfe.message)
        } catch (e: Exception) {
            Log.e("FileUtility", e.message)
        }
    }

    fun deleteFile(context: Context, filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
    }

    fun deleteAllFile(folderName: String) {
        val folder = File(folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        for (file in folder.listFiles()) {
            if (file.exists()) {
                file.delete()
            }
        }
    }

    fun getFileSize(file: File): String {
        val fileSizeInBytes = file.length()

        val fileSizeInKB = DecimalFormat("##.##").format(fileSizeInBytes.toDouble() / 1024).toDouble()
        val fileSizeInMB = DecimalFormat("##.##").format(fileSizeInKB.toDouble() / 1024).toDouble()
        val fileSizeInGB = DecimalFormat("##.##").format(fileSizeInMB.toDouble() / 1024).toDouble()

        var fileSize = ""

        if (fileSizeInGB > 1.0) {
            fileSize = "$fileSizeInGB GB"

        } else if (fileSizeInMB > 1.0) {
            fileSize = "$fileSizeInMB MB"

        } else {
            fileSize = "$fileSizeInKB KB"
        }

        return fileSize
    }

    fun removeFileExtention(fileName: String): String {
        var name = ""
        if (name.indexOf(".") > 0)
            name = name.substring(0, name.lastIndexOf("."))
        return name
    }

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