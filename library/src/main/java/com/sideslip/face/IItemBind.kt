package com.sideslip.face

import com.sideslip.view.ItemView

/**
 * created by wyu on 2021/3/6.
 */
interface IItemBind {
    fun bind(holder: ItemView, data: Any, position: Int)
}