package seedu.address.commons.util;

/**
 *
 * @param <T>
 */
public class Pair<T> {

    private T key;

    private T value;

    public Pair(T key, T value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        Pair<T> other = (Pair<T>) obj;
        return this.getKey().equals(other.getKey()) && this.getValue().equals(other.getValue());
    }



}
