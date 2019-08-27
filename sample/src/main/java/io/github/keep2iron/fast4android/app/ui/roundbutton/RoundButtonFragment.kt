package io.github.keep2iron.fast4android.app.ui.roundbutton

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.RoundButtonActivityBinding
import io.github.keep2iron.fast4android.app.ui.HomeFragment
import io.github.keep2iron.fast4android.arch.AbstractFragment

class RoundButtonFragment : AbstractFragment<RoundButtonActivityBinding>(), OnClickListener {

  override fun resId(): Int = R.layout.round_button_activity

  override fun initVariables(savedInstanceState: Bundle?) {
//    dataBinding.tvRightAlphaTextView.setOnClickListener(this)
    dataBinding.tvRadiusTextView.setOnClickListener(this)
  }

  override fun onClick(v: View) {
//    startActivity(MainActivity::class.java)
    requireFragmentManager().beginTransaction()
      .replace(R.id.container, HomeFragment())
      .commit()
  }
}