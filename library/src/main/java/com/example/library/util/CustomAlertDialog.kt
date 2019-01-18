package com.example.common.util

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog

class CustomAlertDialog {

    fun showDialogWithOneButton(
        context: Context,
        title: String,
        message: String,
        buttonLabel: String,
        alertButtonClickListener: AlertButtonClickListener
    ) {
        val builder = AlertDialog.Builder(context).setTitle(title).setMessage(message)
        builder.setPositiveButton(buttonLabel) { dialog, which -> alertButtonClickListener.onAlertClick(dialog, which) }
        builder.create().show()
    }

    fun showDialogWithTwoButton(
        context: Context,
        title: String,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String,
        alertTwoButtonClickListener: AlertTwoButtonClickListener
    ) {
        val builder = AlertDialog.Builder(context).setTitle(title).setMessage(message)
        builder.setPositiveButton(positiveButtonLabel) { dialog, which ->
            alertTwoButtonClickListener.onAlertClick(
                dialog,
                which,
                true
            )
        }
        builder.setNegativeButton(negativeButtonLabel) { dialog, which ->
            alertTwoButtonClickListener.onAlertClick(
                dialog,
                which,
                false
            )
        }
        builder.create().show()
    }

    interface AlertButtonClickListener {
        fun onAlertClick(dialog: DialogInterface, which: Int)
    }

    interface AlertTwoButtonClickListener {
        fun onAlertClick(dialog: DialogInterface, which: Int, isPositive: Boolean)
    }
}
