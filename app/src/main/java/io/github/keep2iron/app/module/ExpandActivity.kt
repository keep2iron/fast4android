package io.github.keep2iron.app.module

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.ext.FindViewById
import io.github.keep2iron.app.R
import io.github.keep2iron.app.valyout.ExpandListAdapter
import io.github.keep2iron.app.valyout.Item

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/31
 */
class ExpandActivity(override val resId: Int = R.layout.activity_expand) : AbstractActivity<ViewDataBinding>() {
    private val recyclerView: RecyclerView by FindViewById(R.id.recyclerView)

    override fun initVariables(savedInstanceState: Bundle?) {
        val data = ArrayList<Item>()

        for (i in 0 until 10) {
            val childes = generateChilds(i.toString())
            val item = Item(i.toString(), null, false, false, childes)
            data.add(item)
        }

        recyclerView.adapter = ExpandListAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    fun generateChilds(parentId: String): List<Item> {
        val childes = ArrayList<Item>()

        for (i in 0 until 3) {
            childes.add(Item("$parentId-$i", parentId, false, false, null))
        }

        return childes
    }
}