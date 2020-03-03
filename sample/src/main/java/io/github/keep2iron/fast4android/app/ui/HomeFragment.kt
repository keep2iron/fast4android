package io.github.keep2iron.fast4android.app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.ui.databinding.DataBindingActivity
import io.github.keep2iron.fast4android.app.ui.density.DensityActivity
import io.github.keep2iron.fast4android.app.ui.dialog.DialogComponentsActivity
import io.github.keep2iron.fast4android.app.ui.grouplistview.GroupListViewActivity
import io.github.keep2iron.fast4android.app.ui.layout.FastLayoutComponentActivity
import io.github.keep2iron.fast4android.app.ui.looplayout.LoopLayoutActivity
import io.github.keep2iron.fast4android.app.ui.roundbutton.RoundComponentsActivity
import io.github.keep2iron.fast4android.app.ui.tabsegment.TabSegmentListActivity
import io.github.keep2iron.fast4android.app.ui.tip.TipDialogComponentActivity
import io.github.keep2iron.fast4android.arch.AbstractFragment
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.base.util.layoutInflate

class HomeFragment : AbstractFragment<ViewDataBinding>() {
  private val recyclerView: RecyclerView by findViewByDelegate(R.id.recyclerView)
  //  private val titleBar: FastTopBarLayout by FindViewById(R.id.titleBar)
  override fun resId(): Int = R.layout.home_fragment

  private val items = listOf(
    Description("RoundComponents", R.mipmap.icon_grid_button, RoundComponentsActivity::class.java),
    Description("TabSegment", R.mipmap.icon_grid_tab_segment, TabSegmentListActivity::class.java),
    Description(
      "GroupListView",
      R.mipmap.icon_grid_group_list_view,
      GroupListViewActivity::class.java
    ),
    Description("Dialog", R.mipmap.icon_grid_dialog, DialogComponentsActivity::class.java),
    Description("DataBinding Bast Practice", R.mipmap.icon_grid_group_list_view, DataBindingActivity::class.java),
    Description("LoopLayout", R.mipmap.icon_grid_pager_layout_manager, LoopLayoutActivity::class.java),
    Description("TipDialog", R.mipmap.icon_grid_tip_dialog, TipDialogComponentActivity::class.java),
    Description("Shadow Border Layout", R.mipmap.icon_grid_layout, FastLayoutComponentActivity::class.java),
    Description("Density", R.mipmap.icon_grid_layout, DensityActivity::class.java)
  )

  override fun initVariables(savedInstanceState: Bundle?) {
    recyclerView.adapter = MainAdapter(items, requireFragmentManager())

    recyclerView.layoutManager = GridLayoutManager(requireContext().applicationContext, 3)
  }

  class MainAdapter(private val data: List<Description>, val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val context = parent.context
      return MyViewHolder(context.layoutInflate(R.layout.home_item_main, parent)).apply {
        val viewHolder = this
        itemView.setOnClickListener {
          val intent = Intent(context, data[viewHolder.layoutPosition].clazz)
          context.startActivity(intent)
        }
      }
    }

    override fun getItemCount(): Int = data.size
    override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item = data[position]
      holder.itemView.findViewById<ImageView>(R.id.item_icon).setImageResource(item.icon)
      holder.itemView.findViewById<TextView>(R.id.item_name).text = item.title
    }
  }

  class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
  class Description(
    val title: String,
    val icon: Int,
    val clazz: Class<out Activity>
  )
}