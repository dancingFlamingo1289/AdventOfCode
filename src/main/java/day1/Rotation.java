package day1;

public class Rotation {
    private Direction direction;
    private int amount;

    public Rotation(String r) {
        String rotation = r.toLowerCase();
        this.direction = rotation.charAt(0) == 'r' ? Direction.RIGHT : Direction.LEFT;
        this.amount = Integer.parseInt(rotation.substring(1));
    }

    public Direction getDirection() {
        return direction;
    }

    public int getAmount() {
        return amount;
    }
}
