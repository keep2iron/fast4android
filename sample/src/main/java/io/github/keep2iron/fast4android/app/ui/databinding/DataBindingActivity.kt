package io.github.keep2iron.fast4android.app.ui.databinding

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.DatabindingItemBinding
import io.github.keep2iron.fast4android.app.databinding.DatabindingNestedItemBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.base.util.FastStatusBarHelper
import io.github.keep2iron.fast4android.base.util.setPaddingBottom
import io.github.keep2iron.fast4android.topbar.FastTopBarLayout
import io.github.keep2iron.pomelo.pager.adapter.AbstractSubListAdapter
import io.github.keep2iron.pomelo.pager.adapter.RecyclerViewHolder

data class Item(val nestedList: ArrayList<NestedItem> = ArrayList()) {

  var totalCount: ObservableInt

  init {
    for (i in 0 until (Math.random() * 9 + 1).toInt()) {
      nestedList.add(NestedItem())
    }

    val array = nestedList.map {
      it.count
    }.toTypedArray()

    totalCount = ObservableInt(*array)
    totalCount.set(
      nestedList.map { it.count.get() }
        .reduce { acc, i -> acc + i })

    totalCount.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        val total = nestedList.map { it.count.get() }.reduce { acc, i -> acc + i }
        totalCount.set(total)
      }
    })
  }
}

data class NestedItem(val count: ObservableInt = ObservableInt((Math.random() * 30).toInt())) {
}

class DataBindingAdapter(data: ObservableList<Item>) : AbstractSubListAdapter<Item>(data), View.OnClickListener {

  override fun onClick(v: View) {
    val position = v.tag as Int
    val nestedPosition = v.getTag(R.id.nestedRootLayout) as Int

    val nestedItem = data[position].nestedList[nestedPosition]
    when (v.id) {
      R.id.add -> {
        nestedItem.count.set(nestedItem.count.get() + 1)
      }
      R.id.subtract -> {
        nestedItem.count.set(nestedItem.count.get() - 1)
      }
    }
  }

  override fun onInflateLayoutId(parent: ViewGroup, viewType: Int): Int {
    return R.layout.databinding_item
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
    val holder = super.onCreateViewHolder(parent, viewType)
    DatabindingItemBinding.bind(holder.itemView)
    return holder
  }


  override fun render(holder: RecyclerViewHolder, item: Item, position: Int) {
    val itemBinding = DataBindingUtil.findBinding<DatabindingItemBinding>(holder.itemView)!!
    itemBinding.root.tag = position
    itemBinding.item = item

    itemBinding.nestedLayout.removeAllViews()
    item.nestedList.forEachIndexed { index, nestedItem ->
      val nestedItemBinding = DataBindingUtil.inflate<DatabindingNestedItemBinding>(
        LayoutInflater.from(holder.itemView.context),
        R.layout.databinding_nested_item,
        itemBinding.nestedLayout,
        false)
      nestedItemBinding.item = nestedItem
      nestedItemBinding.add.tag = position
      nestedItemBinding.add.setTag(R.id.nestedRootLayout, index)
      nestedItemBinding.subtract.tag = position
      nestedItemBinding.subtract.setTag(R.id.nestedRootLayout, index)

      nestedItemBinding.editText.onFocusChangeListener = View.OnFocusChangeListener { p0, focus ->
        if(focus){
          nestedItemBinding.editText.hint = "请输入你的姓名"
        }else {
          nestedItemBinding.editText.hint = ""
        }
      }
      nestedItemBinding.listener = this
//            nestedItemBinding.executePendingBindings()

      itemBinding.nestedLayout.addView(nestedItemBinding.root)
    }

    itemBinding.executePendingBindings()
  }

}

@ParallaxBack
class DataBindingActivity : AbstractActivity<ViewDataBinding>() {

  override fun resId(): Int = R.layout.databinding_activity

  override fun initVariables(savedInstanceState: Bundle?) {
    FastStatusBarHelper.translucent(this)

    findViewById<FastTopBarLayout>(R.id.titleBarLayout).setup {
      addLeftBackImageButton().setOnClickListener { finish() }
    }

    val recylcerView = findViewById<RecyclerView>(R.id.recyclerView)
    val data = ObservableArrayList<Item>()

    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
    recyclerView.adapter = DataBindingAdapter(data)
    recyclerView.layoutManager = LinearLayoutManager(applicationContext)

    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())
    data.add(Item())

//    ViewCompat.setOnApplyWindowInsetsListener(recylcerView) { _, insets ->
//      recylcerView.setPaddingBottom(insets.systemWindowInsetBottom)
//
//      insets
//    }
  }
}