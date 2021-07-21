package com.tws.moments.imageloader

import android.widget.ImageView

interface ImageLoader {
    fun displayImage(url: String?, imageView: ImageView)
}