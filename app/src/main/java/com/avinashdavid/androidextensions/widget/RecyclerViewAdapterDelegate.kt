package com.avinashdavid.androidextensions.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.avinashdavid.androidextensions.getLogTagForFile

abstract class AdapterDelegate<T> {
    abstract fun isForViewType(items: T, position: Int) : Boolean
    abstract fun onCreateViewholder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(items: T, position: Int, viewHolder: RecyclerView.ViewHolder)

    abstract fun getItemId(items: T, position: Int): Long
}

open class AdapterDelegatesManager<T> {
    private val FALLBACK_DELEGATE_VIEW_TYPE = Int.MAX_VALUE - 1
    protected var fallbackDelegate : AdapterDelegate<T>? = null

    private var adapterDelegates : HashMap<Int, AdapterDelegate<T>> = hashMapOf()

    fun getItemViewType(items: T, position: Int) : Int {
        adapterDelegates.forEach { if (it.value.isForViewType(items, position)) return it.key }

        return FALLBACK_DELEGATE_VIEW_TYPE
    }

    fun getItemId(items: T, position: Int) : Long {
        val itemType = getItemViewType(items, position)
        val delegateForItem = adapterDelegates.get(itemType)
        if (delegateForItem != null)
            return delegateForItem.getItemId(items, position)
        return -1
    }

    /**
     * @throws NullPointerException if no adapter delegate is found for an item
     */
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        val adapterDelegate = try { adapterDelegates.getValue(viewType) } catch (e: Exception) { null }
        adapterDelegate?.let {
            return adapterDelegate.onCreateViewholder(parent)
        }
        throw NullPointerException("ViewHolder returned from AdapterDelegate " + adapterDelegate + " for viewtype " + viewType + " is null!")
    }

    fun onBindViewHolder(items: T, position: Int, viewHolder: RecyclerView.ViewHolder) {
        val adapterDelegate = try { adapterDelegates.getValue(viewHolder.itemViewType) } catch (e: Exception) { null }
        adapterDelegate?.let {
            it.onBindViewHolder(items, position, viewHolder)
        } ?: run {
            Log.e(getLogTagForFile("RecyclerViewAdapterDelegate"), "Empty viewholder bound for item at position: $position",
                    NullPointerException("No delegate found for item at position =  " + position + " for viewtype = " + viewHolder.itemViewType))
        }
    }

    /**
     * @throws IllegalArgumentException if delegate with viewType of Int.MAX_VALUE - 1 is added
     * @throws IllegalArgumentException if an adapter delegate with the same view type has previously been added
     */
    fun addDelegate(adapterDelegate: AdapterDelegate<T>) {
        val viewType= adapterDelegates.count()
        addDelegate(viewType+1, adapterDelegate)
    }

    /**
     * @throws IllegalArgumentException if delegate with viewType of Int.MAX_VALUE - 1 is added
     * @throws IllegalArgumentException if an adapter delegate with the same view type has previously been added
     */
    fun addDelegate(viewType: Int, adapterDelegate: AdapterDelegate<T>, allowReplacingDelegate : Boolean = false) {
        if (viewType == FALLBACK_DELEGATE_VIEW_TYPE)
            throw IllegalArgumentException("The view type " + FALLBACK_DELEGATE_VIEW_TYPE.toString() + " is reserved for the fall back view type. Please try another view type value")

        if (!allowReplacingDelegate && adapterDelegates.containsKey(viewType))
            throw IllegalArgumentException("An AdapterDelegate is already registered for the viewType = " +
                    viewType + " Already registered AdapterDelegate is " +
                    adapterDelegates.get(viewType))

        adapterDelegates[viewType] = adapterDelegate
    }

    fun clearDelegates() {
        adapterDelegates.clear()
    }

    fun getDelegates() = adapterDelegates
}

abstract class BaseAdapterDelegateRecyclerViewAdapter(protected val context: Context, adapterDelegates: List<AdapterDelegate<List<Any>>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val adapterDelegatesManager = AdapterDelegatesManager<List<Any>>()
    protected val items = mutableListOf<Any>()

    /**
     * @throws IllegalArgumentException if FALLBACK_DELEGATE_VIEW_TYPE is added
     * @throws IllegalArgumentException if an adapter delegate with the same view type has previously been added
     */
    init {
        adapterDelegates.forEach {
            adapterDelegatesManager.addDelegate(it)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapterDelegatesManager.onBindViewHolder(items, position, holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return try {
            adapterDelegatesManager.onCreateViewHolder(parent, viewType)
        } catch (e: Exception) {
            ViewHolderGeneric(TextView(context).apply { height = 0 })
        }
    }

    override fun getItemId(position: Int): Long {
        return adapterDelegatesManager.getItemId(items, position)
    }

    override fun getItemViewType(position: Int): Int {
        return adapterDelegatesManager.getItemViewType(items, position)
    }

    fun addItem(item: Any) {
        items.add(item)
        notifyItemInserted(items.count())
    }

    fun addItems(itemsToAdd: List<Any>) {
        items.addAll(itemsToAdd)
        notifyItemRangeInserted(items.count(), itemsToAdd.count())
    }

    fun clearItems() {
        val itemCount = items.count()
        items.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    fun removeItem(position: Int) {
        if (position < items.count() && position >= 0) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun insertItem(position: Int, item: Any) {
        if (position < items.count() && position >= 0) {
            items.add(position, item)
            notifyItemInserted(position)
        }
    }

    fun replaceItem(position: Int, item: Any) {
        if (position < items.count() && position >= 0) {
            items.removeAt(position)
            items.add(position, item)
            notifyItemChanged(position)
        }
    }
}

class ViewHolderGeneric(itemView: View):RecyclerView.ViewHolder(itemView)