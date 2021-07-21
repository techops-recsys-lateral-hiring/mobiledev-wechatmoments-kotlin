package com.tws.moments

import android.app.Application
import com.tws.moments.imageloader.GlideImageLoader
import com.tws.moments.imageloader.ImageLoader
import com.tws.moments.utils.ScreenAdaptiveUtil

class TWApplication : Application() {

    companion object {
        lateinit var app: Application
        lateinit var imageLoader: ImageLoader
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        ScreenAdaptiveUtil.adaptive(this)

        initImageLoader()
    }

    private fun initImageLoader() {
        imageLoader = GlideImageLoader(this)
    }
}
