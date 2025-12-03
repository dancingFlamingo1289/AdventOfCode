package utils.fileReaders;

import utils.files.BaseFile;

public abstract class FileReader {

    protected FileReader() {}

    public abstract BaseFile loadFromResources(String fileName, ClassLoader c) throws FileReaderException;
}
