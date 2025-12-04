package day3;

import utils.fileReaders.FileReaderException;
import utils.fileReaders.TextFileReader;
import utils.files.TextFile;
import java.io.IOException;

public class JoltageProblem {
    private static final String JOLTAGE_FILE_NAME = "joltages.txt";
    private static final boolean DEBUG = true;

    private static void debugPrint(String message) {
        if (DEBUG) {
            System.out.println("[DEBUG] " + message);
        }
    }

    public static int maxTwoDigit(String s) {
        int bestA = -1;
        int best = -1;

        for (int i = 0; i < s.length(); i++) {
            int digit = s.charAt(i) - '0';

            if (bestA != -1) {
                int candidate = bestA * 10 + digit;
                if (candidate > best) best = candidate;
            }

            if (digit > bestA) {
                bestA = digit;
            }
//            debugPrint(bestA + "");
//            debugPrint(best + "");
        }

        return best;
    }

    public static long maxDigit(String s, int nDigits) {
        int n = s.length();
        int toRemove = n - nDigits; // combien on peut enlever
        int[] stack = new int[n];
        int top = -1; // index du sommet de pile

        for (int i = 0; i < n; i++) {
            int digit = s.charAt(i) - '0';

            // tant qu'on peut enlever et améliorer la valeur finale
            while (top >= 0 && stack[top] < digit && toRemove > 0) {
                top--;
                toRemove--;
            }

            stack[++top] = digit;
        }

        // on ne garde que nDigits chiffres
        long result = 0;
        for (int i = 0; i < nDigits; i++) {
            result = result * 10 + stack[i];
        }

        return result;
    }

    public static long sum(long[] table) {
        long result = 0;
        for (long value : table) {
            result += value;
        }
        return result;
    }

    public static int sum(int[] table) {
        int result = 0;
        for (int value : table) {
            result += value;
        }
        return result;
    }

    public static double sum(double[] table) {
        double result = 0;
        for (double value : table) {
            result += value;
        }
        return result;
    }

    public static void main(String[] args) throws FileReaderException, IOException {
        System.out.println("\u001B[5;33m===== JoltageProblem started =====\u001B[0m");
        TextFile joltagesFile = (TextFile) TextFileReader.getInstance()
                .loadFromResources(JOLTAGE_FILE_NAME, JoltageProblem.class.getClassLoader());

        int numRows = joltagesFile.getLineCount();

        // Part 1
        int[] maxTwoDigits = new int[numRows];
        for (int i = 0 ; i < numRows ; i++) {
            String line = joltagesFile.getLine(i);
            maxTwoDigits[i] = maxTwoDigit(line);
            debugPrint("Bank \"" + line + "\" -> " + maxTwoDigits[i]);
        }
        System.out.println(sum(maxTwoDigits));

        // Part 2
        long[] maxTwelveDigits = new long[numRows];
        int nDigits = 12;
        for (int i = 0 ; i < numRows ; i++) {
            String line = joltagesFile.getLine(i);
            maxTwelveDigits[i] = maxDigit(line, nDigits);
            debugPrint("Bank \"" + line + "\" -> " + maxTwelveDigits[i]);
        }
        System.out.println(sum(maxTwelveDigits));

        System.out.println("\u001B[5;33m===== JoltageProblem finished =====\u001B[0m");
    }
}
