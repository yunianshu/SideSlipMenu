package com.sideslip.view;

/**
 * created by wyu on 2021/3/6.
 */


class NormalItem {
    NormalItem(int layoutId, float heightRatio) {
        this.layoutId = layoutId;
        this.heightRatio = heightRatio;
    }

    int layoutId;
    float heightRatio;  // 占屏幕高度比例
}