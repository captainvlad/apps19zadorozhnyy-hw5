package ua.edu.ucu.iterators;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BasicIterator implements Iterator {

    private int[] values;
    private int current = 0;

    public BasicIterator(int... values) {
        this.values = values;
    }


    @Override
    public boolean hasNext() {
        return current < values.length;
    }

    @Override
    public Object next() {
        if (!hasNext()){
            throw new NoSuchElementException("No values left!");
        }
        int result = values[current];
        current += 1;
        return result;
    }
}
