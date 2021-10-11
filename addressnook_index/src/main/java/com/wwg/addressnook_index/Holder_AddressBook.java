package com.wwg.addressnook_index;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.kongzue.baseokhttp.util.JsonMap;

/**
 * 通讯录列表 - 普通模式
 */
public class Holder_AddressBook extends BaseAddressBookHolder {

    public View itemView;
    private Context context;

    private AppCompatImageView addressBook_userHead;
    private AppCompatTextView addressBook_userName;
    private AppCompatTextView addressBook_info;

    public Holder_AddressBook(Context context, @NonNull View itemView) {
        super(context,itemView);
        this.context = context;
        this.itemView = itemView;

        addressBook_userHead = (AppCompatImageView) itemView.findViewById(R.id.addressBook_userHead);
        addressBook_userName = (AppCompatTextView) itemView.findViewById(R.id.addressBook_userName);
        addressBook_info = (AppCompatTextView) itemView.findViewById(R.id.addressBook_info);

    }

    @Override
    public BaseAddressBookHolder getInstance(Context context, @NonNull View itemView) {
        return new Holder_AddressBook(context,itemView);
    }

    @Override
    public void setData(JsonMap data, ItemClickInter listener) {

        addressBook_userName.setText(data.getString("userName"));
        if (listener!=null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onViewClick("itemView",getLayoutPosition(),data);
                }
            });
        }

    }

}
