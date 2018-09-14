package com.avinashdavid.androidextensions

internal const val LOG_TAG_PREFIX = "AndroidExtensions"
internal fun getLogTagForFile(fileName: String) =
        if ("$LOG_TAG_PREFIX$fileName".length > 23) "$LOG_TAG_PREFIX$fileName"
        else fileName