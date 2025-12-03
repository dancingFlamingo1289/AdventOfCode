package utils.fileReaders;

import utils.files.BaseFile;
import utils.files.BinaryFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class BinaryFileReader extends FileReader {
    private static BinaryFileReader instance;

    private BinaryFileReader() {}

    @Override
    public BaseFile loadFromResources(String fileName, ClassLoader classLoader) throws FileReaderException {
        try {
            Path path = Path.of(
                    Objects.requireNonNull(classLoader.getResource(fileName))
                            .toURI()
            );

            return new BinaryFile(new File(path.getParent().toString()), path.toFile().getName(), ".");
        } catch (URISyntaxException | IOException | ClassNotFoundException e) {
            throw new FileReaderException("Error while loading : " + fileName, e);
        }
    }

    public static BinaryFileReader getInstance() {
        if (instance == null) {
            instance = new BinaryFileReader();
        }
        return instance;
    }
}
