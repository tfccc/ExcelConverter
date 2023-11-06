package com.example.excelconverter.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Frank.Tang
 * @date 2021-07-21 10:04
 * @desc
 **/
@SuppressWarnings("unused")
public class StreamUtil {

    private StreamUtil() {
        throw new IllegalStateException("该对象不能实例化");
    }

    /**
     * 执行Consumer类方法 (简化if then set)
     * @author Frank.tang
     */
    public static <A> void execSetter(boolean b, Consumer<A> consumer, A a) {
        if (b) {
            consumer.accept(a);
        }
    }

    /**
     * 执行Supplier类方法 (简化if then get)
     * @author Frank.tang
     */
    public static <A> void execGetter(boolean b, Supplier<A> supplier) {
        if (b) {
            supplier.get();
        }
    }

    /**
     * 提取某一个字段
     * @author Frank.Tang
     * @return List
     */
    public static <O, R> List<R> toList(Collection<O> collection, Function<? super O, ? extends R> function) {
        if (collection == null) {
            return new ArrayList<>();
        }
        return collection.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 提取某一个字段
     * @author Frank.Tang
     * @return Set
     */
    public static <O, R> Set<R> toSet(Collection<O> collection, Function<? super O, ? extends R> function) {
        if (collection == null) {
            return new HashSet<>();
        }
        return collection.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 转map
     * @author Frank.Tang
     * @return Map<K, V>
     */
    public static <O, K, V> Map<K, V> toMap(Collection<O> list,
                                            Function<? super O, ? extends K> keyFunc,
                                            Function<? super O, ? extends V> valFunc) {
        if (list == null) {
            return new HashMap<>();
        }
        return list.stream().filter(Objects::nonNull).collect(Collectors.toMap(keyFunc, valFunc, (o1, o2) -> o1));
    }

    /**
     * 转map
     *
     * @return Map<K, V>
     * @author Bob
     */
    public static <O, K> Map<K, O> toMap(Collection<O> list,
                                         Function<? super O, ? extends K> keyFunc) {
        if (list == null) {
            return new HashMap<>();
        }
        return list.stream().filter(Objects::nonNull).collect(Collectors.toMap(keyFunc, o -> o, (o1, o2) -> o1));
    }

    /**
     * 保留满足条件的
     * @author Frank.Tang
     * @return List
     */
    public static <O> List<O> filter(List<O> list, Predicate<? super O> predicate) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().filter(Objects::nonNull).filter(predicate).collect(Collectors.toList());
    }

}
