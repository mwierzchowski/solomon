package solomon.utils;

import lombok.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface Iterables {
    @SafeVarargs
    static <T> Iterable<T> join(List<T>... lists) {
        return () -> new JoinedIterator<>(lists, 1);
    }

    @SafeVarargs
    static <T> Iterable<T> joinReverse(List<T>... lists) {
        return () -> new JoinedIterator<>(lists, -1);
    }

    class JoinedIterator<T> implements Iterator<T> {
        private final List<T>[] lists;
        private final int step;
        private int currentListIndex;
        private int currentItemIndex;

        public JoinedIterator(@NonNull List<T>[] lists, int step) {
            if (step == 0) {
                throw new IllegalArgumentException("Step cannot be zero");
            }
            this.lists = lists;
            this.step = step;
            if (isForward()) {
                this.currentListIndex = 0;
                this.currentItemIndex = -1;
            } else {
                this.currentListIndex = this.lists.length - 1;
                this.currentItemIndex = safeListSize(this.currentListIndex);
            }
        }

        @Override
        public boolean hasNext() {
            return nextIndex().isPresent();
        }

        @Override
        public T next() {
            var nextIndex = nextIndex().orElseThrow(IndexOutOfBoundsException::new);
            this.currentListIndex = nextIndex.list();
            this.currentItemIndex = nextIndex.item();
            return this.lists[this.currentListIndex].get(this.currentItemIndex);
        }

        private Optional<Index> nextIndex() {
            int listIndex = this.currentListIndex;
            int itemIndex = this.currentItemIndex + this.step;
            while (true) {
                if (listIndex < 0 || listIndex >= this.lists.length) {
                    return Optional.empty();
                }
                if (itemIndex < 0 || this.lists[listIndex] == null || itemIndex >= this.lists[listIndex].size()) {
                    listIndex += this.step;
                    if (isForward()) {
                        itemIndex = 0;
                    } else {
                        itemIndex = safeListSize(listIndex) - 1;
                    }
                    continue;
                }
                var index = new Index(listIndex, itemIndex);
                return Optional.of(index);
            }
        }

        private boolean isForward() {
            return this.step > 0;
        }

        private int safeListSize(int listIndex) {
            if (listIndex < 0 || listIndex >= this.lists.length || this.lists[listIndex] == null) {
                return -1;
            } else {
                return this.lists[listIndex].size();
            }
        }

        private record Index(int list, int item) {}
    }
}
