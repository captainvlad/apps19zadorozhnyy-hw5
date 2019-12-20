package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;
import java.util.ArrayList;

public class AsIntStream implements IntStream {

    private AsIntStream finale;
    private ArrayList<Object> operations = new ArrayList<>();
    private boolean TerminalUsed = false;
    private int[] stream;

    private AsIntStream(int... arg){
        stream = arg;
    }

    public static IntStream of(int... values) {

        return new AsIntStream(values);
    }

    private void CheckEmpty(){
        if (count() == 0){
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
        CheckEmpty();
        finale = run();
        Integer max = finale.toArray()[0];
        for (int item: finale.toArray()) {
            max = item > max ? item: max;
        }
        return max;
    }

    @Override
    public Integer min() {
        CheckEmpty();

        finale = run();
        Integer min = finale.toArray()[0];
        for (int item: finale.toArray()) {
            min = item < min ? item: min;
        }
        return min;
    }

    @Override
    public long count() {
        return run().toArray().length;
    }

    @Override
    public Integer sum() {
        CheckEmpty();
        Integer sum = 0;

        for (int item: run().stream) {
            sum += item;
        }

        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        if (!TerminalUsed) {
            operations.add(predicate);
            return this;
        }
        int k = 0;
        int p = 0;
        for (int item: stream) {
            k = predicate.test(item) ? k + 1 : k;
        }
        int[] result = new int[k];

        for (int item: stream) {
            if (predicate.test(item)){
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
        if( !TerminalUsed ){
            operations.add(mapper);
            return this;
        }
        int[] new_stream = stream.clone();
        for (int i = 0; i < new_stream.length; i++) {
            new_stream[i] = mapper.apply(new_stream[i]);
        }
        stream = new_stream;
        return of(new_stream);
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        if (!TerminalUsed) {
            operations.add(func);
            return this;
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int item : stream) {
            for (int sub_item : func.applyAsIntStream(item).toArray()) {
                result.add(sub_item);
            }
        }
        int final_res[] = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            final_res[i] = result.get(i);
        }
        stream = final_res;
        return of(final_res);
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        finale = run();
        int res = identity;
        for (int i = 0; i < count() - 1; i += 2) {
            res += op.apply(finale.toArray()[i], finale.toArray()[i + 1]);
        }
        res = count() % 2 == 1 ? res + finale.toArray()[(int)finale.count() - 1] : res;
        return res;
    }

    @Override
    public int[] toArray() {
        return run().stream;
    }

    private AsIntStream run(){
        TerminalUsed = true;
        AsIntStream finale = this;
        for (Object item : operations) {
            if (item instanceof IntPredicate) {
                finale = (AsIntStream) this.filter( (IntPredicate) item);
            }
            else if (item instanceof IntUnaryOperator){
                finale = (AsIntStream) this.map( (IntUnaryOperator) item);
            }
            else if (item instanceof IntToIntStreamFunction){
                finale = (AsIntStream) this.flatMap( (IntToIntStreamFunction) item);
            }
        }
        operations.clear();
        return finale;
    }
}