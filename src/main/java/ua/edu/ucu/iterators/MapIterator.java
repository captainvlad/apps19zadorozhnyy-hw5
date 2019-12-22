package ua.edu.ucu.iterators;
import ua.edu.ucu.function.IntUnaryOperator;
import java.util.Iterator;

public class MapIterator implements Iterator {
    private IntUnaryOperator operator;
    private Iterator argument;

    public MapIterator(Iterator argument, IntUnaryOperator operator){
        this.argument = argument;
        this.operator = operator;
    }

    @Override
    public boolean hasNext() {
        return argument.hasNext();
    }

    @Override
    public Object next() {
        return operator.apply((Integer) argument.next());
    }
}
