package org.jzl.lang.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ReflectUtils {

    private static final Class<?>[] WRAP_PRIMITIVE_TYPES = {
            Integer.class, Long.class, Short.class, Byte.class, Character.class, Float.class, Double.class, Boolean.class
    };

    private ReflectUtils() {
    }


    public static boolean isPrimitive(Class<?> type) {
        if (type == null) {
            return false;
        }
        return type.isPrimitive() || isWrapPrimitive(type);
    }

    public static boolean isWrapPrimitive(Class<?> type) {
        if (type == null) {
            return false;
        }
        for (Class<?> pType : WRAP_PRIMITIVE_TYPES) {
            if (type == pType) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAssignableFrom(Class<?> parent, Class<?> child) {
        return parent != null && parent.isAssignableFrom(child);
    }

    public static Object invokeMethod(Object target, Method method, Object defReturn, Object... args) {
        Objects.requireNonNull(method);
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return defReturn;
        }
    }

    public static Object invokeField(Object target, Field field, Object defValue) {
        ObjectUtils.requireNonNull(field);
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(target);
        } catch (IllegalAccessException e) {
            return defValue;
        }
    }

    public static Object getFieldValue(Object target, Field field) {
        ObjectUtils.requireNonNull(field);
        if (ObjectUtils.nonNull(target)) {
            try {
                return invokeMethod(target, getGetterMethod(target.getClass(), field), null);
            } catch (NoSuchMethodException ignored) {
                return invokeField(target, field, null);
            }
        } else {
            return invokeField(null, field, null);
        }
    }

    public static Object getFieldValue(Class<?> type, String fieldName) {
        ObjectUtils.requireNonNull(type);
        try {
            return getFieldValue(null, type.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static Object getFieldValue(Object target, String fieldName) {
        ObjectUtils.requireNonNull(target);
        ObjectUtils.requireNonNull(fieldName);
        try {
            return getFieldValue(target, target.getClass().getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static Method getGetterMethod(Class<?> type, Field field) throws NoSuchMethodException {
        ObjectUtils.requireNonNull(field, "field is null");
        ObjectUtils.requireNonNull(type, "type is null");
        String name = field.getName();
        Class<?> fieldType = field.getType();
        String getterName;
        if (fieldType == boolean.class) {
            getterName = "is" + name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return getMethod(type, getterName);
    }

    public static Method getSetterMethod(Class<?> type, Field field) throws NoSuchMethodException {
        ObjectUtils.requireNonNull(type, "type is null");
        ObjectUtils.requireNonNull(field, "field is null");
        String name = field.getName();
        String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        return getMethod(type, setterName);
    }

    public static Method getMethod(Class<?> type, String methodName, Class<?>... types) throws NoSuchMethodException {
        ObjectUtils.requireNonNull(type, "type is null");
        ObjectUtils.requireNonNull(methodName, "methodName is null");
        return type.getDeclaredMethod(methodName, types);
    }
}
