package com.example.common.util

import android.util.Patterns
import android.widget.EditText

class ValidatorUtility {

    fun getTextFromEditText(editText: EditText): String {
        return editText.text.toString()
    }

    fun isEmpty(editText: EditText): Boolean {
        return isEmpty(getTextFromEditText(editText))
    }

    fun isEmpty(string: String): Boolean {
        return string.trim().isEmpty()
    }

    fun isValidMobileNumber(editText: EditText): Boolean {
        return isValidMobileNumber(getTextFromEditText(editText))
    }

    fun isValidMobileNumber(string: String): Boolean {
        return Patterns.PHONE.matcher(string).matches()
    }

    fun isValidEmailAddress(editText: EditText): Boolean {
        return isValidEmailAddress(getTextFromEditText(editText))
    }

    fun isValidEmailAddress(string: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(string).matches()
    }

    fun isValidMin(editText: EditText, min: Int): Boolean {
        return isValidMin(getTextFromEditText(editText), min)
    }

    fun isValidMin(string: String, min: Int): Boolean {
        return string.length >= min
    }

    fun isValidMax(editText: EditText, max: Int): Boolean {
        return isValidMax(getTextFromEditText(editText), max)
    }

    fun isValidMax(string: String, max: Int): Boolean {
        return string.length <= max
    }

    fun isValidMinMax(editText: EditText, min: Int, max: Int): Boolean {
        return isValidMinMax(getTextFromEditText(editText), min, max)
    }

    fun isValidMinMax(string: String, min: Int, max: Int): Boolean {
        return isValidMin(string, min) && isValidMax(string, max)
    }
}
