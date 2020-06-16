package org.jzl.lang;

import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.EncryptUtils;
import org.jzl.lang.util.MapUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.StringRandomUtils;
import org.jzl.lang.util.StringUtils;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JavaMain {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        System.out.println(StringUtils.join(":", null, "12", null, StringUtils.joiner("{", ":", "}").join("123").join("456"), "33"));
        System.out.println(StringUtils.join(":", Arrays.asList("ff", "12", "01", "33")));
        Map<String, Object> map = MapUtils.<String, Object>of((key, value) -> Pattern.matches("[0-9]*", key) || value instanceof Integer, new HashMap<>())
                .put("kkk1", StringRandomUtils.randomLowerString(10))
                .put("kkk2", StringRandomUtils.randomNumber(10))
                .put("kkk3", 525)
                .put("123", 548)
                .put("kkk3", "test")
                .build();
        System.out.println(ArrayUtils.isEmpty(new int[0]));
        System.out.println(StringUtils.joiner("?", "&", "#123").joins("=", map));
        System.out.println(StringUtils.toHexString(":", new byte[]{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, true));
        System.out.println(StringUtils.toHexString(new byte[]{-1, 0, 15, 25, 99}));
        System.out.println(StringUtils.toHexString(0));
        System.out.println(EncryptUtils.md5(":", "123", true));
        System.out.println(EncryptUtils.sha1(":", "123", false));
        System.out.println(EncryptUtils.sha256(StringUtils.EMPTY, "123", true));
        System.out.println(EncryptUtils.sha512(StringUtils.EMPTY, "123", true));
        System.out.println("time:" + (System.nanoTime() - startTime));

        Runnable run = () -> System.out.println("123456");

        Runnable object = (Runnable) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Runnable.class}, (proxy, method, args1) -> {
            System.out.println(method.getName());
            return method.invoke(run, args1);
        });
        object.run();

        System.out.println(ObjectUtils.isEmpty(""));
        System.out.println(ObjectUtils.isEmpty(new int[0]));
        System.out.println(ObjectUtils.isEmpty(new Object[1][2]));
        System.out.println(ObjectUtils.isEmpty(new ArrayList<>()));
        System.out.println(ObjectUtils.isEmpty(new HashMap<>()));
        System.out.println(ObjectUtils.isEmpty(null));
        System.out.println(ObjectUtils.isEmpty("123456"));
    }
}
