package com.sideslip.view;

import com.sideslip.face.IItemBind;

/**
 * created by wyu on 2021/3/6.
 */


public abstract class ItemBind<T> implements IItemBind {


    @Override
    public final void bind(ItemView itemView, Object data, int position) {
        onBind(itemView, (T) data, position);
    }


    public abstract void onBind(ItemView itemView, T data, int position);
}
