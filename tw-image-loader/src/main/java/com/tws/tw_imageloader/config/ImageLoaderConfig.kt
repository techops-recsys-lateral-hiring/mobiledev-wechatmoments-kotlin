package com.tws.tw_imageloader.config

import androidx.annotation.DrawableRes

class ImageLoaderConfig private constructor(builder: Builder) {
    private val maxMemory = Runtime.getRuntime().maxMemory() / 1024 // unit:kb

    internal val memoryCacheMaxSize = (maxMemory / 8).toInt()
    internal val diskCacheMaxSize = 20 * 1024 * 1024 // 50MB

    @DrawableRes
    var loadingHolder: Int = builder.loadingHolder

    @DrawableRes
    var errorHolder: Int = builder.errorHolder

    class Builder() {
        @DrawableRes
        internal var loadingHolder: Int = 0

        @DrawableRes
        internal var errorHolder: Int = 0

        fun loadingHolder(@DrawableRes loadingHolder: Int) = apply { this.loadingHolder = loadingHolder }

        fun errorHolder(@DrawableRes errorHolder: Int) = apply { this.errorHolder = errorHolder }

        fun build() = ImageLoaderConfig(this)
    }
}
