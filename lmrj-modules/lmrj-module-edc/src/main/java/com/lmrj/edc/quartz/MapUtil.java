package com.lmrj.edc.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface MapUtil<M>  {

    static <T> Map<String, T> listToMap(List<T> list, MapUtil<T> mapUtil) {

        Map<String, T> map = new HashMap();
        for (T each: list) {
            map.put(mapUtil.getKey(each), each);
        }
        return map;
    }


    static <T> Map<String, List<T>> listToMapList(List<T> list, MapUtil<T> mapUtil) {

        Map<String, List<T>> map = new HashMap();
        for (T each: list) {

            String key = mapUtil.getKey(each);
            List<T> ts = map.get(key);
            if (null == ts) {
                ts = new ArrayList<>();
            }
            map.put(key, ts);
            ts.add(each);
        }
        return map;
    }

    String getKey(M m);
}
