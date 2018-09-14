package com.avinashdavid.androidextensions.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.ImageButton

/**
 * @param colorResourceId: Color resource ID that is accessible from this imageView's context
 */
fun ImageButton.setTint(colorResourceId: Int) {
    this.setColorFilter(ContextCompat.getColor(context, colorResourceId))
}