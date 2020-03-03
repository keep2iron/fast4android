package io.github.keep2iron.density

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/3/2.
 *版本号：1.0

 */
class InitProvider : ContentProvider() {
  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    return null
  }

  override fun query(
    uri: Uri,
    projection: Array<out String>?,
    selection: String?,
    selectionArgs: Array<out String>?,
    sortOrder: String?
  ): Cursor? {
    return null
  }

  override fun onCreate(): Boolean {
    val application = if (context?.applicationContext == null) {
      context?.applicationContext
    } else {
      getApplicationByReflect()
    }
    DensityConfig.init(
      application = application as Application,
      isBaseOnWidth = true,
      strategy = DefaultDensityAdaptStrategy()
    )
      .setIsExcludeFontScale(true)
    return true
  }

  override fun update(
    uri: Uri,
    values: ContentValues?,
    selection: String?,
    selectionArgs: Array<out String>?
  ): Int {
    return 0
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
    return 0
  }

  override fun getType(uri: Uri): String? {
    return null
  }
}