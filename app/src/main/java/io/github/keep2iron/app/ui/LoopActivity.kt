package io.github.keep2iron.app.ui

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import io.github.keep2iron.android.ext.layout
import io.github.keep2iron.app.R
import io.github.keep2iron.app.util.BannerLayoutManager
import io.github.keep2iron.app.valyout.LoopVerticalAdapter

class LoopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop)

        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val virtualLayoutManager = VirtualLayoutManager(applicationContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager, true)
        delegateAdapter.addAdapter(LoopVerticalAdapter())
        mRecyclerView.adapter = delegateAdapter
        mRecyclerView.layoutManager = virtualLayoutManager
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}