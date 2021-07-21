package com.tws.tw_imageloader.cache

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Looper
import androidx.annotation.WorkerThread
import com.jakewharton.disklrucache.DiskLruCache
import com.tws.tw_imageloader.ImageLoader
import com.tws.tw_imageloader.resizer.decodeSampleBitmapFromFileDescriptor
import com.tws.tw_imageloader.utils.hashKeyForDisk
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class DiskCache(private val maxSize: Int) {
    private var diskLruCache: DiskLruCache? = null

    init {
        try {
            val cacheDir = getDiskCacheDir()
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(), 1, maxSize.toLong());
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun edit(url: String): DiskLruCache.Editor? {
        return diskLruCache?.edit(url.hashKeyForDisk())
    }

    fun flush(){
        diskLruCache?.flush()
    }

    @WorkerThread
    fun get(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw RuntimeException("get bitmap in disk should not from UI Thread.")
        }
        if (diskLruCache == null) return null
        val snapShot = diskLruCache!!.get(url.hashKeyForDisk()) ?: return null
        val fileInputStream = snapShot.getInputStream(0) as FileInputStream
        return fileInputStream.fd.decodeSampleBitmapFromFileDescriptor(reqWidth, reqHeight)
    }

    private fun getDiskCacheDir(): File {
        return File(ImageLoader.application.cacheDir.absolutePath + File.separator + "bitmap")
    }

    private fun getAppVersion(): Int {
        try {
            val info = ImageLoader.application.packageManager.getPackageInfo(ImageLoader.application.packageName, 0);
            return info.versionCode;
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }
        return 1
    }
}
