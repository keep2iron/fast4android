package io.github.keep2iron.app.valyout

import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import io.github.keep2iron.android.Fast4Android
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.android.utilities.ToastUtil
import io.github.keep2iron.app.R
import io.github.keep2iron.app.ui.LoopActivity
import io.github.keep2iron.app.util.BannerLayoutManager

class LoopVerticalAdapter : AbstractSubAdapter() {

    init {
        setOnItemClickListener {
            ToastUtil.S("测试 ${it}")
        }
    }

    override fun getLayoutId(): Int = R.layout.item_loop_vertical

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
        val mRecyclerView = holder.findViewById<RecyclerView>(R.id.recyclerView)

        val mLayoutManager = BannerLayoutManager(Fast4Android.CONTEXT, BannerLayoutManager.VERTICAL)
        mLayoutManager.setItemSpace(0)
        mLayoutManager.setCenterScale(1f)
        mLayoutManager.setMoveSpeed(1f)
        mRecyclerView.layoutManager = mLayoutManager

        mRecyclerView.adapter = object : RecyclerView.Adapter<LoopActivity.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): LoopActivity.ViewHolder {
                val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_child, parent, false)
                return LoopActivity.ViewHolder(layout)
            }

            override fun getItemCount(): Int = 4

            override fun onBindViewHolder(viewHolder: LoopActivity.ViewHolder, p1: Int) {
                val text = viewHolder.itemView.findViewById<TextView>(R.id.textView)
                text.setBackgroundColor(ContextCompat.getColor(text.context.applicationContext, arrayOf(R.color.colorPrimary, R.color.blue, R.color.deep_purple)[(Math.random() * 3).toInt()]))
                text.text = "postion : ${p1}"
            }
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mRecyclerView)

        val handler = Handler()

        val runnable = object : Runnable {
            override fun run() {
                mRecyclerView.smoothScrollToPosition(mLayoutManager.currentPosition + 1)
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
        return holder
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 1
}