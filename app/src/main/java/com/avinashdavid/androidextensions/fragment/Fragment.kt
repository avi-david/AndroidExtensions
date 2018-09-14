package com.avinashdavid.androidextensions.fragment

import android.os.Bundle
import android.util.Log
import com.avinashdavid.androidextensions.getLogTagForFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @throws Exception: Gson exceptions are passed through
 * @param name: Name of the JSON-ified collection object in the bundle
 * @return An object of this collection type, if the bundle contains a value with the named key
 */
inline fun <reified T: Collection<Any>> Bundle.getListOfJSONifiedObjects(name: String) : T? {
    if (containsKey(name)) {
        return try {
            Gson().fromJson<T>(getString(name), object : TypeToken<T>(){}.type)
        } catch (e: Exception) {
            Log.e("AndroidExtensionsBundle", "Error parsing collection object for name: $name", e)
            null
        }
    }
    return null
}

/**
 * @since Collections are usually not handled well by this method. Please use Bundle.getListOfJSONifiedObjects<T>(name: String) instead when parsing into collections
 * @throws Exception: Gson exceptions are passed through
 * @param name: Name of the JSON-ified object in the bundle
 * @return An object of this type, if the bundle contains a value with the named key
 */
inline fun <reified T> Bundle.getJSONifiedObject(name: String) : T? {
    if (containsKey(name)) {
        val gson = Gson()
        return try {
            if (T::class is Collection<*>) {
                gson.fromJson<T>(getString(name), object : TypeToken<T>(){}.type)
            } else {
                gson.fromJson(getString(name), T::class.java)
            }
        } catch (e: Exception) {
            Log.e("AndroidExtensionsBundle", "Error parsing object for name: $name", e)
            null
        }
    }
    return null
}

/**
 * @param dataToSave: A JSON-ifiable object
 * @param name: The name under which dataToSave will be put on the bundle
 */
fun Bundle.putJSONifiedObject(dataToSave: Any?, name: String) {
    dataToSave?.let {
        try {
            putString(name, Gson().toJson(it))
        } catch (e: Exception) {
            Log.e(getLogTagForFile("Bundle"), "Error while JSON-ifying object $dataToSave", e)
        }
    }
}