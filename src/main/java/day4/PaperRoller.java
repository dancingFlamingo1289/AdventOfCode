package day4;

import day2.IdPicker;
import utils.fileReaders.FileReaderException;
import utils.fileReaders.TextFileReader;
import utils.files.TextFile;

public class PaperRoller {
    private static final String BASE_PATH = "src/";
    private static final String ROLLS_FILE_NAME = "paperRolls.txt";

    public static TextFile loadFromRessources(String fileName) throws FileReaderException {
        return (TextFile) TextFileReader.getInstance().loadFromResources(fileName, PaperRoller.class.getClassLoader());
    }



    public static void main(String[] args) throws FileReaderException {
        TextFile rollsFile = loadFromRessources(ROLLS_FILE_NAME);

        System.out.println(rollsFile.getLineCount());
        for (String line : rollsFile) {
            for (int i = 0 ; i < line.length() ; i++) {

            }
        }
    }
}
