package com.wwg.addressnook_index;

import com.kongzue.baseokhttp.util.JsonMap;

/**
 * 通讯录索引View交互回调
 */
public interface IndexCallBack {

    /**
     * 选中索引回调
     *
     * @param position 选中下标
     */
    void onSelectIndexChange(int position);

}
