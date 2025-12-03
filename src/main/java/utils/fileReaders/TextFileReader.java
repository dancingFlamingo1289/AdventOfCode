package utils.fileReaders;

import utils.files.BaseFile;
import utils.files.TextFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class TextFileReader extends FileReader {
    private static TextFileReader instance;

    private TextFileReader() {}

    @Override
    public BaseFile loadFromResources(String fileName, ClassLoader classLoader) throws FileReaderException {
        try {
            Path path = Path.of(
                    Objects.requireNonNull(classLoader.getResource(fileName))
                            .toURI()
            );
            return new TextFile(path.toFile().getAbsolutePath());
        } catch (URISyntaxException | IOException e) {
            throw new FileReaderException("Error while loading : " + fileName, e);
        }
    }

    public static TextFileReader getInstance() {
        if (instance == null) {
            instance = new TextFileReader();
        }
        return instance;
    }
}
