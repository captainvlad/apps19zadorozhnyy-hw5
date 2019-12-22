package ua.edu.ucu.stream;

import org.junit.Test;

import static org.junit.Assert.*;

public class AsIntStreamTest {
    private IntStream intStream = AsIntStream.of(-1, 0, 1, 2, 3);;

    @Test
    public void of() {
        intStream = AsIntStream.of(-1, 0, 1, 2, 3);
        assertArrayEquals(intStream.toArray(), new int[]{-1, 0, 1, 2, 3});
    }

    @Test(expected = IllegalArgumentException.class)
    public void Empty(){
        AsIntStream intStrea = (AsIntStream) AsIntStream.of();
        intStrea.reduce(0, (sum, x) -> sum += x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void average() {
        Double avg = intStream.average();
        intStream.average();
        assertEquals(avg, 1, 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void max() {
        intStream = AsIntStream.of(-1, 0, 1, 2, 3);
        assertEquals(intStream.max(), 3, 0.1);
        intStream.max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void min() {
        assertEquals(intStream.min(), -1, 0.1);
        intStream.min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void count() {
        assertEquals(intStream.count(), 5, 0.1);
        intStream.count();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sum() {
        assertEquals(intStream.sum(), 5, 0.1);
        intStream.sum();
    }

    @Test
    public void filter() {
        assertArrayEquals(new int[] {1, 2, 3}, intStream.filter(x -> x > 0).toArray());
    }

    @Test
    public void map() {
        assertArrayEquals(new int[] {1, 0, 1, 4, 9}, intStream.map(x -> x * x).toArray());
    }

    @Test
    public void flatMap() {
        int res[] = intStream
                .filter(x -> x > 0) // 1, 2, 3
                .map(x -> x * x) // 1, 4, 9
                .flatMap(x -> AsIntStream.of(x - 1, x, x + 1)).toArray();
        assertArrayEquals(res, new int[]{0, 1, 2, 3, 4, 5, 8, 9, 10});
    }

    @Test(expected = IllegalArgumentException.class)
    public void reduce() {
        assertEquals(intStream.reduce(0, (sum, x) -> sum += x), 5);
        intStream.reduce(0, (sum, x) -> sum += x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toArray() {
        assertArrayEquals(intStream.toArray(), new int[]{-1, 0, 1, 2, 3});
        intStream.toArray();
    }
}