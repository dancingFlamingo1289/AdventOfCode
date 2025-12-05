package day5;

import utils.fileReaders.FileReaderException;
import utils.fileReaders.TextFileReader;
import utils.files.TextFile;

import java.util.ArrayList;
import java.util.List;

public class IngredientSelector {
    private static final String INGREDIENTS_IDS_FILE_NAME = "ingredients.txt";

    public static TextFile loadFromRessources(String fileName) throws FileReaderException {
        return (TextFile) TextFileReader.getInstance().loadFromResources(fileName, IngredientSelector.class.getClassLoader());
    }

    public static List<Long[]> getRanges(TextFile textFile) throws FileReaderException {
        long currentIndex = 0;
        String currentLine = textFile.getLine((int) currentIndex).trim();
        List<Long[]> ranges = new ArrayList<>();

        while (!currentLine.isEmpty()) {
            String[] tokens = currentLine.split("-");
            if (tokens.length != 2) {
                throw new IllegalArgumentException("Invalid ingredient line: " + currentLine);
            }

            ranges.add(new Long[]{Long.parseLong(tokens[0]), Long.parseLong(tokens[1])});

            currentIndex++;
            currentLine = textFile.getLine((int) currentIndex).trim();
        }

        ranges.add(new Long[]{currentIndex});

        return ranges;
    }

    public static List<Long> getIds(TextFile textFile, long firstIdIndex) throws FileReaderException {
        List<Long> ids = new ArrayList<>();
        for (long i = firstIdIndex ; i < textFile.getLineCount(); i++) {
            ids.add(Long.parseLong(textFile.getLine((int) i)));
        }
        return ids;
    }

    public static boolean isInsideRanges(List<Long[]> ranges, long id) {
        for (Long[] range : ranges) {
            if (range.length != 2) {
                throw new IllegalArgumentException("Invalid ingredient line: " + range);
            }

            if (range[0] <= id && id <= range[1]) {
                return true;
            }
        }

        return false;
    }

    public static boolean mergeIntervals(List<Long[]> ranges) {
        if (ranges.size() <= 1) {
            return false; // rien à fusionner
        }

        // 1. Trier les intervalles par début
        ranges.sort((a, b) -> Long.compare(a[0], b[0]));

        List<Long[]> merged = new ArrayList<>();
        Long[] current = ranges.get(0);

        boolean mergedSomething = false;

        // 2. Balayer et fusionner
        for (int i = 1; i < ranges.size(); i++) {
            Long[] next = ranges.get(i);

            // si les intervalles se chevauchent ou se touchent :
            // [a, b] et [c, d] se chevauchent si c <= b
            if (next[0] <= current[1]) {
                // fusionner : mettre fin = max(b, d)
                current[1] = Math.max(current[1], next[1]);
                mergedSomething = true;
            } else {
                // pas de chevauchement → ajouter l'intervalle courant
                merged.add(current);
                current = next;
            }
        }

        // ajouter le dernier intervalle fusionné
        merged.add(current);

        // 3. Remplacer la liste originale
        ranges.clear();
        ranges.addAll(merged);

        return mergedSomething;
    }

    public static long getNumberOfIdsFromRange(Long[] range) {
        return range[1] - range[0] + 1;
    }

    public static void main(String[] args) throws FileReaderException {
        TextFile freshIngredientsIdFile = loadFromRessources(INGREDIENTS_IDS_FILE_NAME);
        List<Long[]> ranges = getRanges(freshIngredientsIdFile);
        long firstIndex = ranges.removeLast()[0] + 1;
        List<Long> ids = getIds(freshIngredientsIdFile, firstIndex);

        // Part 1
        List<Long> freshIds = new ArrayList<>();
        for (long id : ids) {
            if (isInsideRanges(ranges, id)) {
                freshIds.add(id);
                //System.out.println("Found id: " + id);
            }
        }

        System.out.println("Fresh ids : " + freshIds);
        System.out.println("Number of fresh ingredients : " + freshIds.size());

        // Part 2
        mergeIntervals(ranges);
        long numberOfFreshIds = 0;
        for (Long[] range : ranges) {
            numberOfFreshIds += getNumberOfIdsFromRange(range);
        }
        System.out.println("Number of fresh ingredients : " + numberOfFreshIds);
    }
}
