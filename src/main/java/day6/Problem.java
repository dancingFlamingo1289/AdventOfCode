package day6;

import java.util.List;

public class Problem {
    private final List<Long> numbers;
    private final Operation operation;

    public Problem(List<Long> numbers, Operation operation) {
        this.numbers = numbers;
        this.operation = operation;
    }

    public String toString() {
        return numbers.toString() + " " + operation.toString();
    }

    public Long solve() {
        return switch (operation) {
            case Operation.ADDITION -> addAll();
            case SUBTRACTION -> subtractAll();
            case MULTIPLICATION -> multiplyAll();
            case DIVISION -> divideAll();
        };
    }

    private Long addAll() {
        long sum = 0;
        for (Long number : numbers) {
            sum += number;
        }
        return sum;
    }

    private Long subtractAll() {
        long dif = numbers.getFirst();
        for (int i = 1; i < numbers.size(); i++) {
            dif -= numbers.get(i);
        }
        return dif;
    }

    private Long multiplyAll() {
        long mul = 1;
        for (Long number : numbers) {
            mul = mul * number;
        }
        return mul;
    }

    private long divideAll() {
        if (numbers.isEmpty()) return 0;
        long div = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            div = div / numbers.get(i);
        }
        return div;
    }
}
