package org.jzl.lang.util;

import org.jzl.lang.fun.Function;
import org.jzl.lang.fun.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class CollectionUtils {

    private CollectionUtils() {
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
            ForeachUtils.eachIfIterator(collection, (t, remover) -> {
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

    public static <T> void move(List<T> list, int position, int targetPosition) {
        if (nonEmpty(list)) {
            T target = list.get(position);
            if (position < targetPosition) {

                for (int i = position; i < targetPosition; i++) {
                    list.set(i, list.get(i + 1));
                }

            } else {
                for (int i = position; i > targetPosition; i--) {
                    list.set(i, list.get(i - 1));
                }
            }
            list.set(targetPosition, target);
        }
    }
}
