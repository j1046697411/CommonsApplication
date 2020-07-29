package org.jzl.lang;

import org.jzl.lang.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaMain {

    public static void main(String[] args) {
        List<String> test = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        CollectionUtils.move(test, 0, 9);
        System.out.println(test);

    }
}
