package org.jzl.lang;

import org.jzl.lang.util.datablcok.DataBlock;
import org.jzl.lang.util.datablcok.DataBlockProvider;
import org.jzl.lang.util.datablcok.DataBlockProviders;
import org.jzl.lang.util.datablcok.DataObserver;

import java.util.List;
import java.util.ListIterator;

public class DataBlockMain {
    public static void main(String[] args) {
        DataBlockProvider<String> dataBlockProvider = DataBlockProviders.dataBlockProvider(1);

        dataBlockProvider.addDataObserver(new DataObserver() {
            @Override
            public void onInserted(int position, int count) {
                System.out.println("onInserted:" + position + "=>" + count);
            }

            @Override
            public void onRemoved(int position, int count) {
                System.out.println("onRemoved:" + position + "=>" + count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                System.out.println("onMoved:" + fromPosition + "=>" + toPosition);
            }

            @Override
            public void onChanged(int position) {
                System.out.println("onChanged:" + position);
            }

            @Override
            public void onAllChanged() {
                System.out.println("onAllChanged");
            }
        });

        DataBlock<String> dataBlock = dataBlockProvider.dataBlock(DataBlock.PositionType.CONTENT, 1);
        DataBlock<String> dataBlock2 = dataBlockProvider.dataBlock(DataBlock.PositionType.CONTENT, 2);
        DataBlock<String> dataBlock3 = dataBlockProvider.dataBlock(DataBlock.PositionType.CONTENT, 3);
        DataBlock<String> header = dataBlockProvider.dataBlock(DataBlock.PositionType.HEADER, 2);
        DataBlock<String> footer = dataBlockProvider.dataBlock(DataBlock.PositionType.FOOTER, 3);
        DataBlock<String> footer2 = dataBlockProvider.dataBlock(DataBlock.PositionType.FOOTER, 4);

        dataBlock.addAll("content_1", "content_2", "content_3");
        dataBlock.addAll("content_4", "content_5", "content_6");
        header.addAll("header_1", "header_3", "header_3");
        footer.addAll("footer_1", "footer_2", "footer_3");
        footer2.addAll("footer_4", "footer_5", "footer_6");
        dataBlockProvider.addAllToContent("content_7", "content_8", "content_9");

        dataBlockProvider.remove(9);
        dataBlockProvider.move(2, 7);
        System.out.println("footer_2 => " + dataBlockProvider.indexOf("footer_2"));

        System.out.println(dataBlockProvider + "");
        System.out.println(dataBlockProvider.lastContentDataBlock());

        ListIterator<String> iterator = dataBlockProvider.listIterator(5);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            iterator.remove();
        }
        List<String> test = dataBlockProvider.subList(2, 5);
        System.out.println(test);
        test.add(1,"1123465");
        System.out.println(test);
    }
}
