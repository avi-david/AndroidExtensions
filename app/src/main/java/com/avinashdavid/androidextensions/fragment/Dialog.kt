package com.avinashdavid.androidextensions.fragment

import android.app.Dialog
import android.view.Gravity
import android.view.View

/**
 * @param anchorView: View below which to anchor this dialog
 * @see window: If this dialog's window is null, the anchor will not take place
 */
fun Dialog.anchorBelow(anchorView: View) {
    val lp = this.window?.attributes
    var outArray: IntArray = IntArray(2).apply {
        this[0] = 100
        this[1] = 100
    }
    anchorView.getLocationInWindow(outArray)
    val x = outArray[0]
    val y = outArray[1]
    lp?.x = x
    lp?.y = y
    lp?.gravity = Gravity.START or Gravity.TOP
}

/**
 * @param styleResourceId: animation style resource ID
 */
fun Dialog.setDialogAnimation(styleResourceId: Int) {
    this.window?.attributes?.windowAnimations = styleResourceId
}