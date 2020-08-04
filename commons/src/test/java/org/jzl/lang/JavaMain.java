package org.jzl.lang;

import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaMain {

    public static void main(String[] args) {
        List<String> test = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        CollectionUtils.move(test, 0, 9);
        System.out.println(test);
        System.out.println(StringUtils.findIfEmpty("123 456 789 456 852, 489", "(([1-9])[0-9]+)", 3, 2));
        System.out.println(StringUtils.matches("023456", "^(\\+|\\-?)([1-9]\\d*)$"));
        System.out.println(StringUtils.matches("0x123456", "^(#|0[xX])[0-9a-fA-F]+$"));
        System.out.println(StringUtils.matches("0X123456", "^#|0[xX][0-9a-fA-F]+$"));
        System.out.println(StringUtils.matches("#123456", "^#|0[xX][0-9a-fA-F]+$"));

        System.out.println(StringUtils.matches("0x1234Fa", "^(#|0[xX])[0-9a-fA-F]+$"));
        System.out.println(StringUtils.matches("0X12F456", "^(#|0[xX])[0-9a-fA-F]+$"));
        System.out.println(StringUtils.matches("0X123456", "^(#|0[xX])[0-9a-fA-F]+$"));

        System.out.println(StringUtils.matches("0x123T456", "^(#|0[xX])[0-9a-fA-F]+$"));
        System.out.println(StringUtils.matches("0X1234A56", "^(#|0[xX])[0-9a-fA-F]+$"));
        System.out.println(StringUtils.matches("0X12C3456", "^(#|0[xX])[0-9a-fA-F]+$"));

        System.out.println(StringUtils.matches("012557", "^(\\-|\\+?)0[0-7]+$"));
        System.out.println(StringUtils.matches("0", StringUtils.PATTERN_NUMBER_10));

        String r16 = "^#|(0[xX])(\\+|\\-?)(\\w+)$";
        String r8 = "^(\\+|\\-)?0([0-7]+)$";
        String r10 = "^\\+|\\-[1-9]\\w*$";

    }
}
