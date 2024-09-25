package com.hgd.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class MyCopyBeanUtil {
    public static<T> T copyBean(Object src, Class<T> tClass){
        try {
            T t = tClass.newInstance();
            BeanUtils.copyProperties(src,t);
            return t;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static<T> List<T> copyList(List<? extends Object> srcList, Class<T> tClass){
        return srcList.stream().map(o -> copyBean(o,tClass)).collect(Collectors.toList());
    }
}
