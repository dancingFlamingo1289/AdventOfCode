package day4;

import utils.fileReaders.FileReaderException;
import utils.fileReaders.TextFileReader;
import utils.files.TextFile;
import java.util.ArrayList;
import java.util.List;

public class Part2 {
    private static final String ROLLS_FILE_NAME = "paperRolls.txt";

    public static TextFile loadFromRessources(String fileName) throws FileReaderException {
        // Ajustez cette ligne si le chargement des ressources est différent dans votre environnement.
        return (TextFile) TextFileReader.getInstance().loadFromResources(fileName, Part2.class.getClassLoader());
    }

    // --- Logique du problème (Méthodes d'aide) ---

    /**
     * Identifie les rouleaux accessibles dans l'état actuel de la grille.
     * @param grid La grille de l'entrepôt.
     * @return Une liste de coordonnées [ligne, colonne] des rouleaux accessibles.
     */
    private static List<int[]> findAccessibleRolls(char[][] grid) {
        List<int[]> accessibleRolls = new ArrayList<>();
        int rows = grid.length;
        if (rows == 0) return accessibleRolls;
        int cols = grid[0].length;

        // Offsets pour les 8 voisins
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (grid[r][c] == '@') {
                    int neighborRolls = 0;

                    // Compter les rouleaux dans les 8 positions adjacentes
                    for (int i = 0; i < 8; i++) {
                        int nr = r + dr[i];
                        int nc = c + dc[i];

                        // Gérer les bords
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                            if (grid[nr][nc] == '@') {
                                neighborRolls++;
                            }
                        }
                    }

                    // Règle d'accessibilité : strictement moins de 4 voisins
                    if (neighborRolls < 4) {
                        accessibleRolls.add(new int[]{r, c});
                    }
                }
            }
        }
        return accessibleRolls;
    }

    /**
     * Simule le processus itératif de retrait des rouleaux.
     * @param initialGrid La grille de départ.
     * @return Le nombre total de rouleaux retirés.
     */
    public static int solvePartTwo(char[][] initialGrid) {
        // Créer une copie de la grille pour la modifier pendant la simulation
        char[][] grid = new char[initialGrid.length][];
        for (int i = 0; i < initialGrid.length; i++) {
            grid[i] = initialGrid[i].clone();
        }

        int totalRemoved = 0;
        int removedInIteration;

        do {
            // 1. Identifier tous les rouleaux accessibles dans l'état actuel
            List<int[]> accessible = findAccessibleRolls(grid);
            removedInIteration = accessible.size();

            // 2. Retirer ces rouleaux simultanément
            for (int[] coords : accessible) {
                int r = coords[0];
                int c = coords[1];
                // Retirer le rouleau (le remplacer par un espace vide, par ex. '.')
                grid[r][c] = '.';
            }

            // 3. Compter
            totalRemoved += removedInIteration;

            // 4. Arrêter si aucun rouleau n'a été retiré
        } while (removedInIteration > 0);

        return totalRemoved;
    }

    // --- Main Method ---
    public static void main(String[] args) throws FileReaderException {
        // Charger les données de la grille
        TextFile rollsFile = loadFromRessources(ROLLS_FILE_NAME);

        int lineCount = rollsFile.getLineCount();
        if (lineCount == 0) {
            System.out.println("Le fichier de rouleaux est vide.");
            return;
        }

        int colCount = rollsFile.getLine(0).length();
        char[][] initialGrid = new char[lineCount][colCount];

        for (int i = 0; i < lineCount; i++) {
            initialGrid[i] = rollsFile.getLine(i).toCharArray();
        }

        // Exécuter la simulation de la Partie Deux
        int totalRollsRemoved = solvePartTwo(initialGrid);

        System.out.println("Le nombre total de rouleaux de papier pouvant être retirés est : " + totalRollsRemoved);
    }
}