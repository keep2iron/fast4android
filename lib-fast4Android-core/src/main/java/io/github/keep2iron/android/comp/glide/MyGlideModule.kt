package io.github.keep2iron.android.comp.glide

import android.content.Context
import android.os.Environment

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule

import java.io.File
import java.io.InputStream

/**
 * @author keep2iron
 */
@GlideModule
class MyGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context?, builder: GlideBuilder?) {
        //获取内存的默认配置
        //内存缓存相关,默认是24m
        // 20mb
        val memoryCacheSizeBytes = 1024 * 1024 * 20
        builder!!.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))

        //        设置磁盘缓存及其路径
        val MAX_CACHE_SIZE = 100 * 1024 * 1024
        val CACHE_FILE_NAME = "imgCache"
        builder.setDiskCache(ExternalCacheDiskCacheFactory(context, CACHE_FILE_NAME, MAX_CACHE_SIZE))
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val downloadDirectoryPath = Environment.getExternalStorageDirectory().absolutePath + "/" +
                    CACHE_FILE_NAME
            val file = File(downloadDirectoryPath)
            var isExit = false
            if (!file.exists()) {
                isExit = file.mkdirs()
            }
            if (isExit) {
                //路径---->sdcard/imgCache
                builder.setDiskCache(DiskLruCacheFactory(downloadDirectoryPath, MAX_CACHE_SIZE))
            } else {
                builder.setDiskCache(ExternalCacheDiskCacheFactory(context, CACHE_FILE_NAME, MAX_CACHE_SIZE))
            }
        } else {
            //路径---->/sdcard/Android/data/<application package>/cache/imgCache
            builder.setDiskCache(ExternalCacheDiskCacheFactory(context, CACHE_FILE_NAME, MAX_CACHE_SIZE))
        }
    }

    override fun registerComponents(context: Context?, glide: Glide?, registry: Registry?) {
        registry!!.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
    }

    override fun isManifestParsingEnabled(): Boolean {
        //不使用清单配置的方式,减少初始化时间
        return false
    }

}