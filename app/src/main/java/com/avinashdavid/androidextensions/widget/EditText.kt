package com.avinashdavid.androidextensions.widget

import android.widget.EditText

/**
 * @return The entered text as a String
 */
fun EditText.getString() : String? {
    return text?.toString()
}

/**
 * @return true if EditText is null or empty, and false if not
 */
fun EditText.isNullOrEmpty() : Boolean {
    return getString().isNullOrEmpty()
}