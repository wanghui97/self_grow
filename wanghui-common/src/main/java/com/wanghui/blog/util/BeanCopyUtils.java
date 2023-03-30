package com.wanghui.blog.util;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: BeanCopyUtils
 * Package: com.wanghui.blog.util
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/23 16:16
 * @Version 1.0
 */
public class BeanCopyUtils {
    private BeanCopyUtils() {
    }

    /**
     * 拷贝单个对象
     * */
    public static <V> V copyBean(Object source, Class<V> clazz){
        V result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source,result);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 拷贝集合对象
     * */
    public static <O, V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o,clazz))
                .collect(Collectors.toList());
    }
}
