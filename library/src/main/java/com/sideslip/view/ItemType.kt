package com.sideslip.view

import com.sideslip.face.IItemType

/**
 * created by wyu on 2021/3/6.
 */
abstract class ItemType<T> : IItemType {
    override fun type(data: Any, position: Int): Int {
        return getItemOrder(data as T, position)
    }

    abstract fun getItemOrder(data: T, position: Int): Int
}