package org.jzl.lang.util.holder;

import java.io.Serializable;

public class TernaryHolder<ONE, TWO, THREE> extends BinaryHolder<ONE, TWO> implements Serializable {

    public THREE three;

    protected TernaryHolder(ONE one, TWO two, THREE three) {
        super(one, two);
        this.three = three;
    }

    public static <ONE, TWO, THREE> TernaryHolder<ONE, TWO, THREE> of(ONE one, TWO two, THREE three){
        return new TernaryHolder<>(one, two, three);
    }
}
