package com.sideslip.face

import com.sideslip.view.ItemView

/**
 * created by wyu on 2021/3/6.
 */
interface HeaderBind {
    fun onBind(header: ItemView, order: Int)
}