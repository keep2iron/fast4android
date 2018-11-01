package io.github.keep2iron.app.valyout

import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.CheckBox
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.android.databinding.RecyclerViewChangeAdapter
import io.github.keep2iron.app.R
import java.lang.IllegalArgumentException

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/31
 */
data class Item(val id: String,
                val parentId: String?,
                var expend: Boolean = false,
                var checked: Boolean = false,
                val childes: List<Item>?)

class ExpandListAdapter(items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = ObservableArrayList<Item>()

    init {
        this.data.addAll(items)
        data.addOnListChangedCallback(RecyclerViewChangeAdapter<Item>(this))
    }

    companion object {
        const val TYPE_PARENT = 0
        const val TYPE_CHILD = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PARENT -> {
                RecyclerViewHolder(parent.context, R.layout.item_parent, parent)
            }
            TYPE_CHILD -> {
                RecyclerViewHolder(parent.context, R.layout.item_child, parent)
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        when (itemViewType) {
            TYPE_PARENT -> {
                bindParent(holder, position)
            }
            TYPE_CHILD -> {
                bindChild(holder, position)
            }
        }
    }

    private fun bindChild(holder: RecyclerView.ViewHolder, position: Int) {
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.checkbox)
        val item = data[position]
        checkBox.tag = item
        checkBox.isChecked = item.checked
        checkBox.setOnClickListener {
            val item = it.tag as Item
            item.checked = !item.checked

            data.filter {
                item.parentId != null && item.parentId != it.parentId && item.checked
            }.forEach {
                it.checked = false
            }

            //设置父级为true
            val parentItem = data.first {
                it.parentId == null && it.id == item.parentId
            }

            parentItem.childes?.apply {
                val checkCount = this.count {
                    it.checked
                }
                parentItem?.checked = checkCount == parentItem.childes!!.count()
                notifyDataSetChanged()
            }
        }
    }

    private fun bindParent(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tag = data[position]
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.checkbox)
        val item = data[position]
        checkBox.tag = item
        checkBox.isChecked = item.checked
        holder.itemView.setOnClickListener {
            val item = holder.itemView.tag as Item
            item.expend = !item.expend
            expand(item.id, item.expend)
        }
        checkBox.setOnClickListener {
            val item = it.tag as Item
            item.checked = !item.checked

            item.childes?.forEach {
                it.checked = item.checked
            }

            // 将其他的父级下面的子层级设置为false
            data.filter {
                it.parentId == null && it.id != item.id
            }.flatMap {
                it.checked = false
                return@flatMap it.childes ?: emptyList()
            }.forEach {
                it.checked = false
            }

            notifyDataSetChanged()
        }
    }

    fun expand(id: String, expand: Boolean) {
        val index = this.data.indexOfFirst {
            return@indexOfFirst it.id == id
        }

        val childes = data[index].childes
        childes?.apply {
            if (expand) {
                data.addAll(index + 1, childes)
            } else {
                childes.forEach {
                    data.remove(it)
                }
            }

            childes.asSequence().forEach {
                it.expend = expand
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return if (item.parentId == null) {
            TYPE_PARENT
        } else {
            TYPE_CHILD
        }
    }
}