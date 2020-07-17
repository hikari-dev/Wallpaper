package dev.hikari.wallpaper.utils

import android.content.res.Resources

object DensityUtils {

    fun dp2px(dpValue: Float): Int {
        return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun px2dp(pxValue: Int): Float {
        return pxValue / Resources.getSystem().displayMetrics.density
    }

}