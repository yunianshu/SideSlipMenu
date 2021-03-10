package com.sideslip.view;

/**
 * created by wyu on 2021/3/7.
 */



 class SlideItem {
    int itemLayoutId;
    int leftMenuLayoutId;
    float leftMenuRatio;
    int rightMenuLayoutId;
    float rightMenuRatio;

    SlideItem(int itemLayoutId, int leftMenuLayoutId, float leftMenuRatio, int rightMenuLayoutId, float rightMenuRatio) {
        this.itemLayoutId = itemLayoutId;
        this.leftMenuLayoutId = leftMenuLayoutId;
        this.leftMenuRatio = leftMenuRatio;
        this.rightMenuLayoutId = rightMenuLayoutId;
        this.rightMenuRatio = rightMenuRatio;
    }
}
