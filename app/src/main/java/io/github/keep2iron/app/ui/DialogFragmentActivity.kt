package io.github.keep2iron.app.ui

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.widget.Button
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.ext.FindViewById
import io.github.keep2iron.app.R

/**
 * 用于测试dialog fragment
 */
class DialogFragmentActivity : AbstractActivity<ViewDataBinding>() {

    private val btnTestTopDialogFragment: Button by FindViewById(R.id.btnTestTopDialogFragment)
    private val btnTestCenterDialogFragment: Button by FindViewById(R.id.btnTestCenterDialogFragment)
    private val btnTestLeftDialogFragment: Button by FindViewById(R.id.btnTestLeftDialogFragment)
    private val btnTestRightDialogFragment: Button by FindViewById(R.id.btnTestRightDialogFragment)


    override fun resId(): Int = R.layout.activity_dialog

    override fun initVariables(savedInstanceState: Bundle?) {
    }
}