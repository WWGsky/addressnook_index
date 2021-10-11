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

        list.add(JsonMapHelper.getMap().with("userName","亳州"));
        list.add(JsonMapHelper.getMap().with("userName","大娃"));
        list.add(JsonMapHelper.getMap().with("userName","二娃"));
        list.add(JsonMapHelper.getMap().with("userName","三娃"));
        list.add(JsonMapHelper.getMap().with("userName","四娃"));
        list.add(JsonMapHelper.getMap().with("userName","五娃"));
        list.add(JsonMapHelper.getMap().with("userName","六娃"));
        list.add(JsonMapHelper.getMap().with("userName","七娃"));
        list.add(JsonMapHelper.getMap().with("userName","喜羊羊"));
        list.add(JsonMapHelper.getMap().with("userName","美羊羊"));
        list.add(JsonMapHelper.getMap().with("userName","懒羊羊"));
        list.add(JsonMapHelper.getMap().with("userName","沸羊羊"));
        list.add(JsonMapHelper.getMap().with("userName","暖羊羊"));
        list.add(JsonMapHelper.getMap().with("userName","慢羊羊"));
        list.add(JsonMapHelper.getMap().with("userName","灰太狼"));
        list.add(JsonMapHelper.getMap().with("userName","红太狼"));
        list.add(JsonMapHelper.getMap().with("userName","孙悟空"));
        list.add(JsonMapHelper.getMap().with("userName","黑猫警长"));
        list.add(JsonMapHelper.getMap().with("userName","舒克"));
        list.add(JsonMapHelper.getMap().with("userName","贝塔"));
        list.add(JsonMapHelper.getMap().with("userName","海尔"));
        list.add(JsonMapHelper.getMap().with("userName","阿凡提"));
        list.add(JsonMapHelper.getMap().with("userName","邋遢大王"));
        list.add(JsonMapHelper.getMap().with("userName","哪吒"));
        list.add(JsonMapHelper.getMap().with("userName","没头脑"));
        list.add(JsonMapHelper.getMap().with("userName","不高兴"));
        list.add(JsonMapHelper.getMap().with("userName","蓝皮鼠"));
        list.add(JsonMapHelper.getMap().with("userName","大脸猫"));
        list.add(JsonMapHelper.getMap().with("userName","大头儿子"));
        list.add(JsonMapHelper.getMap().with("userName","小头爸爸"));
        list.add(JsonMapHelper.getMap().with("userName","蓝猫"));
        list.add(JsonMapHelper.getMap().with("userName","淘气"));
        list.add(JsonMapHelper.getMap().with("userName","叶峰"));
        list.add(JsonMapHelper.getMap().with("userName","楚天歌"));
        list.add(JsonMapHelper.getMap().with("userName","江流儿"));
        list.add(JsonMapHelper.getMap().with("userName","Tom"));
        list.add(JsonMapHelper.getMap().with("userName","Jerry"));
        list.add(JsonMapHelper.getMap().with("userName","12345"));
        list.add(JsonMapHelper.getMap().with("userName","54321"));
        list.add(JsonMapHelper.getMap().with("userName","_(:з」∠)_"));
        list.add(JsonMapHelper.getMap().with("userName","……%￥#￥%#"));

        adapter = new AddressBook_Adapter(this, list,R.layout.item_addressbook, "userName",true);
        adapter.setClickListener(new ItemClickInter() {
            @Override
            public void onViewClick(String tag, int position, JsonMap map) {
                if (tag.equals("itemView")){
                    Toast.makeText(MainActivity.this, map.getString("userName"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("MainActivity", "列表滚动 首条下标 --> " + adapter.getGroupNames().toString());
        addressBookIndex.setData(adapter.getGroupNames());

        rec.setAdapter(adapter);

    }

}