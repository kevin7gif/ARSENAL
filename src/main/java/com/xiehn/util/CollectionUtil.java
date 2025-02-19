package com.xiehn.util;

import java.util.Collection;

public class CollectionUtil {

    /**
     * <p> 功能描述：判断个字符是否为空</p>
     *
     * @param source 要判断字符串
     * @return 如果为空返回真，反之为假
     * @author jiangyu
     * @date 2016年3月23日 下午2:29:53
     * @since V1.0
     */
    public static boolean isEmpty(String source) {
        return (source == null || source.trim().equals("") || "null".equals(source));
    }

    /**
     * <p> 功能描述：判断个对象是否为空</p>
     *
     * @param source 要判断对
     * @return 如果为空返回真，反之为假
     * @author jiangyu
     * @date 2016年3月23日 下午2:30:26
     * @since V1.0
     */
    public static boolean isEmpty(Object source) {
        if (source instanceof Collection<?> && source != null) {
            Collection<?> collection = (Collection<?>) source;
            return (collection == null || collection.isEmpty());
        }
        return (source == null || source.toString().trim().equals("") || "null".equals(source));
    }

}

