package utils.fileReaders;

import utils.files.BaseFile;
import utils.files.medias.Image;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class ImageFileReader extends FileReader {
    private static ImageFileReader instance;

    private ImageFileReader() {}

    @Override
    public BaseFile loadFromResources(String fileName, ClassLoader classLoader) throws FileReaderException {
        try {
            Path path = Path.of(
                    Objects.requireNonNull(classLoader.getResource(fileName))
                            .toURI()
            );

            return new Image(path.toFile().getAbsolutePath(), "");
        } catch (IOException | NullPointerException | URISyntaxException | ClassNotFoundException e) {
            throw new FileReaderException("Error while loading : " + fileName, e);
        }
    }

    public static ImageFileReader getInstance() {
        if (instance == null) {
            instance = new ImageFileReader();
        }
        return instance;
    }
}
