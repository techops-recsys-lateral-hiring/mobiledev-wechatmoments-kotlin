package com.tws.tw_imageloader.cache

import android.graphics.Bitmap
import android.util.LruCache

class MemoryCache(private val maxSize: Int) {

    private val lruCache: LruCache<String, Bitmap> by lazy {
        object : LruCache<String, Bitmap>(maxSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return if (value != null) {
                    value.rowBytes * value.height / 1024 //kb
                } else {
                    super.sizeOf(key, value)
                }
            }
        }
    }

    fun get(url: String): Bitmap? {
        return lruCache.get(url)
    }

    fun put(url: String, bitmap: Bitmap?) {
        if (bitmap == null) return
        lruCache.put(url, bitmap)
    }
}
