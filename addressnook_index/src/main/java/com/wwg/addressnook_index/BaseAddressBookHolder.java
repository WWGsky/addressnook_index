package com.wwg.addressnook_index;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.baseokhttp.util.JsonMap;

public abstract class BaseAddressBookHolder extends RecyclerView.ViewHolder {

    public BaseAddressBookHolder(Context context, @NonNull View itemView) {
        super(itemView);
    }

    public abstract BaseAddressBookHolder getInstance(Context context, @NonNull View itemView);
    public abstract void setData(JsonMap data, ItemClickInter listener);

}
