package org.jzl.reflect;

import com.alibaba.fastjson.JSON;

import org.jzl.lang.util.StreamUtils;
import org.jzl.lang.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

public class JavaMain {
    public static void main(String[] args) throws ReflectException {
        System.out.println(Reflect.of(String.class).create(new char[]{'a', 'b'}, 0, 1).get());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{11, 2, 1, 4, 125});
        byte[] bytes = Reflect.of(StreamUtils.class).<byte[]>call("copyStreamToBytes", inputStream).get();
        System.out.println(StringUtils.toHexString(":", bytes, true));
        Test test = Reflect.of(new HashMap<>()).as(Test.class);
        test.test("test");
        test.setTest2("test2");
        test.setLocked(true);

        Reflect<Test> reflect = Reflect.of(test);

        System.out.println("test:" + test.test());
        System.out.println("test2:" + test.getTest2());
        System.out.println("locked:" + test.isLocked());
        System.out.println(JSON.toJSONString(test));

        System.out.println(reflect.get("test2").get());
        System.out.println(reflect.get("locked").get());
        System.out.println(reflect.call("test").get());
    }

    public interface Test {

        String test();

        void test(String test);

        boolean isLocked();

        void setLocked(boolean locked);

        void setTest2(String test2);

        String getTest2();
    }
}
