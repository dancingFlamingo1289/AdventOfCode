package utils.fileReaders;

import utils.files.BaseFile;
import utils.files.medias.Audio;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class AudioFileReader extends FileReader {
    private static AudioFileReader instance;

    private AudioFileReader() {}

    @Override
    public BaseFile loadFromResources(String fileName, ClassLoader classLoader) throws FileReaderException {
        try {
            Path path = Path.of(
                    Objects.requireNonNull(classLoader.getResource(fileName))
                            .toURI()
            );

            return new Audio(path.toFile().getParentFile(), path.toFile().getName());
        } catch (NullPointerException | URISyntaxException e) {
            throw new FileReaderException("Error while loading : " + fileName, e);
        }
    }

    public static AudioFileReader getInstance() {
        if (instance == null) {
            instance = new AudioFileReader();
        }
        return instance;
    }
}
