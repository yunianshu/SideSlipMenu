package com.sideslip.view

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * created by wyu on 2021/3/6.
 */
internal object ScreenSize {
    private var width = 0
    private var height = 0

    @JvmStatic
    fun w(context: Context): Int {
        if (width == 0) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(outMetrics)
            width = outMetrics.widthPixels
        }
        return width
    }

    @JvmStatic
    fun h(context: Context): Int {
        if (height == 0) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(outMetrics)
            height = outMetrics.heightPixels
        }
        return height
    }
}