package day1.locks;

import day1.Rotation;

public abstract class Lock {
    private int currentNumber;
    private int min = 0, max = 99, range;
    private int start = 50;

    public Lock(int start, int min, int max) {
        this.start = start;
        this.currentNumber = start;
        this.min = min;
        this.max = max;
        this.range = max - min + 1;
    }

    public Lock(int start) {
        this(start, 0, 99);  // Appel au constructeur principal
    }

    public Lock() {
        this(0);
    }

    public String toString() {
        return "Current number: " + currentNumber;
    }

    public void addRotation(Rotation rotation) {
        int amount = rotation.getDirection().getValue() * rotation.getAmount();
        currentNumber = ((currentNumber + amount) % range + range) % range;
    }

    public abstract int obtainPasscode();

    protected int getCurrentNumber() {
        return currentNumber;
    }

    protected void setCurrentNumber(int newNumber) {
        this.currentNumber = newNumber;
    }

    protected int getStart() {
        return start;
    }
    protected int getMin() {
        return min;
    }

    protected int getMax() {
        return max;
    }

    protected int getRange() {
        return range;
    }
}
