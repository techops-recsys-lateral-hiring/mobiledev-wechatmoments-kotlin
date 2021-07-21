package com.tws.tw_imageloader

import android.app.Application
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tws.tw_imageloader.config.ImageLoaderConfig
import com.tws.tw_imageloader.dispatcher.LoaderDispatcher

object ImageLoader {

    internal lateinit var application: Application
    private var initialize: Boolean = false
    var imageLoaderConfig: ImageLoaderConfig = ImageLoaderConfig.Builder().build()

    @JvmStatic
    fun init(application: Application) {
        if (initialize) return
        this.application = application
        initialize = true
    }

    @JvmStatic
    fun config(config: ImageLoaderConfig) {
        imageLoaderConfig = config
    }

    /**
     * @param url request url
     * @param imageView target ImageView
     * @param loadingHolder image source displayed during loading
     * @param errorHolder image source displayed when loaded error.
     * @param reqMaxWidth target ImageView max width
     * @param reqMaxHeight target ImageView max height
     */
    @JvmStatic
    fun displayImage(
        url: String?, imageView: ImageView,
        @DrawableRes loadingHolder: Int = 0,
        @DrawableRes errorHolder: Int = 0,
        reqMaxWidth: Int = 0,
        reqMaxHeight: Int = 0
    ) {
        if (!initialize) {
            throw Exception("You must call init(application: Application) method first.")
        }

        if (url.isNullOrBlank()) {
            // error holder
            if (errorHolder != 0) {
                imageView.setImageResource(errorHolder)
            } else if (imageLoaderConfig.errorHolder != 0) {
                imageView.setImageResource(imageLoaderConfig.errorHolder)
            }
            return
        }

        // loading holder
        if (loadingHolder != 0) {
            imageView.setImageResource(loadingHolder)
        } else if (imageLoaderConfig.loadingHolder != 0) {
            imageView.setImageResource(imageLoaderConfig.loadingHolder)
        }

        // calc ImageView width and height,ignore match parent and wrap content temporarily.
        var reqWidth = 0
        var reqHeight = 0
        if (imageView.layoutParams != null) {
            reqWidth = imageView.layoutParams.width.coerceAtLeast(0)
            reqHeight = imageView.layoutParams.height.coerceAtLeast(0)
        }
        if (reqHeight == 0 || reqWidth == 0) {
            reqWidth = reqMaxWidth
            reqHeight = reqMaxHeight
        }

        LoaderDispatcher.loadBitmapAsync(url, reqWidth, reqHeight) {
            if (it != null) {
                imageView.setImageBitmap(it)
            } else {
                // error holder
                if (errorHolder != 0) {
                    imageView.setImageResource(errorHolder)
                } else if (imageLoaderConfig.errorHolder != 0) {
                    imageView.setImageResource(imageLoaderConfig.errorHolder)
                }
            }
        }

    }


}
