package com.avinashdavid.androidextensions.widget

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * @param webViewClient: WebViewClient configured per your requirements
 * @param webChromeClient: WebChromeClient configured per your requirements
 * @param loadWithOverviewMode: value for settings.loadWithOverviewMode
 * @param useWideViewPort: value for settings.useWideViewPort
 */
fun WebView.configureWebview(webViewClient: WebViewClient?, webChromeClient: WebChromeClient?,
                             loadWithOverviewMode: Boolean = true, useWideViewPort: Boolean = true) {
    this.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
    this.settings.defaultZoom = WebSettings.ZoomDensity.FAR
    this.settings.loadWithOverviewMode = loadWithOverviewMode
    this.settings.useWideViewPort = useWideViewPort

    webViewClient?.let {
        this.webViewClient = it
    }
    webChromeClient?.let {
        this.webChromeClient = it
    }
}