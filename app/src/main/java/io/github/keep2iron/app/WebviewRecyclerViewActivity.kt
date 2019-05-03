package io.github.keep2iron.app

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.ext.FindViewById
import org.w3c.dom.Document
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import android.widget.LinearLayout
import android.util.TypedValue
import android.view.ViewGroup
import android.webkit.WebView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import io.github.keep2iron.app.valyout.FooterAdapter
import io.github.keep2iron.app.valyout.HeaderAdapter
import io.github.keep2iron.app.valyout.WebViewAdapter

//import com.tencent.smtt.sdk.WebSettings
//import com.tencent.smtt.sdk.WebView
//import com.tencent.smtt.sdk.WebViewClient


/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/30
 */
class WebviewRecyclerViewActivity(override val resId: Int =  R.layout.activity_web_view_recycler) : AbstractActivity<ViewDataBinding>() {

    //    val webView: WebView by FindViewById(R.id.webView)
    val recyclerView: RecyclerView by FindViewById(R.id.recyclerView)

    override fun initVariables(savedInstanceState: Bundle?) {
        val virtualLayoutManager = VirtualLayoutManager(applicationContext)
        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 1)
        viewPool.setMaxRecycledViews(1, 1)
        viewPool.setMaxRecycledViews(2, 1)

        val delegateAdapter = DelegateAdapter(virtualLayoutManager, false)
        delegateAdapter.addAdapter(HeaderAdapter(applicationContext))
        delegateAdapter.addAdapter(WebViewAdapter(this, delegateAdapter))
        delegateAdapter.addAdapter(FooterAdapter(applicationContext))

        recyclerView.setRecycledViewPool(viewPool)
        recyclerView.layoutManager = virtualLayoutManager
        recyclerView.adapter = delegateAdapter
    }

}