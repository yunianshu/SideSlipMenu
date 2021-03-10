package com.sideslip.view;

import com.sideslip.face.IItemType;


/**
 * created by wyu on 2021/3/6.
 */



public abstract class ItemType<T> implements IItemType {


    @Override
    public final int type(Object data, int position) {
        return getItemOrder((T) data, position);
    }

    public abstract int getItemOrder(T data,int position);
}
