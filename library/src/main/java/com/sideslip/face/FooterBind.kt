package com.sideslip.face

import com.sideslip.view.ItemView

/**
 * created by wyu on 2021/3/6.
 */
interface FooterBind {
    fun onBind(footer: ItemView, order: Int)
}