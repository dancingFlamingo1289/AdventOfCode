package utils.fileReaders;

import utils.files.BaseFile;
import utils.files.TabularFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class TabularFileReader extends FileReader {
    private static TabularFileReader instance;

    private TabularFileReader() {}

    @Override
    public BaseFile loadFromResources(String fileName, ClassLoader classLoader) throws FileReaderException {
        try {
            Path path = Path.of(
                    Objects.requireNonNull(classLoader.getResource(fileName))
                            .toURI()
            );

            return new TabularFile(path.toFile().getAbsolutePath());
        } catch (IOException | NullPointerException | URISyntaxException e) {
            throw new FileReaderException("Error while loading : " + fileName, e);
        }
    }

    public static TabularFileReader getInstance() {
        if (instance == null) {
            instance = new TabularFileReader();
        }
        return instance;
    }
}
