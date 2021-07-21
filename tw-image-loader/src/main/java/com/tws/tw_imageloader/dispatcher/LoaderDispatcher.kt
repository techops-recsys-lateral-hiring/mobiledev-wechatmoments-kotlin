package com.tws.tw_imageloader.dispatcher

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.WorkerThread
import com.tws.tw_imageloader.ImageLoader
import com.tws.tw_imageloader.cache.DiskCache
import com.tws.tw_imageloader.cache.MemoryCache
import com.tws.tw_imageloader.download.cacheToDiskFromServer
import com.tws.tw_imageloader.download.downloadBitmapFromHttp
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

internal object LoaderDispatcher {
    private const val TAG = "LoaderDispatcher--"

    private val memoryCache = MemoryCache(ImageLoader.imageLoaderConfig.memoryCacheMaxSize)
    private val diskCache = DiskCache(ImageLoader.imageLoaderConfig.diskCacheMaxSize)

    private val CORE_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = CORE_COUNT + 1
    private val MAXIMUM_POOL_SIZE = CORE_COUNT * 2 + 1
    private const val KEEP_ALIVE = 10L

    /**
     * The Handler Switch task to UI thread
     */
    private val handler = Handler(Looper.getMainLooper())

    private val sThreadFactory = object : ThreadFactory {
        private val count = AtomicInteger(1)
        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "TWImageLoader#${count.getAndIncrement()}")
        }
    }

    private val executor = ThreadPoolExecutor(
        CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
        TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>(), sThreadFactory
    )

    /**
     * Asynchronous loading.
     * @param url request url
     * @param reqWidth the width ImageViw desired
     * @param reqHeight the height ImageView desired
     */
    fun loadBitmapAsync(
        url: String, reqWidth: Int, reqHeight: Int,
        onNext: (Bitmap?) -> Unit
    ) {
        // this step is necessary.reduce the number of tasks.
        val memoryBitmap = memoryCache.get(url)
        if (memoryBitmap != null) {
            Log.i(TAG, "memory cache.")
            handler.post {
                onNext.invoke(memoryBitmap)
            }
            return
        }

        val loadBitmapRunnable = Runnable {
            val bitmap = loadBitmap(url, reqWidth, reqHeight)
            handler.post {
                onNext.invoke(bitmap)
            }
        }
        executor.execute(loadBitmapRunnable)
    }

    /**
     * Synchronous loading.
     * @param url request url
     * @param reqWidth the width ImageViw desired
     * @param reqHeight the height ImageView desired
     */
    @WorkerThread
    fun loadBitmap(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw RuntimeException("load image should not from UI Thread.")
        }
        // load from memory
        var bitmap = memoryCache.get(url)
        if (bitmap != null) {
            Log.i(TAG, "memory cache.")
            return bitmap
        }

        // load from disk
        bitmap = diskCache.get(url, reqWidth, reqHeight)
        if (bitmap != null) {
            memoryCache.put(url, bitmap)
            Log.i(TAG, "disk cache.")
            return bitmap
        }

        // download and cache to disk
        val editor = diskCache.edit(url) ?: return downloadBitmapFromHttp(url).also {
            Log.i(TAG, "download form http.")
            memoryCache.put(url, it)
        }
        Log.i(TAG, "begin cacheToDiskFromServer.")
        val cacheSuccess = cacheToDiskFromServer(url, editor)
        diskCache.flush()

        return if (cacheSuccess) {
            Log.i(TAG, "cacheToDiskFromServer success.")
            diskCache.get(url, reqWidth, reqHeight).also {
                memoryCache.put(url, it)
            }
        } else {
            Log.i(TAG, "cacheToDiskFromServer failed.")
            downloadBitmapFromHttp(url).also {
                Log.i(TAG, "download form http.")
                memoryCache.put(url, it)
            }
        }
    }


}
