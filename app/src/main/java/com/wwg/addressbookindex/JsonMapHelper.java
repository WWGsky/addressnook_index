package com.wwg.addressbookindex;

import com.kongzue.baseokhttp.util.JsonMap;

public class JsonMapHelper extends JsonMap {

    public static JsonMapHelper getMap(){
        return new JsonMapHelper();
    }

    public JsonMapHelper with(String key,Object value){
        this.put(key,value);
        return this;
    }

}
