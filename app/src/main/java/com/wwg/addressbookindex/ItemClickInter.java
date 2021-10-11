package com.wwg.addressbookindex;

import com.kongzue.baseokhttp.util.JsonMap;

public interface ItemClickInter {

    /**
     * 列表中的控件点击
     *
     * @param tag View标志
     * @param position 下标
     * @param map 附加信息
     */
    default void onViewClick(String tag, int position, JsonMap map) {
    }

    /**
     * 列表中的控件长按
     *
     * @param tag View标志
     * @param position 下标
     * @param map 附加信息
     */
    default void onViewLongClick(String tag, int position, JsonMap map) {
    }

}
