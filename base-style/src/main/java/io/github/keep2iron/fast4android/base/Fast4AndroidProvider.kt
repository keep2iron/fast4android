package io.github.keep2iron.fast4android.base

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class Fast4AndroidProvider : ContentProvider() {
  override fun insert(uri: Uri, values: ContentValues?): Uri? = null

  override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null

  override fun onCreate(): Boolean {
    Fast4Android.init(context ?: throw IllegalArgumentException("context is null!"))
    return true
  }

  override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

  override fun getType(uri: Uri): String? = null
}