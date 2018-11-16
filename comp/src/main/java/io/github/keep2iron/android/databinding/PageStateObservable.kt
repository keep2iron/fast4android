package io.github.keep2iron.android.databinding

import android.databinding.Observable
import io.github.keep2iron.android.widget.PageState

import io.github.keep2iron.android.widget.PageStateLayout

open class PageStateObservable(private val pageStateLayout: PageStateLayout,
                               val state: PageState = PageState.ORIGIN) : Observable {

    private val callbacks: ArrayList<Observable.OnPropertyChangedCallback> = ArrayList(3)

    init {
        if (state != PageState.ORIGIN) {
            pageStateLayout.initPageState(state)
        }
    }

    companion object {
        const val PAGE_STATE_PROP_ID = 100000
    }


    fun setPageState(pageState: PageState) {
        when (pageState) {
            PageState.ORIGIN -> {
                pageStateLayout.displayOriginView()
            }
            PageState.LOADING -> {
                pageStateLayout.displayLoading()
            }
            PageState.LOAD_ERROR -> {
                pageStateLayout.displayLoadError()
            }
            PageState.NO_DATA -> {
                pageStateLayout.displayNoData()
            }
            PageState.NO_NETWORK -> {
                pageStateLayout.displayNoNetwork()
            }
        }
        callbacks.forEach {
            it.onPropertyChanged(this@PageStateObservable, PAGE_STATE_PROP_ID)
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }
}
