package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;
import ua.edu.ucu.iterators.BasicIterator;
import ua.edu.ucu.iterators.FilterIterator;
import ua.edu.ucu.iterators.FlatMapIterator;
import ua.edu.ucu.iterators.MapIterator;

import java.util.ArrayList;
import java.util.Iterator;

public class AsIntStream implements IntStream {
    private Iterator iterator;
    private int size;
    private boolean terminalUsed = false;


    private void checkUsed(){
        if (terminalUsed){
            throw new IllegalArgumentException("Stream is closed");
        }
        terminalUsed = true;
    }

    private void checkEmpty(int len){
        if (len == 0){
            throw new IllegalArgumentException("Empty stream");
        }
    }

    private AsIntStream(int... values){
        iterator = new BasicIterator(values);
        size = values.length;
    }

    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }

    @Override
    public Double average() {
        checkUsed();
        int sum = 0;
        int len = 0;
        while (iterator.hasNext()){
            sum += (Integer) iterator.next();
            len += 1;
        }
        checkEmpty(len);
        return (double) sum / len;
    }

    @Override
    public Integer max() {
        checkUsed();
        return reduce(Integer.MIN_VALUE, (sum, x) -> sum = Math.max(x, sum));
    }

    @Override
    public Integer min() {
        checkUsed();
        return reduce(Integer.MAX_VALUE, (sum, x) -> sum = Math.min(x, sum));
    }

    @Override
    public long count() {
        checkUsed();
        return toArray().length;
    }

    @Override
    public Integer sum() {
        checkUsed();
        int amount = 0;
        int summ = 0;
        while (iterator.hasNext()){
            summ += (Integer) iterator.next();
            amount += 1;
        }
        checkEmpty(amount);
        return summ;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        AsIntStream result = (AsIntStream) of(new int[size]);
        result.iterator = new FilterIterator(iterator, predicate);
        return result;
    }

    @Override
    public void forEach(IntConsumer action) {
        while (iterator.hasNext()){
            action.accept((Integer) iterator.next());
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        AsIntStream result = (AsIntStream) of(new int[size]);
        result.iterator = new MapIterator(iterator, mapper);
        return result;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        AsIntStream result = (AsIntStream) of(new int[size]);
        result.iterator = new FlatMapIterator(iterator, func);
        return result;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        checkUsed();
        int res = identity;
        int amount = 0;
        while (iterator.hasNext()){
            res = op.apply(res, (Integer) iterator.next());
            amount += 1;
        }
        checkEmpty(amount);

        return res;
    }

    @Override
    public int[] toArray() {
        checkUsed();
        ArrayList result = new ArrayList();
        while (iterator.hasNext()){
            result.add(iterator.next());
        }
        int[] resultFinal = new int[result.size()];
        for (int i = 0; i < resultFinal.length; i++) {
            resultFinal[i] = (int) result.get(i);
        }
        return resultFinal;
    }
}