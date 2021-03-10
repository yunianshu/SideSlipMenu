package com.sideslip.face;

import com.sideslip.view.ItemView;

/**
 * created by yhao on 2017/9/8.
 */


public interface IItemBind {
    void bind(ItemView holder, Object data, int position);
}
