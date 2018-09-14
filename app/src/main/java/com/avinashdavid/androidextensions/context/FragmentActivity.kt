package com.avinashdavid.androidextensions.context

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

/**
 * @param toolbar: The toolbar view
 * @param setHomeAsUp: Whether to set the the home button as up
 * @param themeId: Theme for the activity
 * @param toolbarBackgroundColorResourceId: Background color for the toolbar
 * @param upButtonDrawable: Drawable for the up (home) button
 * @param menuButtonDrawable: Drawable for the menu (end) button
 */
fun AppCompatActivity.setToolbar(toolbar: Toolbar, setHomeAsUp: Boolean, themeId: Int? = null, toolbarBackgroundColorResourceId: Int = -1, upButtonDrawable: Drawable? = null, menuButtonDrawable: Drawable? = null) {
    if (toolbarBackgroundColorResourceId != -1)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, toolbarBackgroundColorResourceId))
    if (themeId != null) {
        toolbar.context.setTheme(themeId)
    }
    setSupportActionBar(toolbar)
    if (setHomeAsUp) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        upButtonDrawable?.let {
            supportActionBar?.setHomeAsUpIndicator(it)
        }
    }
    menuButtonDrawable?.let {
        toolbar.overflowIcon = it
    }
}

//REGION: Fragments
/**
 * @return first fragment of the provided type, or null if none are found
 */
inline fun <reified T> FragmentActivity.getFirstFragmentOfType() : T? {
    var fragmentId = -1
    supportFragmentManager.fragments.forEach {
        if (it is T) {
            fragmentId = it.id
        }
    }
    return if (fragmentId > -1) {
        supportFragmentManager.fragments.first { f -> f is T } as T
    } else {
        null
    }
}

/**
 * @return all fragments of the provided type, or null if none are found
 */
inline fun <reified T> FragmentActivity.getAllFragmentsOfType() : List<T>? {
    val fragments = mutableListOf<T>()
    supportFragmentManager.fragments.forEach {
        if (it is T) {
            fragments.add(it)
        }
    }
    return if (fragments.count() > 0) {
        fragments.toList()
    } else {
        null
    }
}

/**
 * @param containerViewId: ID of the view into which the fragment will be placed
 * @param fragment: The fragment to be inserted
 * @param fragmentTag: Tag for the fragment
 * @param addToBackStack: Whether this transaction is to be added to the Android backstack. Defaults to true
 * @param backStackName: Name for the backstack on which to add this fragment
 * @return REPLACES the fragment in the container view
 */
fun FragmentActivity?.replaceFragmentInContainer(containerViewId: Int, fragment: Fragment, fragmentTag: String? = null, addToBackStack: Boolean = true, backStackName: String? = null) {
    this?.let {
        supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment, fragmentTag).apply {
                    if (addToBackStack) {
                        addToBackStack(backStackName)
                    }
                }
                .commit()
    }
}

/**
 * @param containerViewId: ID of the view into which the fragment will be placed
 * @param fragment: The fragment to be inserted
 * @param fragmentTag: Tag for the fragment
 * @param addToBackStack: Whether this transaction is to be added to the Android backstack. Defaults to true
 * @param backStackName: Name for the backstack on which to add this fragment
 * @return ADDS the fragment in the container view
 */
fun FragmentActivity?.addFragmentInContainer(containerViewId: Int, fragment: Fragment, fragmentTag: String? = null, addToBackStack: Boolean = true, backStackName: String? = null) {
    this?.let {
        supportFragmentManager.beginTransaction()
                .add(containerViewId, fragment, fragmentTag).apply {
                    if (addToBackStack) {
                        addToBackStack(backStackName)
                    }
                }
                .commit()
    }
}

/**
 * @param dialogFragment: the dialog fragment
 * @param fragmentTag: tag for fragment in transaction
 * @param enterAnimation (optional): enter animation for dialog. Must also provide exit animation if providing this.
 * @param exitAnimation (optional): exit animation for dialog. Defaults to value for enter animation.
 */
fun FragmentActivity?.showDialogFragment(dialogFragment: DialogFragment, fragmentTag: String, enterAnimation: Int = -1, exitAnimation: Int = enterAnimation) {
    val fm = this?.supportFragmentManager
    fm?.let {
        val ft = fm.beginTransaction();
        val prev = fm.findFragmentByTag(fragmentTag)
        prev?.let {
            ft.remove(it)
        }
        if (enterAnimation != -1 && exitAnimation != -1) ft.setCustomAnimations(enterAnimation, exitAnimation);
        dialogFragment.show(ft, fragmentTag);
    }
}
///REGION

//REGION: Broadcast receivers
/**
 * @param broadcastReceiversWithIntentFilters: A list of pairs containing broadcast receivers and their respective intent filters
 */
fun FragmentActivity.registerBroadcastReceivers(broadcastReceiversWithIntentFilters: List<Pair<BroadcastReceiver, IntentFilter>>) {
    broadcastReceiversWithIntentFilters.forEach {
        registerReceiver(it.first, it.second)
    }
}

/**
 * @param broadcastReceivers: A list of broadcast receivers to be unregistered
 */
fun FragmentActivity.unregisterBroadcastReceivers(broadcastReceivers: List<BroadcastReceiver>) {
    broadcastReceivers.forEach {
        unregisterReceiver(it)
    }
}
///REGION