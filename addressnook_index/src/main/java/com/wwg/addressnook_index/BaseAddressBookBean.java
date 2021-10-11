package com.wwg.addressnook_index;

import com.github.promeg.pinyinhelper.Pinyin;

public class BaseAddressBookBean implements Comparable<BaseAddressBookBean> {

    //分组字段的文本
    private String name;
    //文本对应的拼音
    private String pinyin;
    //文本的首字母
    private String firstLetter;
    //转换前的数据源
    private String data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (!name.isEmpty()){
            char[] chars = new char[name.length()];
            name.getChars(0,1,chars,0);
            try {
                pinyin = Pinyin.toPinyin(chars[0]);
                firstLetter = pinyin.substring(0, 1).toUpperCase();
                if (!firstLetter.matches("[A-Z]")){
                    firstLetter = "#";
                }
            }catch (Exception e){
                firstLetter = "#";
            }
        }else {
            firstLetter = "#";
        }

    }

    public String getPinyin() {
        return pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int compareTo(BaseAddressBookBean o) {
        if (firstLetter.equals("#") && !o.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && o.getFirstLetter().equals("#")){
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(o.getPinyin());
        }
    }
}
