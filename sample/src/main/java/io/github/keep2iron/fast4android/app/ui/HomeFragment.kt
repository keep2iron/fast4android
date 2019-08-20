package io.github.keep2iron.fast4android.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.arch.AbstractFragment
import io.github.keep2iron.fast4android.arch.FindViewById

class HomeFragment : AbstractFragment<ViewDataBinding>() {

  private val recyclerView: RecyclerView by FindViewById(R.id.recyclerView)

  override fun resId(): Int = R.layout.fragment_main

  val items = listOf(
    Description("RoundButton", R.mipmap.icon_grid_button)
  )

  override fun initVariables(container: View, savedInstanceState: Bundle?) {
    recyclerView.adapter = MainAdapter(items)
    recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
  }

  class MainAdapter(private val data: List<Description>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return MyViewHolder(
        LayoutInflater.from(parent.context).inflate(
          R.layout.home_item_main,
          parent,
          false
        )
      )
    }

    override fun getItemCount(): Int = data.size

    override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

  }

  class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  class Description(
    val title: String,
    val icon: Int
  )
}