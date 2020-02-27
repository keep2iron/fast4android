package io.github.keep2iron.fast4android.app.ui.tip

import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.base.util.translucent
import io.github.keep2iron.fast4android.core.widget.FastDialog
import io.github.keep2iron.fast4android.core.widget.FastTipDialogBuilder
import io.github.keep2iron.fast4android.core.widget.Tip
import io.github.keep2iron.fast4android.topbar.FastTopBarLayout
import io.github.keep2iron.pomelo.pager.adapter.AbstractSubListAdapter
import io.github.keep2iron.pomelo.pager.adapter.RecyclerViewHolder

data class TipDialogType(val tipName: String, val tipType: Tip)

class ListAdapter(data: ObservableList<TipDialogType>) : AbstractSubListAdapter<TipDialogType>(data) {

  override fun onInflateLayoutId(parent: ViewGroup, viewType: Int): Int = R.layout.common_list_item

  override fun render(holder: RecyclerViewHolder, item: TipDialogType, position: Int) {
    holder.setText(R.id.tvListItem, item.tipName)
  }
}

class TipDialogComponentActivity : AbstractActivity<ViewDataBinding>() {

  private val recyclerView: RecyclerView by findViewByDelegate(R.id.recyclerView)

  private val topBarLayout: FastTopBarLayout by findViewByDelegate(R.id.topBarLayout)

  override fun resId(): Int = R.layout.tip_dialog_comp_activity

  var fastDialog: FastDialog? = null

  override fun initVariables(savedInstanceState: Bundle?) {
    translucent()

    topBarLayout.setup {
      title = "Tip Dialog Component"
      addLeftBackImageButton().setOnClickListener {
        finish()
      }
    }

    val observableList = ObservableArrayList<TipDialogType>()
    observableList.addAll(listOf(
      TipDialogType("Loading Tip Dialog", Tip.LOADING),
      TipDialogType("Nothing Tip Dialog", Tip.NOTHING),
      TipDialogType("Successful Tip Dialog", Tip.SUCCESSFUL),
      TipDialogType("Fail Tip Dialog", Tip.FAIL),
      TipDialogType("Info Tip Dialog", Tip.INFO)
    ))

    val adapter = ListAdapter(observableList)
    adapter.setOnItemClickListener { position, view, itemView ->
      val tipDialogType = observableList[position]
      when (tipDialogType.tipType) {
        Tip.NOTHING -> {
          fastDialog = FastTipDialogBuilder(this) {
            iconType = tipDialogType.tipType
            tipContent = "单独文本"
          }.build()
        }
        Tip.LOADING -> {
          fastDialog = FastTipDialogBuilder(this) {
            iconType = tipDialogType.tipType
            tipContent = "正在加载"
          }.build()
        }
        Tip.SUCCESSFUL -> {
          fastDialog = FastTipDialogBuilder(this) {
            iconType = tipDialogType.tipType
            tipContent = "成功"
          }.build()
        }
        Tip.FAIL -> {
          fastDialog = FastTipDialogBuilder(this) {
            iconType = tipDialogType.tipType
            tipContent = "失败"
          }.build()
        }
        Tip.INFO -> {
          fastDialog = FastTipDialogBuilder(this) {
            iconType = tipDialogType.tipType
            tipContent = "信息"
          }.build()
        }
      }
      fastDialog?.show()
      topBarLayout.postDelayed({
        fastDialog?.dismiss()
      }, 2000)
    }
    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    recyclerView.adapter = adapter
  }
}