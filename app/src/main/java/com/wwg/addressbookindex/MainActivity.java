package com.wwg.addressbookindex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.kongzue.baseokhttp.util.JsonList;
import com.kongzue.baseokhttp.util.JsonMap;
import com.wwg.addressnook_index.AddressBookIndex;
import com.wwg.addressnook_index.AddressBook_Adapter;
import com.wwg.addressnook_index.IndexCallBack;
import com.wwg.addressnook_index.ItemClickInter;
import com.wwg.addressnook_index.StickHeaderDecoration;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rec;
    private AddressBookIndex addressBookIndex;
    private AddressBook_Adapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rec = findViewById(R.id.rec);
        addressBookIndex = findViewById(R.id.addressBookIndex);

        rec.addItemDecoration(new StickHeaderDecoration(this));
        layoutManager = new LinearLayoutManager(this);
        rec.setLayoutManager(layoutManager);

        rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int itemPosition = layoutManager.findFirstVisibleItemPosition();
                String selectGroupName = adapter.getGroupName(itemPosition);
                int selectGroupIndex = -1;
                for (int i = 0; i < adapter.getGroupNames().size(); i++) {
                    if (selectGroupName.equals(adapter.getGroupNames().get(i))){
                        selectGroupIndex = i;
                        break;
                    }
                }

                addressBookIndex.setSelectIndex(selectGroupIndex);

            }
        });

        addressBookIndex.setIndexCallBack(new IndexCallBack() {
            @Override
            public void onSelectIndexChange(int position) {

                rec.stopScroll();

                for (int i = 0; i < adapter.getData().size(); i++) {

                    if (adapter.getGroupNames().get(position).equals(adapter.getData().getJsonMap(i).getString("groupName"))){
                        layoutManager.scrollToPositionWithOffset(i,0);
                        break;
                    }

                }

            }
        });

        initData();

    }

    private void initData(){

        JsonList list = new JsonList();

        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","????????????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","????????????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","????????????"));
        list.add(JsonMapHelper.getMap().with("userName","????????????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","??????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","?????????"));
        list.add(JsonMapHelper.getMap().with("userName","Tom"));
        list.add(JsonMapHelper.getMap().with("userName","Jerry"));
        list.add(JsonMapHelper.getMap().with("userName","12345"));
        list.add(JsonMapHelper.getMap().with("userName","54321"));
        list.add(JsonMapHelper.getMap().with("userName","_(:????????)_"));
        list.add(JsonMapHelper.getMap().with("userName","??????%???#???%#"));

        adapter = new AddressBook_Adapter(this, list,R.layout.item_addressbook, "userName",true);
        adapter.setClickListener(new ItemClickInter() {
            @Override
            public void onViewClick(String tag, int position, JsonMap map) {
                if (tag.equals("itemView")){
                    Toast.makeText(MainActivity.this, map.getString("userName"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("MainActivity", "???????????? ???????????? --> " + adapter.getGroupNames().toString());
        addressBookIndex.setData(adapter.getGroupNames());

        rec.setAdapter(adapter);

    }

}