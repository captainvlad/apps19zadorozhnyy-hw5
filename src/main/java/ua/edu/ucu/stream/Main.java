package ua.edu.ucu.stream;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        IntStream intStream = AsIntStream.of(-1, 0, 1, 2, 3); // input values
        int res = intStream
                .filter(x -> x > 0) // 1, 2, 3
                .map(x -> x * x) // 1, 4, 9
                .flatMap(x -> AsIntStream.of(x - 1, x, x + 1)).reduce(Integer.MAX_VALUE, (sum, x) -> sum = Math.min(x, sum));
                // 0, 1, 2, 3, 4, 5, 8, 9, 10

//        System.out.println(Arrays.toString(res.toArray()));
        System.out.println(res);
    }
}
