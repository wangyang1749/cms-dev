package com.wangyang.cms.cache;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StringCacheStore {
    public final static ConcurrentHashMap<String,String> CACHE_CONTAINER = new ConcurrentHashMap<>();

    public Optional<String> get(String key){
        return Optional.ofNullable(CACHE_CONTAINER.get(key));
    }
    public void setValue(String key,String value){
        CACHE_CONTAINER.put(key,value);
    }
    public void clearByKey(String key){
        CACHE_CONTAINER.remove(key);
    }
    public void clearAll(){
        CACHE_CONTAINER.clear();
    }

    public static String getValue(String key){
        return CACHE_CONTAINER.get(key);
    }

}
