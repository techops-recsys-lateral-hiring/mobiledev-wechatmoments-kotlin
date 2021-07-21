package com.tws.moments.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics

object ScreenAdaptiveUtil {
    /**
     * Status bar height(px)
     */
    var sStatusBarHeight = 0

    /**
     * Screen width(px)
     */
    var sScreenWidth = 0

    /**
     * Screen height(px)
     */
    var sScreenHeight = 0

    /**
     * Density after modify.
     * px = dp * density [android.util.TypedValue.applyDimension]
     */
    var sDensity = 0f

    /**
     * ScaleDensity after modify.
     * px = sp * scaleDensity [android.util.TypedValue.applyDimension]
     */
    var sScaleDensity = 0f

    /**
     * DensityDpi after modify.
     */
    var sDensityDpi = 0

    /**
     * The design width ,unit is dp.
     */
    private const val DESIGN_WIDTH = 375f

    /**
     * Density before modify.
     */
    private var oriDensity = 0f

    /**
     * ScaleDensity before modify.
     */
    private var oriScaleDensity = 0f

    /**
     * Initial system parameters
     *
     * @param application Application
     */
    fun adaptive(application: Application) {
        val displayMetrics = application.resources.displayMetrics
        sStatusBarHeight = getStatusBarHeight(application)
        if (oriDensity == 0f) {
            sScreenWidth = displayMetrics.widthPixels
            sScreenHeight = displayMetrics.heightPixels

            // init
            sDensity = displayMetrics.density
            oriDensity = sDensity
            sScaleDensity = displayMetrics.scaledDensity
            oriScaleDensity = sScaleDensity
            sDensityDpi = displayMetrics.densityDpi
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig.fontScale > 0) {
                        oriScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
    }

    fun adaptive(activity: Activity) {
        // modify--
        sDensity = sScreenWidth / DESIGN_WIDTH
        sScaleDensity =
            sDensity * (oriScaleDensity / oriDensity)
        sDensityDpi = (sScaleDensity * 160).toInt()
        val displayMetrics = activity.resources.displayMetrics
        displayMetrics.density = sDensity
        displayMetrics.scaledDensity = sScaleDensity
        displayMetrics.densityDpi = sDensityDpi
    }

    /**
     * @param context context
     * @return The height of status bar
     */
    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
