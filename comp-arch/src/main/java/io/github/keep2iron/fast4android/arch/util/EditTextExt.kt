package io.github.keep2iron.fast4android.arch.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import io.github.keep2iron.base.Fast4Android

fun EditText.closeKeyword() {
    clearFocus()
    val imm = Fast4Android.CONTEXT.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun EditText.openKeyword(){
    requestFocus()
    val imm = Fast4Android.CONTEXT.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}