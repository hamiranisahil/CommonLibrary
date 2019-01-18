package com.example.common.util

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import java.io.File


class IntentUtility {

    fun openActivity(from: Context, to: Class<out AppCompatActivity>, bundle: Bundle?, isFinish: Boolean) {
        val intent = Intent(from, to)
        if (bundle != null)
            intent.putExtras(bundle)
        from.startActivity(intent)
        if (isFinish)
            (from as AppCompatActivity).finish()
    }

    fun openParticularApp(context: Context, appPackageName: String, appUriString: String) {
        if (isAppInstalled(context, appPackageName)) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUriString))
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No Any App Found..", Toast.LENGTH_LONG).show()
        }
    }

    fun openUrl(context: Context, url: String?) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun openUrlInYoutube(context: Context, url: String?) {
        val youtubeID = url!!.substring(url.indexOf('=') + 1, url.length)
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeID))
        try {
            context.startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(intentBrowser)
        }
    }

    fun chooseSelectedAppToShare(context: Context, text: String?, fileString: String?) {
        val targetShareIntents = ArrayList<Intent>()
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        val resInfos = context.packageManager.queryIntentActivities(shareIntent, 0)
        if (!resInfos.isEmpty()) {
            println("Have package")
            for (resInfo in resInfos) {
                val packageName = resInfo.activityInfo.packageName
                MyLog().printLog("Package Name", packageName)
                if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana") || packageName.contains(
                        "com.whatsapp"
                    )
                ) {
                    val intent = Intent()
                    intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                    intent.action = Intent.ACTION_SEND
                    if (text != null) {
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, text)
                    }
                    if (fileString != null) {
                        intent.type = "image/jpeg"
                        intent.putExtra(
                            Intent.EXTRA_STREAM,
                            FileProvider.getUriForFile(
                                context,
                                context.applicationContext.packageName + ".my.package.name.provider",
                                File(fileString)
                            )
                        )
                    }
                    intent.setPackage(packageName)
                    targetShareIntents.add(intent)
                }
            }
            if (!targetShareIntents.isEmpty()) {
                println("Have Intent")
                val chooserIntent = Intent.createChooser(targetShareIntents.removeAt(0), "Choose app to share")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toTypedArray<Parcelable>())
                context.startActivity(chooserIntent)
            }
        }
    }

    fun shareUrlTextImageVideo(context: Context, text: String?, fileString: String?) {
        shareUrlTextImageVideo(context, text, fileString, null)
    }

    fun shareUrlTextImageVideo(context: Context, text: String?, fileString: String?, packageName: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        if (text != null) {
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
        }
        if (fileString != null) {
            intent.type = "image/jpeg"
            intent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName + ".my.package.name.provider",
                    File(fileString)
                )
            )
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (packageName != null) {
            intent.setPackage(packageName)
            context.startActivity(intent)

        } else {
            context.startActivity(Intent.createChooser(intent, "Choose App to Share"))
        }
    }

    fun shareUrlTextImageVideoWhatsapp(
        context: Context,
        phoneNumber: String,
        string: String?,
        uri: Uri?,
        packageName: String
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        if (string != null) {
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, string)
        }
        if (uri != null) {
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
        }

        intent.putExtra("jid", PhoneNumberUtils.stripSeparators(phoneNumber) + "@s.whatsapp.net")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (packageName != null) {
            intent.setPackage(packageName)
            context.startActivity(intent)
        } else {
            context.startActivity(Intent.createChooser(intent, "Choose App to Share"))
        }
    }

    fun isAppInstalled(context: Context, appPackageName: String): Boolean {
        val packageManager = context.packageManager
        var installed: Boolean
        try {
            packageManager.getPackageInfo(appPackageName, PackageManager.GET_ACTIVITIES)
            installed = true
        } catch (e: Exception) {
            installed = false
        }
        return installed

    }

    class AppPackageName {
        companion object {
            val PLAY_STORE = "com.android.vending"
            val TWITTER = "com.twitter.android"
        }
    }
}
