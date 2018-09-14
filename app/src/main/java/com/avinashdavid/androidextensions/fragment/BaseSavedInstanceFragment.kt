package com.avinashdavid.androidextensions.fragment

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseSavedInstanceFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listOf(arguments, savedInstanceState).forEach {
            it?.let {
                getPropertiesFromBundle(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val savedBundle = savePropertiesIntoBundle(outState)
        super.onSaveInstanceState(savedBundle)
    }

    /**
     * @param bundle: arguments and savedInstanceState. In onCreate(savedInstanceState: Bundle?), this method is called on each bundle in that order
     */
    abstract fun getPropertiesFromBundle(bundle: Bundle)

    /**
     * @param bundle: savedInstanceState. In onSaveInstanceState(outState: Bundle), outState is passed into this method
     * @return The final bundle passed to super.onSaveInstanceState(bundle)
     */
    abstract fun savePropertiesIntoBundle(bundle: Bundle): Bundle
}