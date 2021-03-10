package com.sideslip.face;

import com.sideslip.view.ItemView;


/**
 * created by wyu on 2021/3/6.
 */


public interface IItemBind {
    void bind(ItemView holder, Object data, int position);
}
