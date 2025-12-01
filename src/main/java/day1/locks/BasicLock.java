package day1.locks;

import day1.Rotation;

import java.util.ArrayList;

public class BasicLock extends Lock {
    private ArrayList<Integer> passcode;

    public BasicLock(int start, int min, int max) {
        super(start, min, max);
        passcode = new ArrayList<>();
    }

    public BasicLock(int start) {
        this(start, 0, 99);  // Appel au constructeur principal
    }

    public BasicLock() {
        this(0);
    }

    public String toString() {
        return "Current number: " + getCurrentNumber();
    }

    public void addRotation(Rotation rotation) {
        int amount = rotation.getDirection().getValue() * rotation.getAmount();
        int range = getRange();
        setCurrentNumber(((getCurrentNumber() + amount) % range + range) % range);
        passcode.addLast(getCurrentNumber());
    }

    public int obtainPasscode() {
        int count = 0;
        for (int num : passcode) {
            if (num == 0) count++;
        }
        return count;
    }
}
