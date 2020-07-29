package org.jzl.lang.util;

import org.jzl.lang.fun.Function;
import org.jzl.lang.fun.Predicate;
import org.jzl.lang.fun.Supplier;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> LinkedList<T> newLinkedList() {
        return new LinkedList<>();
    }

    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<>();
    }

    public static <T> HashSet<T> newHashSet() {
        return new HashSet<>();
    }

    public static <T> LinkedHashSet<T> newLinkedHashSet() {
        return new LinkedHashSet<>();
    }

    public static <T> CopyOnWriteArraySet<T> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<>();
    }

    public static <E extends Enum<E>> EnumSet<E> newEnumSet(Class<E> type) {
        return EnumSet.allOf(type);
    }

    public static BitSet newBitSet() {
        return new BitSet();
    }

    public static <T> T get(List<T> list, int index, Supplier<T> supplier) {
        return notLimit(list, index) ? supplier.get() : list.get(index);
    }

    public static <T> T get(List<T> list, int index, T def) {
        return notLimit(list, index) ? def : list.get(index);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean nonEmpty(Collection<?> collection) {
        return collection != null && collection.size() != 0;
    }

    @SafeVarargs
    public static <T> void addAll(Collection<T> collection, T... array) {
        Collections.addAll(collection, array);
    }

    public static <T> void addAll(Collection<T> collection, Collection<T> data) {
        if (ObjectUtils.nonNull(collection) && ObjectUtils.nonNull(data)) {
            collection.addAll(data);
        }
    }

    public static <T> ArrayList<T> toArrayList(Collection<T> data) {
        return data instanceof ArrayList ? (ArrayList<T>) data : new ArrayList<>(data);
    }

    public static <T, C extends Collection<T>> C trimAllNull(C collection) {
        return trimAllIf(collection, ObjectUtils::isNull);
    }

    public static <T, C extends Collection<T>> C trimAllIf(C collection, Predicate<T> predicate) {
        ObjectUtils.requireNonNull(predicate, "predicate");
        if (nonEmpty(collection)) {
            ForeachUtils.eachIfRemove(collection, (t, remover) -> {
                if (predicate.test(t)) {
                    remover.remove();
                }
            });
        }
        return collection;
    }

    public static <T, R, C extends Collection<R>> C map(Collection<T> request, C result, Function<T, R> mapper) {
        ObjectUtils.requireNonNull(result, "result");
        ObjectUtils.requireNonNull(mapper, "mapper");

        if (CollectionUtils.nonEmpty(request)) {
            for (T t : request) {
                result.add(mapper.apply(t));
            }
        }
        return result;
    }

    public static <T> void move(List<T> list, int fromPosition, int toPosition) {
        if (nonEmpty(list)) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(list, i, i + 1);
                }

            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(list, i, i - 1);
                }
            }
        }
    }

    public static int length(Collection<?> collection) {
        return isEmpty(collection) ? 0 : collection.size();
    }

    public static <T> List<T> subList(List<T> list, int fromPosition, int toPosition) {
        return isEmpty(list) ? new ArrayList<>() : list.subList(fromPosition, toPosition);
    }

    public static boolean isLimit(Collection<?> collection, int index) {
        return MathUtils.isLimit(index, 0, length(collection) - 1);
    }

    public static boolean notLimit(Collection<?> collection, int index) {
        return !isLimit(collection, index);
    }
}
