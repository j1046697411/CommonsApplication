package org.jzl.lang.util;

import org.jzl.lang.builder.Builder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public final class StringJoiner implements CharSequence, Serializable, Builder<String> {

    private final CharSequence prefix;
    private final CharSequence delimiter;
    private final CharSequence suffix;

    private StringBuilder value;
    private String emptyValue;

    StringJoiner(CharSequence delimiter) {
        this(StringUtils.EMPTY, delimiter, StringUtils.EMPTY);
    }

    StringJoiner(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        ObjectUtils.requireNonNull(prefix);
        ObjectUtils.requireNonNull(delimiter);
        ObjectUtils.requireNonNull(suffix);

        this.prefix = prefix;
        this.delimiter = delimiter;
        this.suffix = suffix;

        this.emptyValue = prefix.toString() + suffix.toString();
    }

    public <T> StringJoiner join(T text) {
        prepareBuilder().append(text);
        return this;
    }

    public StringJoiner join(char[] chars, int offset, int length) {
        prepareBuilder().append(chars, offset, length);
        return this;
    }

    public StringJoiner join(char[] chars) {
        prepareBuilder().append(chars);
        return this;
    }

    public <K, V> StringJoiner join(CharSequence delimiter, K key, V value) {
        prepareBuilder().append(key).append(delimiter).append(value);
        return this;
    }

    @SafeVarargs
    public final <T> StringJoiner joins(T... texts) {
        if (ArrayUtils.nonEmpty(texts)) {
            for (T text : texts) {
                prepareBuilder().append(text);
            }
        }
        return this;
    }

    public <T> StringJoiner joins(Collection<T> texts) {
        if (CollectionUtils.nonEmpty(texts)) {
            for (T text : texts) {
                prepareBuilder().append(text);
            }
        }
        return this;
    }

    public <K, V> StringJoiner joins(CharSequence delimiter, Map<K, V> map) {
        if (MapUtils.nonEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                prepareBuilder().append(entry.getKey()).append(delimiter).append(entry.getValue());
            }
        }
        return this;
    }

    private StringBuilder prepareBuilder() {
        if (value != null) {
            value.append(delimiter);
        } else {
            value = new StringBuilder(prefix);
        }
        return value;
    }

    @Override
    public int length() {
        if (value == null) {
            return emptyValue.length();
        } else {
            return value.length() + suffix.length();
        }
    }

    @Override
    public char charAt(int index) {
        if (value == null) {
            return emptyValue.charAt(index);
        } else {
            int valueLength = value.length();
            if (index < valueLength) {
                return value.charAt(index);
            } else if (index - valueLength < suffix.length()) {
                return suffix.charAt(index - valueLength);
            }
        }
        throw new StringIndexOutOfBoundsException(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (value == null) {
            return emptyValue.subSequence(start, end);
        } else {
            int valueLength = value.length();
            if (end <= valueLength) {
                return value.subSequence(start, end);
            } else if (start > valueLength) {
                return suffix.subSequence(start - valueLength, end - valueLength);
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append(value.subSequence(start, valueLength));
                builder.append(suffix.subSequence(0, end - valueLength));
                return builder;
            }
        }
    }

    @Override
    public String toString() {
        if (value == null) {
            return emptyValue;
        } else {
            if (StringUtils.isEmpty(prefix)) {
                return value.toString();
            } else {
                int initialLength = value.length();
                String result = value.append(suffix).toString();
                value.setLength(initialLength);
                return result;
            }
        }
    }

    @Override
    public String build() {
        return toString();
    }
}
