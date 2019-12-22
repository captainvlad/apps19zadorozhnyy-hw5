package ua.edu.ucu.iterators;
import ua.edu.ucu.function.IntToIntStreamFunction;
import java.util.Iterator;

public class FlatMapIterator implements Iterator {

    private Iterator current = new BasicIterator();
    private Iterator previous;
    private IntToIntStreamFunction action;

    public FlatMapIterator(Iterator iterator, IntToIntStreamFunction function) {
        previous = iterator;
        action = function;
    }

    @Override
    public boolean hasNext() {
        return current.hasNext() || previous.hasNext();
    }

    @Override
    public Integer next() {
        if (!current.hasNext()) {
            current = new BasicIterator(action.applyAsIntStream(
                    (Integer) previous.next()).toArray());
        }
        return (Integer) current.next();
    }
}