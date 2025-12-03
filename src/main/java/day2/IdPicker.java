package day2;

import utils.fileReaders.FileReaderException;
import utils.fileReaders.TabularFileReader;
import utils.fileReaders.TextFileReader;
import utils.files.TabularFile;
import utils.files.TextFile;

import java.io.IOException;
import java.util.*;

public class IdPicker {
    private static final String BASE_PATH = "src/";
    private static final String IDS_FILE_NAME = "idsSource.csv";
    private static final String LOG_FILE_NAME = "ids.log";

    public static TextFile loadFromRessources(String fileName) throws FileReaderException {
        return (TextFile) TextFileReader.getInstance().loadFromResources(fileName, IdPicker.class.getClassLoader());
    }

//    private static boolean isValidId(String id) {
//        Set<Character> visitedCharacters = new HashSet<>();
//        for (char c : id.toCharArray()) {
//            if (visitedCharacters.contains(c)) {
//                return false;
//            }
//            visitedCharacters.add(c);
//        }
//
//        return !id.isEmpty();
//    }

    private static boolean isValidId_firstMethod(String id) {
        int len = id.length();

        // si longueur impaire → pas possible d’être deux motifs identiques
        if (len % 2 != 0) return true; // true = valide

        String firstHalf = id.substring(0, len / 2);
        String secondHalf = id.substring(len / 2);

        // si les deux moitiés sont identiques → invalide
        return !firstHalf.equals(secondHalf);
    }

    private static boolean isInvalidId(String id) {
        int len = id.length();

        // teste toutes les longueurs possibles du motif
        for (int subLen = 1; subLen <= len / 2; subLen++) {
            if (len % subLen != 0) continue; // la longueur doit être un multiple du motif
            String pattern = id.substring(0, subLen);
            StringBuilder repeated = new StringBuilder();
            int times = len / subLen;
            for (int i = 0; i < times; i++) {
                repeated.append(pattern);
            }
            if (repeated.toString().equals(id)) {
                return true; // le motif se répète au moins 2 fois
            }
        }
        return false; // aucun motif ne se répète
    }

    public static void main(String[] args) throws IOException {
        TabularFile idFile = null;
        TextFile logsFile = null;

//        try {
//            logsFile = loadFromRessources(LOG_FILE_NAME);
//        } catch (FileReaderException e) {
//            e.printStackTrace();
//            logsFile = new TextFile(BASE_PATH + IDS_FILE_NAME);
//            logsFile.write();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            idFile = (TabularFile) TabularFileReader.getInstance().loadFromResources(IDS_FILE_NAME, IdPicker.class.getClassLoader());
        } catch (FileReaderException e) {
            idFile = new TabularFile(BASE_PATH + IDS_FILE_NAME);
            idFile.write();
        }

        System.out.println("\u001B[5;33m===== IdPicker started =====\u001B[0m");
        String[] ids = idFile.getContentString().getFirst().split(",");
        System.out.println(Arrays.toString(ids));
        List<String[]> splitIds = new ArrayList<>();
        for (String twoIds : ids) {
            splitIds.add(twoIds.split("-"));
        }

        for (String[] splitId : splitIds) {
            System.out.print(Arrays.toString(splitId) + ",");
        }
        System.out.println();

        Set<Long> invalidIds = new HashSet<>();
        long invalidSum = 0;

        // Part 1
        for (String[] splitId : splitIds) {
            String firstId = splitId[0];
            String secondId = splitId[1];
            long id0 = Long.parseLong(firstId), id1 = Long.parseLong(secondId);

            for (long id = id0; id <= id1; id++) {
                if (!isValidId_firstMethod(id + "")) {
                    boolean success = invalidIds.add(id);
                    if (success) {
                        invalidSum += id;
                    }
                }
            }
        }
        System.out.println("The invalid ids : " + invalidIds);
        System.out.println("The invalid id sum is : " + invalidSum);

        // Part 2
        invalidIds = new HashSet<>();
        invalidSum = 0;
        for (String[] splitId : splitIds) {
            String firstId = splitId[0];
            String secondId = splitId[1];
            long id0 = Long.parseLong(firstId), id1 = Long.parseLong(secondId);

            for (long id = id0; id <= id1; id++) {
                if (isInvalidId(id + "")) {
                    boolean success = invalidIds.add(id);
                    if (success) {
                        invalidSum += id;
                    }
                }
            }
        }
        System.out.println("The invalid ids : " + invalidIds);
        System.out.println("The invalid id sum is : " + invalidSum);
        System.out.println("\u001B[5;33m===== IdPicker finished =====\u001B[0m");
    }
}
