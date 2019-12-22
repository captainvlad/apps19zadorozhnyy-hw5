package ua.edu.ucu.iterators;

import ua.edu.ucu.function.IntPredicate;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilterIterator implements Iterator {
    private Iterator previous;
    private IntPredicate predicate;
    private int nextValue;



    public FilterIterator(Iterator argument, IntPredicate predicat) {

        previous = argument;
        predicate = predicat;
    }


    @Override
    public boolean hasNext() {
        while (previous.hasNext()) {
            int current = (Integer) previous.next();
            if (predicate.test(current)) {
                nextValue = current;
                return true;
            }
        }
        return false;
    }

    @Override
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No values left!");
        }
        return nextValue;
    }
}
