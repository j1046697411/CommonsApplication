package org.jzl.lang.util.datablcok;

public interface DataBlock<T> extends DataSource<T>{

    int getBlockId();

    PositionType getPositionType();

    int startPosition();

    void disassociate();

    enum PositionType {

        HEADER(1), CONTENT(2), FOOTER(3);

        private int sequence;

        PositionType(int sequence) {
            this.sequence = sequence;
        }

        public int getSequence() {
            return sequence;
        }
    }
}
