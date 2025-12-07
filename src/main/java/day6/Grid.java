package day6;

import java.util.Arrays;

public class Grid<T> {
    private final T[] grid;
    private final int numCols; // number of columns
    private final int numRows; // number of rows

    @SuppressWarnings("unchecked")
    Grid(int cols, int rows) {
        numCols = cols;
        numRows = rows;
        grid = (T[]) new Object[numCols * numRows];
        for (int i = 0; i < numCols * numRows; i++) {
            grid[i] = null;
        }
    }

    Grid(T[][] array) {
        // array.length = rows
        // array[0].length = cols
        this(array[0].length, array.length);  // FIXED

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                grid[getIndex(row, col)] = array[row][col];
            }
        }
    }

    private Grid(Grid<T> grid) {
        this.numCols = grid.numCols;
        this.numRows = grid.numRows;
        this.grid = Arrays.copyOf(grid.grid, grid.grid.length);
    }

    public Grid<T> transpose() {
        // New grid : inverted dimensions
        Grid<T> transposed = new Grid<>(numRows, numCols);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                transposed.set(col, row, get(row, col));
            }
        }

        return transposed;
    }

    private int getIndex(int row, int col) {
        return row * numCols + col;
    }

    public T get(int row, int col) {
        return grid[getIndex(row, col)];
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }

    public void set(int row, int col, T value) {
        grid[getIndex(row, col)] = value;
    }

    @SuppressWarnings("unchecked")
    public T[] getRow(int row) {
        T[] rowArray;
        if (numCols == 0) {
            rowArray = (T[]) new Object[0];
        } else {
            // Crée un tableau du type du premier élément
            rowArray = (T[]) java.lang.reflect.Array.newInstance(grid[0].getClass(), numCols);
        }
        for (int col = 0; col < numCols; col++) {
            rowArray[col] = get(row, col);
        }
        return rowArray;
    }

    @SuppressWarnings("unchecked")
    public T[] getColumn(int col) {
        T[] colArray;
        if (numRows == 0) {
            colArray = (T[]) new Object[0];
        } else {
            colArray = (T[]) java.lang.reflect.Array.newInstance(grid[col].getClass(), numRows);
        }
        for (int row = 0; row < numRows; row++) {
            colArray[row] = get(row, col);
        }
        return colArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                sb.append(get(row, col));
                if (col < numCols - 1) sb.append(", ");
            }
            if (row < numRows - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
