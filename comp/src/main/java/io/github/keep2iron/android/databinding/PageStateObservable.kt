package io.github.keep2iron.android.databinding

import android.databinding.Observable

import io.github.keep2iron.android.widget.PageStateLayout

enum class PageState {
    ORIGIN,
    NO_DATA,
    NO_NETWORK,
    LOAD_ERROR,
    LOADING,
}

open class PageStateObservable(private val pageStateLayout: PageStateLayout,
                          val pageState: PageState = PageState.ORIGIN) : Observable {

    companion object {
        const val PAGE_STATE_PROP_ID = 100000
    }

    private val callbacks = ArrayList<Observable.OnPropertyChangedCallback>(3)

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
