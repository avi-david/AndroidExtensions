package com.avinashdavid.androidextensions.widget

import android.view.View
import android.widget.PopupMenu

/**
 * @return Sets view visibility to View.GONE
 */
fun View.makeGone() {
    this.visibility = View.GONE
}

/**
 * @return Sets view visibility to View.INVISIBLE
 */
fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

/**
 * @return Sets view visibility to View.VISIBLE
 */
fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

/**
 * @param menuResourceId: Resource ID for menu
 * @param onMenuItemClickListener: Menu item click listener that accounts for all clickable items in the provided menu
 * @return The created popup menu
 */
fun View.showPopupMenu(menuResourceId: Int, onMenuItemClickListener: PopupMenu.OnMenuItemClickListener): PopupMenu {
    val popup = PopupMenu(context, this)
    popup.setOnMenuItemClickListener(onMenuItemClickListener)
    popup.inflate(menuResourceId)
    popup.show()
    return popup
}