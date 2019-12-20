package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;
import java.util.ArrayList;

public class AsIntStream implements IntStream {

    private AsIntStream finale;
    private ArrayList<Object> operations = new ArrayList<>();
    private static boolean terminalUsed = false;
    public int[] stream;

    private AsIntStream(int... arg) {
        stream = arg;
        finale = this;
    }

    public static IntStream of(int... values) {

        return new AsIntStream(values);
    }

    private void checkEmpty() {
        if (count() == 0) {
            throw new IllegalArgumentException("Empty stream");
        }
    }

    @Override
    public Double average() {
        finale = run();
        return (double) finale.sum() / finale.count();
    }

    @Override
    public Integer max() {
        checkEmpty();
        finale = run();
        Integer max = finale.toArray()[0];
        for (int item: finale.toArray()) {
            if (item > max) {
                max = item;
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        checkEmpty();

        finale = run();
        Integer min = finale.toArray()[0];
        for (int item: finale.toArray()) {
            if (item < min) {
                min = item;
            }
        }
        return min;
    }

    @Override
    public long count() {
        return run().toArray().length;
    }

    @Override
    public Integer sum() {
        checkEmpty();
        Integer sum = 0;

        for (int item: run().stream) {
            sum += item;
        }

        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        if (!terminalUsed) {
            operations.add(predicate);
            return this;
        }
        int k = 0;
        int p = 0;
        for (int item: stream) {
            if (predicate.test(item)) {
                k += 1;
            }
        }
        int[] result = new int[k];

        for (int item: stream) {
            if (predicate.test(item)) {
                result[p] = item;
                p += 1;
            }
        }
        stream = result;
        return of(result);
    }

    @Override
    public void forEach(IntConsumer action) {
        for (int item: run().stream) {
            action.accept(item);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        if (!terminalUsed) {
            operations.add(mapper);
            return this;
        }
        int[] newStream = stream.clone();
        for (int i = 0; i < newStream.length; i++) {
            newStream[i] = mapper.apply(newStream[i]);
        }
        finale.stream = newStream;
        return finale;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        if (!terminalUsed) {
            operations.add(func);
            return this;
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int item : finale.stream) {
            for (int subItem : func.applyAsIntStream(item).toArray()) {
                result.add(subItem);
            }
        }
        int[] finalRes = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            finalRes[i] = result.get(i);
        }
        stream = finalRes;
        return of(finalRes);
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        finale = run();
        int res = identity;
        for (int i = 0; i < count() - 1; i += 2) {
            res += op.apply(finale.toArray()[i], finale.toArray()[i + 1]);
        }
        if (count() % 2 == 1) {
            res += finale.toArray()[(int) finale.count() - 1];
        }
        return res;
    }

    @Override
    public int[] toArray() {
        return run().stream;
    }

    public AsIntStream run() {
        terminalUsed = true;
        for (Object item : operations) {
            if (item instanceof IntPredicate) {
                finale.filter((IntPredicate) item);
            }
            else if (item instanceof IntUnaryOperator) {
                finale.map((IntUnaryOperator) item);
            }
            else if (item instanceof IntToIntStreamFunction) {
                this.flatMap((IntToIntStreamFunction) item);
            }
        }
        operations.clear();
        return finale;
    }
}