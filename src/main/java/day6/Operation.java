package day6;

public enum Operation {
    ADDITION('+'), SUBTRACTION('-'), MULTIPLICATION('*'), DIVISION('/');

    private final char symbol;
    Operation(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
