package com.wwg.addressnook_index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.baseokhttp.util.JsonBean;
import com.kongzue.baseokhttp.util.JsonList;
import com.kongzue.baseokhttp.util.JsonMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseAddressBook_Adapter extends RecyclerView.Adapter {

    /**
     * 数据转换,根据设定字段的拼音进行分组
     * @param list 应用数据源
     * @param groupTag 要分组的字段名
     * @param groupNames 用于存储转换后的分组名称列表
     * @return 转换后的显示数据
     */
    public JsonList convertList(JsonList list,String groupTag,List<String> groupNames){

        List<BaseAddressBookBean> beanList = new ArrayList<>();

        //将外部数据源转换成内部处理类
        for (int i = 0; i < list.size(); i++) {
            BaseAddressBookBean baseAddressBookBean = new BaseAddressBookBean();
            baseAddressBookBean.setData(list.getJsonMap(i).toString());
            baseAddressBookBean.setName(list.getJsonMap(i).getString(groupTag));
            beanList.add(baseAddressBookBean);
        }

        //对转换后的列表进行排序
        Collections.sort(beanList);

        //要返回的处理后的列表
        JsonList newList = new JsonList();

        //将内部数据再次转换成外部数据
        for (int i = 0; i < beanList.size(); i++) {

            JsonMap map = JsonMap.parse(beanList.get(i).getData());
            map.put("groupName",beanList.get(i).getFirstLetter());

            if (i == 0){
                groupNames.add(map.getString("groupName"));
            }else {

                if (!groupNames.get(groupNames.size()-1).equals(map.getString("groupName"))){
                    groupNames.add(map.getString("groupName"));
                }

            }

            newList.add(map);

        }

        return newList;

    }

}
