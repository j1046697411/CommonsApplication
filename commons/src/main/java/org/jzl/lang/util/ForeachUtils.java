package org.jzl.lang.util;

import org.jzl.lang.fun.BinaryConsumer;
import org.jzl.lang.fun.BinaryPredicate;
import org.jzl.lang.fun.Consumer;
import org.jzl.lang.fun.IntBinaryConsumer;
import org.jzl.lang.fun.IntBinaryPredicate;
import org.jzl.lang.fun.IntConsumer;
import org.jzl.lang.fun.IntPredicate;
import org.jzl.lang.fun.Predicate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class ForeachUtils {

    private ForeachUtils() {
    }

    public static <T> void each(int[] array, IntBinaryConsumer consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");
        if (ArrayUtils.nonEmpty(array)) {
            int length = array.length;
            for (int i = 0; i < length; i++) {
                consumer.accept(i, array[i]);
            }
        }
    }

    public static <T> void eachIfBack(int[] array, IntBinaryPredicate predicate) {
        ObjectUtils.requireNonNull(predicate, "predicate");
        if (ArrayUtils.nonEmpty(array)) {
            int length = array.length;
            for (int i = 0; i < length; i++) {
                if (predicate.test(i, array[i])) {
                    return;
                }
            }
        }
    }

    public static <T> void each(T[] array, IntConsumer<T> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");

        if (ArrayUtils.nonEmpty(array)) {
            int length = array.length;
            for (int i = 0; i < length; i++) {
                consumer.accept(i, array[i]);
            }
        }
    }

    public static <T> void each(T[] array, Consumer<T> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");

        if (ArrayUtils.nonEmpty(array)) {
            for (T a : array) {
                consumer.accept(a);
            }
        }
    }

    public static <T> void eachIfBack(T[] array, IntPredicate<T> predicate) {
        ObjectUtils.requireNonNull(predicate, "predicate");
        if (ArrayUtils.nonEmpty(array)) {
            int length = array.length;
            for (int i = 0; i < length; i++) {
                if (predicate.test(i, array[i])) {
                    return;
                }
            }
        }
    }

    public static <K, V> void eachIfBack(Map<K, V> map, BinaryPredicate<K, V> predicate) {
        ObjectUtils.requireNonNull(predicate, "predicate");
        if (MapUtils.nonEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (predicate.test(entry.getKey(), entry.getValue())) {
                    return;
                }
            }
        }
    }

    public static <K, V> void each(Map<K, V> map, BinaryConsumer<K, V> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");
        if (MapUtils.nonEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                consumer.apply(entry.getKey(), entry.getValue());
            }
        }
    }

    public static <T> void each(List<T> list, IntConsumer<T> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");
        if (CollectionUtils.nonEmpty(list)) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                consumer.accept(i, list.get(i));
            }
        }
    }

    public static <T> void eachIfBack(List<T> list, IntPredicate<T> predicate) {
        ObjectUtils.requireNonNull(predicate, "predicate");
        if (CollectionUtils.nonEmpty(list)) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (predicate.test(i, list.get(i))) {
                    return;
                }
            }
        }
    }

    public static <T> void each(Iterable<T> iterable, Consumer<T> consumer) {
        ObjectUtils.requireNonNull(iterable, "iterable");
        ObjectUtils.requireNonNull(consumer, "consumer");

        for (T t : iterable) {
            consumer.accept(t);
        }
    }

    public static <T> void each(Iterable<T> iterable, Predicate<T> predicate) {
        ObjectUtils.requireNonNull(iterable, "iterable");
        ObjectUtils.requireNonNull(predicate, "consumer");
        for (T t : iterable) {
            if (predicate.test(t)) {
                return;
            }
        }
    }

    public static <T> void eachIfIterator(Iterable<T> iterable, BinaryConsumer<T, Remover> consumer) {
        ObjectUtils.requireNonNull(iterable, "iterable");
        each(iterable.iterator(), consumer);
    }

    public static <T> void each(Iterator<T> iterator, BinaryConsumer<T, Remover> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");
        ObjectUtils.requireNonNull(iterator, "iterator");
        Remover remover = iterator::remove;
        while (iterator.hasNext()) {
            consumer.apply(iterator.next(), remover);
        }
    }

    public interface Remover {
        void remove();
    }

}
