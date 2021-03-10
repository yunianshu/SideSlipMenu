package com.sideslip.face

import com.sideslip.view.ItemView
import com.sideslip.view.SlideAdapter

/**
 * created by wyu on 2021/3/6.
 */
interface BottomListener {
    fun onBottom(footer: ItemView, slideAdapter: SlideAdapter)
}