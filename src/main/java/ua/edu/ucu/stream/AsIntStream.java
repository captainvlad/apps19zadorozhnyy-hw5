package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;
import java.util.ArrayList;

public class AsIntStream implements IntStream {

    private ArrayList<Object> operations = new ArrayList<>();
    private boolean terminalUsed = false;
    private int[] stream;
    private int[] stream2;

    private AsIntStream(int... arg) {
        stream = arg;
        stream2 = arg;
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
        run();
        double result = (double) sum() / count();
        stream = stream2;
        return result;
    }

    @Override
    public Integer max() {
        checkEmpty();
        run();
        int[] items = toArray();
        Integer max = items[0];
        for (int item: items) {
            if (item > max) {
                max = item;
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        checkEmpty();

        run();
        int[] items = toArray();
        Integer min = items[0];
        for (int item: items) {
            if (item < min) {
                min = item;
            }
        }
        return min;
    }

    @Override
    public long count() {

        long result = run().toArray().length;
        stream = stream2;
        return result;
    }

    @Override
    public Integer sum() {
        checkEmpty();
        Integer sum = 0;

        for (int item: run().stream) {
            sum += item;
        }
        stream = stream2;
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
        stream = newStream;
        return this;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        if (!terminalUsed) {
            operations.add(func);
            return this;
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int item : stream) {
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
        run();
        int res = identity;
        for (int i = 0; i < count() - 1; i += 2) {
            res += op.apply(toArray()[i], toArray()[i + 1]);
        }
        if (count() % 2 == 1) {
            res += toArray()[(int) count() - 1];
        }
        stream = stream2;
        return res;
    }

    @Override
    public int[] toArray() {
        int[] result = run().stream;
        stream = stream2;
        return result;
    }

    private AsIntStream run() {
        terminalUsed = true;
        for (Object item : operations) {
            if (item instanceof IntPredicate) {
                filter((IntPredicate) item);
            }
            else if (item instanceof IntUnaryOperator) {
                map((IntUnaryOperator) item);
            }
            else if (item instanceof IntToIntStreamFunction) {
                flatMap((IntToIntStreamFunction) item);
            }
        }
        operations.clear();
        return this;
    }
}