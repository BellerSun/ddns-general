package cn.sunyc.ddnsgeneral.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 对象工具类
 *
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/17 15:10
 */
@SuppressWarnings("unused")
public class ObjUtil {


    /**
     * 获取所继承类的泛型实际类型
     *
     * @return 泛型实际类型
     */
    public static Type getGenericType(Object obj) {
        return getGenericType(obj, null);
    }

    /**
     * 获取所继承类的泛型实际类型
     *
     * @param obj             获取的对象
     * @param findGenericType 过滤某个类型的泛型
     * @return 泛型实际类型
     */
    public static Type getGenericType(Object obj, Class<?> findGenericType) {
        return getGenericType(obj.getClass(), findGenericType);
    }

    /**
     * 获取所继承类的泛型实际类型
     *
     * @param clazz           获取的类型
     * @param findGenericType 过滤某个类型的泛型
     * @return 泛型实际类型
     */
    public static Type getGenericType(Class<?> clazz, Class<?> findGenericType) {
        final ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (null == actualTypeArguments || actualTypeArguments.length == 0) {
            return null;
        }
        if (findGenericType == null) {
            return actualTypeArguments[0];
        }

        for (Type type : actualTypeArguments) {
            if (findGenericType.isAssignableFrom((Class<?>) type)) {
                return type;
            }
        }
        return null;
    }

    private ObjUtil() {
    }
}
