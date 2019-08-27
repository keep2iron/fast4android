package io.github.keep2iron.fast4android.app.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.ui.roundbutton.RoundButtonFragment
import io.github.keep2iron.fast4android.arch.AbstractFragment
import io.github.keep2iron.fast4android.arch.FindViewById
import io.github.keep2iron.fast4android.core.util.layoutInflate

class HomeFragment : AbstractFragment<ViewDataBinding>() {

  private val recyclerView: RecyclerView by FindViewById(R.id.recyclerView)

  override fun resId(): Int = R.layout.home_fragment

  private val items = listOf(
    Description("RoundButton", R.mipmap.icon_grid_button, RoundButtonFragment::class.java)
//    Description("TabSegment", R.mipmap.icon_grid_tab_segment, TabSegmentActivity::class.java)
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
          fragmentManager.beginTransaction()
            .replace(R.id.container, RoundButtonFragment())
            .commit()
//                    startActivity(data[viewHolder.layoutPosition].clazz)
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
    val clazz: Class<out Fragment>
  )
}