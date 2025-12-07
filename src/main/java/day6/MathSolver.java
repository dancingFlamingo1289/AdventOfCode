package day6;

import utils.fileReaders.FileReaderException;
import utils.fileReaders.TextFileReader;
import utils.files.TextFile;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

//class StringInverter {
//    private static void swap(char[] string, int a, int b) {
//        char temp = string[a];
//        string[a] = string[b];
//        string[b] = temp;
//    }
//
//    public static String invert(String input) {
//        char[] chars = input.toCharArray();
//
//        for (int i = 0; i < input.length() / 2; i++) {
//            swap(chars, i, input.length() - 1 - i);
//        }
//
//        return String.valueOf(chars);
//    }
//}

public class MathSolver {
    private static final String MATH_PROBLEMS_FILE_NAME = "mathProblems.txt";

    public static TextFile loadFromRessources(String fileName) throws FileReaderException {
        return (TextFile) TextFileReader.getInstance().loadFromResources(fileName, MathSolver.class.getClassLoader());
    }

    public static Grid<String> extractData(TextFile textFile) {
        int numRows = textFile.getLineCount();          // Inclut la ligne des opérations
        int numCols = textFile.getLine(0).trim().split("\\s+").length;

        // Construire la Grid qui contient TOUT, y compris la ligne des opérations
        Grid<String> grid = new Grid<>(numCols, numRows);

        for (int row = 0; row < numRows; row++) {
            String[] parts = textFile.getLine(row).trim().split("\\s+");

            if (parts.length != numCols) {
                throw new IllegalStateException("Inconsistent row length at row " + row);
            }

            for (int col = 0; col < numCols; col++) {
                grid.set(row, col, parts[col]);
            }
        }

        return grid;
    }

    private static List<Problem> extractProblems(Grid<String> grid) {
        List<Problem> problems = new ArrayList<>();

        for (int i = 0; i < grid.getNumCols(); i++) { // parcourt les lignes transposées
            String[] row = grid.getColumn(i);

            List<Long> numbers = new ArrayList<>();
            Operation op = null;

            for (String v : row) {
                if (v.matches("\\d+")) {
                    numbers.add(Long.parseLong(v));
                } else if (v.matches("[+\\-*/]")) {
                    op = getOperation(op, v);
                }
            }

            if (op == null) throw new IllegalStateException("No operation found in row " + i);

            problems.add(new Problem(numbers, op));
        }

        return problems;
    }

    private static List<ProblemBig> extractProblemsV(Grid<String> grid) {
        List<ProblemBig> problems = new ArrayList<>();

        int numCols = grid.getNumCols();
        int numRows = grid.getNumRows();

        // Identifier les colonnes qui appartiennent aux problèmes
        List<List<Integer>> problemColumns = new ArrayList<>();
        List<Integer> currentProblem = new ArrayList<>();

        for (int col = 0; col < numCols; col++) {
            boolean isEmptyCol = true;
            for (int row = 0; row < numRows - 1; row++) { // ignorer la dernière ligne
                if (!grid.get(row, col).trim().isEmpty()) {
                    isEmptyCol = false;
                    break;
                }
            }

            if (isEmptyCol) {
                if (!currentProblem.isEmpty()) {
                    problemColumns.add(currentProblem);
                    currentProblem = new ArrayList<>();
                }
            } else {
                currentProblem.add(col);
            }
        }
        if (!currentProblem.isEmpty()) problemColumns.add(currentProblem);

        // Parcourir les problèmes de droite à gauche
        for (int p = problemColumns.size() - 1; p >= 0; p--) {
            List<Integer> cols = problemColumns.get(p);
            List<BigInteger> numbers = new ArrayList<>();
            Operation op = null;

            for (int col : cols) {
                StringBuilder numberBuilder = new StringBuilder();
                for (int row = 0; row < numRows - 1; row++) {
                    String val = grid.get(row, col);
                    if (!val.trim().isEmpty()) {
                        numberBuilder.append(val);
                    }
                }
                numbers.add(new BigInteger(numberBuilder.toString()));

                String lastLineVal = grid.get(numRows - 1, col).trim();
                if (!lastLineVal.isEmpty()) {
                    op = getOperation(op, lastLineVal);
                }
            }

            if (op == null) throw new IllegalStateException("No operation found in problem " + p);
            problems.add(new ProblemBig(numbers, op));
        }

        return problems;
    }

    // Version BigInteger de Problem
    public static class ProblemBig {
        private final List<BigInteger> numbers;
        private final Operation operation;

        public ProblemBig(List<BigInteger> numbers, Operation operation) {
            this.numbers = numbers;
            this.operation = operation;
        }

        public BigInteger solve() {
            return switch (operation) {
                case ADDITION -> numbers.stream().reduce(BigInteger.ZERO, BigInteger::add);
                case SUBTRACTION -> numbers.stream().skip(1)
                        .reduce(numbers.getFirst(), BigInteger::subtract);
                case MULTIPLICATION -> numbers.stream().reduce(BigInteger.ONE, BigInteger::multiply);
                case DIVISION -> numbers.stream().skip(1)
                        .reduce(numbers.getFirst(), BigInteger::divide);
            };
        }
    }

    private static Operation getOperation(Operation op, String lastLineVal) {
        op = switch (lastLineVal.charAt(0)) {
            case '+' -> Operation.ADDITION;
            case '-' -> Operation.SUBTRACTION;
            case '*' -> Operation.MULTIPLICATION;
            case '/' -> Operation.DIVISION;
            default -> op;
        };
        return op;
    }

    private static long solveAll(List<Problem> problems) {
        long sum = 0;
        for (Problem problem : problems) {
            long result = problem.solve();
            //results.add(result);
            sum += result;
        }
        return sum;
    }

    private static BigInteger solveAllV(List<ProblemBig> problems) {
        BigInteger sum = BigInteger.ZERO;
        for (ProblemBig problem : problems) {
            BigInteger result = problem.solve();
            //results.add(result);
            sum = sum.add(result);
        }
        return sum;
    }

    public static void main(String[] args) throws FileReaderException {
        TextFile problemsFile = loadFromRessources(MATH_PROBLEMS_FILE_NAME);

        Grid<String> grid = extractData(problemsFile);

        // Part 1
        System.out.println("===== Part 1 =====");
        List<Problem> problems = extractProblems(grid);

        List<Long> results = new ArrayList<>();
        long sum = solveAll(problems);
        System.out.println(results);
        System.out.println(sum);

        // Part 2
        System.out.println("===== Part 2 =====");
        List<ProblemBig> problemsV = extractProblemsV(grid);

        BigInteger sumV = new BigInteger(String.valueOf(solveAllV(problemsV)));
        System.out.println(results);
        System.out.println(sumV);
    }
}
