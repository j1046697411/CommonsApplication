package org.jzl.lang.util.holder;

import java.io.Serializable;

public class BinaryHolder<ONE, TWO> implements Serializable {

    public ONE one;
    public TWO two;

    protected BinaryHolder(ONE one, TWO two) {
        this.one = one;
        this.two = two;
    }

    public static <ONE, TWO> BinaryHolder<ONE, TWO> of(ONE one, TWO two) {
        return new BinaryHolder<>(one, two);
    }
}
