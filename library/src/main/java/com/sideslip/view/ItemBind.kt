package com.sideslip.view

import com.sideslip.face.IItemBind

/**
 * created by wyu on 2021/3/6.
 */
abstract class ItemBind<T> : IItemBind {
    override fun bind(itemView: ItemView, data: Any, position: Int) {
        onBind(itemView, data as T, position)
    }

    abstract fun onBind(itemView: ItemView, data: T, position: Int)
}