package day1;

import day1.locks.BasicLock;
import day1.locks.Lock;
import day1.locks.NewMethodLock;
import utils.files.BaseFile;
import utils.files.TextFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class LockPicker {
    private static final String BASE_PATH = "src/";
    private static final String LOCK_FILE_NAME = "lockRotations.txt";
    private static final String LOG_FILE_NAME = "lock.log";

    public static TextFile loadFromRessources(String fileName) throws IOException, URISyntaxException {
        Path path = Path.of(
                Objects.requireNonNull(LockPicker.class.getClassLoader()
                                .getResource(LOCK_FILE_NAME))
                        .toURI()
        );
        return new TextFile(path.toFile().getAbsolutePath());
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("===== LockPicker started =====");
        TextFile combFile = loadFromRessources(LOCK_FILE_NAME);
        BaseFile logFile = loadFromRessources(LOG_FILE_NAME);

        // Part 1
        Lock lock1 = new BasicLock(50);
        for (String rotation : combFile) {
            Rotation r = new Rotation(rotation);
            lock1.addRotation(r);
        }
        System.out.println("Part 1 password : " + lock1.obtainPasscode());

        // Part 2
        Lock lock2 = new NewMethodLock(50);
        for (String rotation : combFile) {
            Rotation r = new Rotation(rotation);
            lock2.addRotation(r);
        }
        System.out.println("Part 2 password : " + lock2.obtainPasscode());

        System.out.println("===== LockPicker finished =====");
    }
}
