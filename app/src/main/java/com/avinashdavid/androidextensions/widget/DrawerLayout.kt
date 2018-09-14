package com.avinashdavid.androidextensions.widget

import android.support.v4.widget.DrawerLayout
import android.view.Gravity


fun DrawerLayout.lockStartDrawer() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.START)
}

fun DrawerLayout.unlockStartDrawer() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.START)
}

fun DrawerLayout.lockEndDrawer() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END)
}

fun DrawerLayout.unlockEndDrawer() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.END)
}
