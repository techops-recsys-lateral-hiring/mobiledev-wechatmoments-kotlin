package com.tws.tw_imageloader.download

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import androidx.annotation.WorkerThread
import com.jakewharton.disklrucache.DiskLruCache
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

private const val IO_BUFFER_SIZE = 8 * 1024

@WorkerThread
@Throws(IOException::class)
fun cacheToDiskFromServer(url: String, editor: DiskLruCache.Editor): Boolean {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        throw RuntimeException("download image should not from UI Thread.")
    }
    val outputStream = editor.newOutputStream(0)
    return if (downloadUrlToStream(url, outputStream)) {
        editor.commit()
        true
    } else {
        editor.abort()
        false
    }
}

@WorkerThread
private fun downloadUrlToStream(url: String, outputStream: OutputStream): Boolean {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        throw RuntimeException("download image should not from UI Thread.")
    }
    var urlConnection: HttpURLConnection? = null
    var out: BufferedOutputStream? = null
    var ins: BufferedInputStream? = null
    try {
        urlConnection = URL(url).openConnection() as HttpURLConnection
        ins = BufferedInputStream(urlConnection.inputStream, IO_BUFFER_SIZE)
        out = BufferedOutputStream(outputStream, IO_BUFFER_SIZE)

        var b: Int
        while (ins.read().also { b = it } != -1) {
            out.write(b)
        }
        return true
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        urlConnection?.disconnect()
        try {
            out?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            ins?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return false
}

/**
 * Download and decode from server.
 */
fun downloadBitmapFromHttp(url: String): Bitmap? {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        throw RuntimeException("download image should not from UI Thread.")
    }
    var bitmap: Bitmap? = null
    var urlConnection: HttpURLConnection? = null
    var ins: BufferedInputStream? = null
    try {
        urlConnection = URL(url).openConnection() as HttpURLConnection
        ins = BufferedInputStream(urlConnection.inputStream, IO_BUFFER_SIZE)
        bitmap = BitmapFactory.decodeStream(ins)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        urlConnection?.disconnect()
        try {
            ins?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return bitmap
}
