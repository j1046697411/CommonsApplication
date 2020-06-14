package org.jzl.reflect;

import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.StringUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Map;

public final class Reflect<T> {

    private Class<T> type;
    private T target;

    private Reflect(Class<T> type, T target) {
        this.type = ObjectUtils.requireNonNull(type);
        this.target = target;
    }

    public static <T> Reflect<T> of(Class<T> type, T target) {
        return new Reflect<>(type, target);
    }

    @SuppressWarnings("unchecked")
    public static <T> Reflect<T> of(T target) {
        ObjectUtils.requireNonNull(target);
        return of((Class<T>) target.getClass(), target);
    }

    public static <T> Reflect<T> of(Class<T> type) {
        return of(type, null);
    }

    @SuppressWarnings("unchecked")
    public Reflect<T> create(Object... args) throws ReflectException {
        Class<?>[] types = types(args);
        try {
            return create(type.getDeclaredConstructor(types), args);
        } catch (NoSuchMethodException e) {
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                if (match(constructor, types)) {
                    return create((Constructor<T>) constructor, args);
                }
            }
            throw new ReflectException();
        }
    }

    private Reflect<T> create(Constructor<T> constructor, Object... args) throws ReflectException {
        try {
            return of(constructor.getDeclaringClass(), constructor.newInstance(args));
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    public <R> Reflect<R> call(Class<R> returnType, Caller<R, T> caller, Object... args) {
        ObjectUtils.requireNonNull(caller);
        return of(returnType, caller.call(target, args));
    }

    public <R> Reflect<R> call(String name, Object... args) throws ReflectException {
        Class<?>[] types = types(args);
        try {
            Method method = findMethod(name, types);
            return call(method, args);
        } catch (NoSuchMethodException e) {
            throw new ReflectException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <R> Reflect<R> call(Method method, Object... args) throws ReflectException {
        Class<R> returnType = (Class<R>) method.getReturnType();
        try {
            R value = (R) accessible(method).invoke(target, args);
            return of(returnType, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectException(e);
        }
    }

    public Reflect<T> set(String name, Object value) throws ReflectException {
        try {
            call("set" + name.substring(0, 1).toUpperCase() + name.substring(1), value);
        } catch (ReflectException e) {
            return field(name, value);
        }
        return this;
    }

    public <R> Reflect<R> get(String name) throws ReflectException {
        try {
            Method method;
            try {
                method = findMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class<?>[0]);
            } catch (NoSuchMethodException ex) {
                method = findMethod("is" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class<?>[0]);
            }
            return call(method);
        } catch (ReflectException | NoSuchMethodException e) {
            return field(name);
        }

    }

    public <R> Reflect<R> field(String name) throws ReflectException {
        return field(findField(name));
    }

    public Reflect<T> field(String name, Object value) throws ReflectException {
        return field(findField(name), value);
    }

    private Reflect<T> field(Field field, Object value) throws ReflectException {
        Field field1 = accessible(field);
        if (Modifier.isFinal(field1.getModifiers())) {
            Reflect.of(Field.class, field1).field("modifiers", field1.getModifiers() & ~Modifier.FINAL);
        }
        try {
            field1.set(target, value);
            return this;
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <R> Reflect<R> field(Field field) throws ReflectException {
        Class<R> fieldType = (Class<R>) field.getType();
        try {
            R value = (R) accessible(field).get(target);
            return of(fieldType, value);
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    private Field findField(String name) throws ReflectException {
        try {
            return type.getField(name);
        } catch (NoSuchFieldException e) {
            Class<?> type = this.type;
            do {
                try {
                    return type.getDeclaredField(name);
                } catch (NoSuchFieldException ex) {
                    type = type.getSuperclass();
                }
            } while (type != null);
        }
        throw new ReflectException();
    }

    private Method findMethod(String name, Class<?>[] types) throws NoSuchMethodException {
        try {
            return this.type.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            Class<?> type = this.type;
            do {
                Method[] methods = type.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals(name) && match(method, types)) {
                        return method;
                    }
                }
                type = type.getSuperclass();
            } while (type != null);
        }
        throw new NoSuchMethodException();
    }

    private boolean match(Executable executable, Class<?>[] types) {
        return match(executable.getParameterTypes(), types);
    }

    private boolean match(Class<?>[] declaredTypes, Class<?>[] types) {
        if (declaredTypes.length != types.length) {
            return false;
        }
        for (int i = 0; i < declaredTypes.length; i++) {
            Class<?> declaredType = wrapper(declaredTypes[i]);
            Class<?> type = wrapper(types[i]);
            if (type == null) {
                continue;
            }
            if (declaredType.isAssignableFrom(type)) {
                continue;
            }
            return false;
        }
        return true;
    }

    public T get() {
        return target;
    }

    private static Class<?>[] types(Object... args) {
        if (ArrayUtils.isEmpty(args)) {
            return new Class<?>[0];
        } else {
            Class<?>[] types = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                Object o = args[i];
                if (ObjectUtils.nonNull(o)) {
                    types[i] = o.getClass();
                }
            }
            return types;
        }
    }

    private Class<?> wrapper(Class<?> type) {
        if (ObjectUtils.nonNull(type) && type.isPrimitive()) {
            if (type == int.class) {
                return Integer.class;
            } else if (type == long.class) {
                return Long.class;
            } else if (type == short.class) {
                return Short.class;
            } else if (type == byte.class) {
                return Byte.class;
            } else if (type == char.class) {
                return Character.class;
            } else if (type == boolean.class) {
                return Boolean.class;
            } else if (type == float.class) {
                return Float.class;
            } else if (type == double.class) {
                return Double.class;
            } else if (type == void.class) {
                return Void.class;
            } else {
                return type;
            }
        } else {
            return type;
        }
    }

    private static <A extends AccessibleObject> A accessible(A accessible) {
        if (ObjectUtils.nonNull(accessible)) {
            if (accessible instanceof Member
                    && Modifier.isPublic(((Member) accessible).getModifiers())
                    && Modifier.isPublic(((Member) accessible).getDeclaringClass().getModifiers())) {
                return accessible;
            }
            if (!accessible.isAccessible()) {
                accessible.setAccessible(true);
                return accessible;
            }
        }
        return accessible;
    }

    @SuppressWarnings("unchecked")
    public <R> R as(Class<R> type) {
        return (R) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (o1, method, objects) -> {
            try {
                return call(method.getName(), objects).get();
            } catch (ReflectException e) {
                if (target instanceof Map<?, ?>) {
                    return property((Map<Object, Object>) target, method.getName(), objects);
                }
                throw e;
            }
        });
    }

    private Object property(Map<Object, Object> map, String property, Object... args) {
        if (property.startsWith("is")) {
            return map.get(property(property.substring(2)));
        } else if (property.startsWith("get")) {
            return map.get(property(property.substring(3)));
        } else if (property.startsWith("set")) {
            map.put(property(property.substring(3)), args[0]);
        } else {
            if (ArrayUtils.isEmpty(args)) {
                return map.get(property(property));
            } else {
                map.put(property(property), args[0]);
            }
        }
        return null;
    }

    private String property(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        } else if (name.length() == 1) {
            return name.toLowerCase();
        } else {
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
    }
}
