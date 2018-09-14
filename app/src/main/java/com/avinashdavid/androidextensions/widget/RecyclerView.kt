package com.avinashdavid.androidextensions.widget

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

fun RecyclerView.setupLinearVertical(context: Context, reverseLayout: Boolean = false) : LinearLayoutManager {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, reverseLayout)
    this.layoutManager = layoutManager
    return layoutManager
}

fun RecyclerView.setupLinearHorizontal(context: Context, reverseLayout: Boolean = false) : LinearLayoutManager {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, reverseLayout)
    this.layoutManager = layoutManager
    return layoutManager
}

fun RecyclerView.addDividerBetweenItems(layoutManager: LinearLayoutManager) {
    val dividerItemDecoration = DividerItemDecoration(this.context,
            layoutManager.orientation)
    addItemDecoration(dividerItemDecoration)
}

abstract class UnlimitedScrollListener(private val linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    var isFetching: Boolean = false
    var hasReachedEndOfPage: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView?.childCount ?: 0
        val totalItemCount = recyclerView?.adapter?.itemCount ?: 0
        val pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition()

        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount
                && !isFetching && !hasReachedEndOfPage) {
            loadMoreItems()
        }
    }

    /**
     * @see isFetching
     * @see hasReachedEndOfPage
     * @since this method triggers when the last item in the recyclerview is visible && !isFetching && !hasReachedEndOfPage, make sure to set isFetching when starting and finishing fetching items, and hasReachedEndOfPage when there are no more items to load
     */
    abstract fun loadMoreItems()
}