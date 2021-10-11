package com.wwg.addressnook_index;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.baseokhttp.util.JsonList;

import java.util.ArrayList;
import java.util.List;

public class AddressBook_Adapter extends BaseAddressBook_Adapter {

    private Context context;
    private JsonList list;

    private ItemClickInter listener;
    private int resId;
    private String groupTag = "groupName";

    private List<String> groupNames = new ArrayList<>();

    public AddressBook_Adapter(
            Context context,
            JsonList list,
            int resId,
            String groupTag,
            boolean isAdapterManageGroup){
        this.context = context;
        this.resId = resId;
        if (isAdapterManageGroup){
            this.list = convertList(list,groupTag,groupNames);
        }else {
            this.groupTag = groupTag;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("AddressBook_Adapter", "创建了Holder");

        return new Holder_AddressBook(context,LayoutInflater.from(context).inflate(resId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Holder_AddressBook nowHolder = (Holder_AddressBook) holder;
        nowHolder.setData(list.getJsonMap(position),listener);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 判断position对应的Item是否是组的第一项
     *
     * @param position
     * @return
     */
    public boolean isItemHeader(int position) {
        if (position == 0) {
            return true;
        } else {
            String lastGroupName = list.getJsonMap(position-1).getString(groupTag);
            String currentGroupName = list.getJsonMap(position).getString(groupTag);
            //判断上一个数据的组别和下一个数据的组别是否一致，如果不一致则是不同组，也就是为第一项（头部）
            if (lastGroupName.equals(currentGroupName)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 获取position对应的Item组名
     *
     * @param position
     * @return
     */
    public String getGroupName(int position) {
        return list.getJsonMap(position).getString(groupTag);
    }

    /**
     * 获取所有的分组名称
     * @return
     */
    public List<String> getGroupNames() {
        return groupNames;
    }

    public JsonList getData(){
        return list;
    }

    public void setClickListener(ItemClickInter listener){
        this.listener = listener;
    }

}
