package io.github.keep2iron.app.ui

import android.annotation.SuppressLint
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.android.collections.AsyncDiffObservableList
import io.github.keep2iron.android.collections.DiffObservableList
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.databinding.RecyclerViewChangeAdapter
import io.github.keep2iron.android.ext.FindViewById
import io.github.keep2iron.app.R
import java.util.*

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2019/1/6
 */
class DiffActivity : AbstractActivity<ViewDataBinding>() {
    private val recyclerView: RecyclerView by FindViewById(R.id.recyclerView)

    private val button: Button by FindViewById(R.id.button)

    override val resId: Int = R.layout.activity_diff

    override fun initVariables(savedInstanceState: Bundle?) {
        val data = AsyncDiffObservableList<String>(object : DiffUtil.ItemCallback<String>() {
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        })

        recyclerView.adapter = TestDiffAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        recyclerView.post {
            val list = ArrayList<String>()
            for (i in 0..10) {
                list.add(i.toString())
            }
            data.update(list)
        }

        button.setOnClickListener {
            val newList = data.toList()

            newList.removeAt(0)

            data.update(newList)
        }
    }

    class TestDiffAdapter(val data: ObservableList<String>) : AbstractSubAdapter() {
        init {
            data.addOnListChangedCallback(RecyclerViewChangeAdapter<String>(this))
        }

        override fun getLayoutId(): Int = R.layout.layout_test_diff

        override fun render(holder: RecyclerViewHolder, position: Int) {
            holder.findViewById<TextView>(R.id.textView).text = data[position]
        }

        override fun getItemCount(): Int = data.size
    }
}