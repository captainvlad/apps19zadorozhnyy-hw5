package ua.edu.ucu.stream;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AsIntStreamTest {
    private IntStream example;
    private IntStream example_2 = AsIntStream.of();

    @Test
    public void of() {
        example = AsIntStream.of(1, 2, 3, 4);
        IntStream example_2 = AsIntStream.of();
        assert (example instanceof AsIntStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void average() {
        example = AsIntStream.of(1, 2, 3, 4);
        assertEquals(example.average(), 2.5, 0.1);
        example_2.average();
    }

    @Test(expected = IllegalArgumentException.class)
    public void max() {
        example = AsIntStream.of(1, 2, 3, 4);
        assertEquals(example.max().intValue(), 4);
        example_2.max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void min() {
        example = AsIntStream.of(1, 2, 3, 4);
        assertEquals(example.min().intValue(), 1);
        example_2.min();
    }

    @Test
    public void count() {
        example = AsIntStream.of(1, 2, 3, 4);
        assertEquals(example.count(), 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sum() {
        example = AsIntStream.of(1, 2, 3, 4);
        assertEquals(example.sum().intValue(), 10);
        example_2.sum();
    }

    @Test
    public void filter() {
        example = AsIntStream.of(-4,-3, -2, -1, 0, 1, 2, 3, 4);
        IntStream result = example.filter(x -> x > 1);
        assertEquals(example, result); // not changed object
        assertArrayEquals(result.toArray(), new int[] {2, 3, 4} );
        // changed after calling terminal method
    }

    @Test
    public void map() {
        example = AsIntStream.of(-4,-3, -2, -1, 0, 1, 2, 3, 4);
        IntStream result = example.map(x -> x * x);
        assertEquals(example, result); // not changed object
        assertArrayEquals(example.toArray(), new int[] {16, 9, 4, 1, 0, 1, 4, 9, 16});
        // changed after calling terminal method
    }

    @Test
    public void flatMap() {
        example = AsIntStream.of(1, 4, 9);
        IntStream result = example.flatMap(x -> AsIntStream.of(x - 1, x, x + 1));
        assertEquals(example, result); // not changed object
        assertArrayEquals(result.toArray(), new int[] {0, 1, 2, 3, 4, 5, 8, 9, 10});
        // changed after calling terminal method
    }

    @Test
    public void reduce() {
        example = AsIntStream.of(0, 1, 2, 3, 4, 5, 8, 9, 10);
        int result = example.reduce(0, (sum, x) -> sum += x);
        assertEquals(result, 42);
    }

    @Test
    public void toArray() {
        example = AsIntStream.of(0, 1, 2, 3, 4, 5, 8, 9, 10);
        assertArrayEquals(example.toArray(), new int[] {0, 1, 2, 3, 4, 5, 8, 9, 10});
    }
}