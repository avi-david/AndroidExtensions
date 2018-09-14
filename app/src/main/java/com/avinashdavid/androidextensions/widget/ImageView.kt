package com.avinashdavid.androidextensions.widget

import android.content.Context
import android.graphics.ColorFilter
import android.os.Build
import android.support.v4.content.ContextCompat
import android.widget.ImageView

/**
 * @param drawableResourceId: Drawable resource ID that is accessible from this imageView's context
 * @param colorResourceId: Color resource ID that is accessible from this imageView's context
 */
fun ImageView.setVectorDrawableWithColor(drawableResourceId: Int, colorResourceId: Int) {
    if (Build.VERSION.SDK_INT >= 21) {
        this.setImageDrawable(this.context.resources.getDrawable(drawableResourceId, this.context.resources.newTheme()))
    } else {
        this.setImageDrawable(this.context.resources.getDrawable(drawableResourceId))
    }
    this.setColorFilter(ContextCompat.getColor(context, colorResourceId), android.graphics.PorterDuff.Mode.SRC_IN)
}

/**
 * @param drawableResourceId: Drawable resource ID that is accessible from this imageView's context
 */
fun ImageView.setVectorDrawableWithColor(drawableResourceId: Int, colorFilter: ColorFilter) {
    if (Build.VERSION.SDK_INT >= 21) {
        this.setImageDrawable(this.context.resources.getDrawable(drawableResourceId, this.context.resources.newTheme()))
    } else {
        this.setImageDrawable(this.context.resources.getDrawable(drawableResourceId))
    }
    this.colorFilter = colorFilter
}

/**
 * @param colorResourceId: Color resource ID that is accessible from this imageView's context
 */
fun ImageView.setTint(colorResourceId: Int) {
    this.setColorFilter(ContextCompat.getColor(context, colorResourceId))
}