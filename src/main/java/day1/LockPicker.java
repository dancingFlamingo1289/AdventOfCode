package day1;

import day1.locks.BasicLock;
import day1.locks.Lock;
import day1.locks.NewMethodLock;
import utils.fileReaders.FileReader;
import utils.fileReaders.FileReaderException;
import utils.fileReaders.TextFileReader;
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

    public static TextFile loadFromRessources(String fileName) throws FileReaderException {
        return (TextFile) TextFileReader.getInstance().loadFromResources(fileName, LockPicker.class.getClassLoader());
    }

    public static void main(String[] args) throws IOException {
        System.out.println("\u001B[5;33m===== LockPicker started =====\u001B[0m");

        TextFile combFile;
        TextFile logFile;

        try {
            combFile = loadFromRessources(LOCK_FILE_NAME);
        } catch (FileReaderException e) {
            combFile = new TextFile(BASE_PATH + LOCK_FILE_NAME);
            combFile.write();
        }

        try {
            logFile = loadFromRessources(LOG_FILE_NAME);
        } catch (Exception e) {
            logFile = new TextFile(BASE_PATH + LOG_FILE_NAME);
            logFile.write();
        }

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

        System.out.println("\u001B[5;33m===== LockPicker finished =====\u001B[0m");
    }
}
