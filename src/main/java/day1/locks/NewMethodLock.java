package day1.locks;

import day1.Rotation;

import java.util.ArrayList;

public class NewMethodLock extends Lock {
    private final ArrayList<Rotation> passcode;

    public NewMethodLock(int start, int min, int max) {
        super(start, min, max);
        passcode = new ArrayList<>();
    }

    public NewMethodLock(int start) {
        this(start, 0, 99);  // Appel au constructeur principal
    }

    public NewMethodLock() {
        this(0);
    }

    @Override
    public void addRotation(Rotation rotation) {
        int range = getRange();
        int amount = rotation.getDirection().getValue() * rotation.getAmount();
        setCurrentNumber(((getCurrentNumber() + amount) % range + range) % range);
        passcode.addLast(rotation);
    }

    @Override
    public int obtainPasscode() {
        int count = 0;

        int range = getRange();

        int previous = getStart();

        for (Rotation r : passcode) {

            int direction = r.getDirection().getValue(); // +1 = R, -1 = L
            int steps = r.getAmount();

            // --- simulate exactly the rotation ---
            for (int i = 0; i < steps; i++) {
                previous = ((previous + direction) % range + range) % range;

                if (previous == 0) {
                    count++;  // passage par 0 exact
                }
            }

            // NOTE : inutile d’ajouter count++ ici, car
            // si current == 0, la boucle l’a déjà compté.
        }

        return count;
    }
}
