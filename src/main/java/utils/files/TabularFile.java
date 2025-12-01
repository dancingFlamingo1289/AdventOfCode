package utils.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Class representing a tabular file (CSV, TSV, SSV, etc.).
 * It allows reading and writing data as a list of string rows.
 * @author: Elias Kassas
 */
public class TabularFile extends BaseFile implements Iterable<List<String>> {
	/** In-memory content of the file as rows of fields. */
	private List<List<String>> content;

	/** Field separator used in the tabular file. */
	private char separator;

	/**
	 * Constructor for a generic tabular file (Separator-Separated Values).
	 * Automatically loads the file in memory if it exists.
	 *
	 * @param fileName  File name with extension.
	 * @param separator Field separator.
     */
	public TabularFile(String fileName, char separator) throws IOException {
		super(fileName, "");
		this.content = new ArrayList<>();
		this.separator = separator;

		if (file.exists()) {
			read();
		}
	}

	/**
	 * Constructor for a generic tabular file with directory.
	 * Automatically loads the file in memory if it exists.
	 *
	 * @param folder     Directory containing the file.
	 * @param fileName   File name with extension.
	 * @param separator  Field separator.
     */
	public TabularFile(File folder, String fileName, char separator) throws IOException {
		super(folder, fileName, "");
		this.content = new ArrayList<>();
		this.separator = separator;

		if (file.exists()) {
			read();
		}
	}

	/**
	 * Constructor for a CSV file.
	 * Automatically loads content if it exists.
	 *
	 * @param folder    Directory containing the file.
	 * @param fileName  File name without extension.
     */
	public TabularFile(File folder, String fileName) throws IOException {
		super(folder, fileName, ".csv");
		this.content = new LinkedList<>();
		this.separator = ',';

		if (file.exists()) {
			read();
		}
	}

	/**
	 * Constructor for a CSV file.
	 * Automatically loads content if it exists.
	 *
	 * @param fileName File name without extension.
     */
	public TabularFile(String fileName) throws IOException {
		super(fileName, ".csv");
		this.content = new ArrayList<>();
		this.separator = ',';

		if (file.exists()) {
			read();
		}
	}

	@Override
	protected String getExtension() {
		return this.fileName.substring(fileName.lastIndexOf('.'));
	}

	/**
	 * Returns the row at the given index.
	 */
	public List<String> getRow(int index) {
		return content.get(index);
	}

	/**
	 * Sets (replaces) a row and writes the updated content to disk.
	 */
	public List<String> setRow(int index, List<String> replacement) throws IOException {
		List<String> previous = content.set(index, replacement);
		write();
		return previous;
	}

	/**
	 * Loads tabular content from the file into memory.
     */
	@Override
	public void read() throws IOException {
		content.clear();

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;

		while ((line = reader.readLine()) != null) {
			content.add(parseCSVLine(line));
		}
	}

	/**
	 * Writes the tabular content into the file.
	 * If args[0] contains a row or list of rows, they are appended before writing.
	 *
	 * @param args Optional data to append.
	 */
	@Override
	public void write(Object... args) throws IOException {
		if (args.length > 0) {
			Object arg = args[0];

			if (arg instanceof List<?> row) {

				if (!row.isEmpty() && row.get(0) instanceof String) {
					content.add(convertRow(row));

				} else if (row.get(0) instanceof List<?>) {
					for (Object innerRow : row) {
						content.add(convertRow((List<?>) innerRow));
					}
				}

			} else if (arg instanceof String line) {
				content.add(parseCSVLine(line));
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (List<String> row : content) {
				writer.write(formatCSVLine(row));
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("CSV writing error: " + e.getMessage());
			throw e;
		}
	}

	/** Returns a deep copy of the content. */
	public List<List<String>> getContent() {
		return new ArrayList<>(content);
	}

	/** Returns content as raw joined strings. */
	public List<String> getContentString() {
		List<String> result = new ArrayList<>();

		for (List<String> row : content) {
			StringBuilder sb = new StringBuilder();
			for (String s : row) {
				sb.append(s).append(separator);
			}
			result.add(sb.toString());
		}

		return result;
	}

	/** Clears memory content and file content. */
	public void clear() throws IOException {
		content.clear();
		write();
	}

	// ---------------- Internal Utility Methods ---------------- //
	/** Formats a row into a CSV/TSV/etc. line with proper escaping. */
	private String formatCSVLine(List<String> row) {
		List<String> escaped = new ArrayList<>();
		for (String field : row) {
			escaped.add(escapeField(field));
		}
		return String.join(String.valueOf(separator), escaped);
	}

	/** Escapes CSV content according to RFC 4180. */
	private String escapeField(String field) {
		if (field.contains(String.valueOf(separator)) || field.contains("\"") || field.contains("\n")) {
			field = field.replace("\"", "\"\"");
			return "\"" + field + "\"";
		}
		return field;
	}

	/**
	 * Parses a CSV/SSV/TSV line.
	 * Does not handle extremely complex nested quotes (same as your original).
	 */
	private List<String> parseCSVLine(String line) {
		List<String> fields = new ArrayList<>();
		StringBuilder cur = new StringBuilder();
		boolean inQuotes = false;

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			if (inQuotes) {
				if (c == '"') {
					if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
						cur.append('"');
						i++;
					} else {
						inQuotes = false;
					}
				} else {
					cur.append(c);
				}
			} else {
				if (c == '"') {
					inQuotes = true;
				} else if (c == separator) {
					fields.add(cur.toString());
					cur.setLength(0);
				} else {
					cur.append(c);
				}
			}
		}

		fields.add(cur.toString().trim());

		// Trim trailing separators
		int end = line.length();
		while (end > 0 && Character.isWhitespace(line.charAt(end - 1))) end--;

		boolean endsWithSeparator = (end > 0) && (line.charAt(end - 1) == separator);

		if (endsWithSeparator) {
			while (!fields.isEmpty() && fields.get(fields.size() - 1).isEmpty()) {
				fields.remove(fields.size() - 1);
			}
		}

		return fields;
	}

	/** Safely casts a generic row into List<String>. */
	private List<String> convertRow(List<?> row) {
		List<String> result = new ArrayList<>();
		for (Object field : row) {
			result.add(field.toString());
		}
		return result;
	}

	@Override
	public Iterator<List<String>> iterator() {
		return content.iterator();
	}

	/**
	 * Physically removes the file from the file system.
	 *
	 * @return true if the deletion succeeded, false otherwise.
	 */
	public boolean deletePhysicalFile() {
		if (file.exists()) {
			boolean deleted = file.delete();
			if (deleted) {
				System.out.println("File " + file.getName() + " was physically deleted.");
				content.clear();
			} else {
				System.err.println("Failed to physically delete file: " + file.getName());
			}
			return deleted;
		} else {
			System.out.println("File does not exist: " + file.getName());
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString() + " {\n");

		for (List<?> row : content) {
			sb.append("\t").append(row).append("\n");
		}

		sb.append("}");
		return sb.toString();
	}

	/**
	 * Returns a visually aligned table view of the content.
	 */
	public String toAlignedTableString() {
		if (content.isEmpty()) {
			return "(empty)";
		}

		int maxColumns = content.stream().mapToInt(List::size).max().orElse(0);

		int[] widths = new int[maxColumns];

		for (List<String> row : content) {
			for (int i = 0; i < row.size(); i++) {
				widths[i] = Math.max(widths[i], row.get(i).length());
			}
		}

		StringBuilder sb = new StringBuilder();

		for (List<String> row : content) {
			for (int i = 0; i < maxColumns; i++) {
				String cell = (i < row.size()) ? row.get(i) : "";
				sb.append(String.format("%-" + (widths[i] + 2) + "s", cell));
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	/** Returns true if the file is empty. */
	public boolean isEmptyFile() throws IOException {
		write();
		return content.isEmpty();
	}

	public void setContent(List<List<String>> list) {
		this.content = list;
	}

	public void setSeparator(char newSeparator) {
		this.separator = newSeparator;
		JOptionPane.showMessageDialog(null,
				"New file content:\n" + this.toAlignedTableString());
	}

	public void setContentString(List<String> list) {
		// Intentionally left blank (matches original)
	}

	/** Removes the row at the given index. */
	public List<String> removeRow(int index) throws IOException {
		if (index < 0 || index > this.content.size()) {
			throw new IllegalArgumentException(
					"Index " + index + " must be between 0 and " + this.content.size() + ".");
		}

		read();

		List<String> removed = content.remove(index);
		write();

		return removed;
	}

	/** Test method. */
	public static void main(String[] args) throws IOException {

		TabularFile csv = new TabularFile(new File("Example"), "users");

		List<String> row1 = List.of("Name", "Age", "Country");
		List<String> row2 = List.of("Alice", "30", "France");
		List<String> row3 = List.of("Bob", "25", "Belgium");

		csv.write(List.of(row1, row2, row3));
		csv.read();

		System.out.println("- CSV File -");
		System.out.println(csv);

		TabularFile tsv = new TabularFile("users.tsv", '\t');

		tsv.write(List.of(row1, row2, row3));
		tsv.read();

		System.out.println("- TSV File -");
		for (List<String> row : tsv.getContent()) {
			System.out.println(row);
		}

		TabularFile psv = new TabularFile("users.psv", '|');

		psv.write(List.of(row1, row2, row3));
		psv.read();

		System.out.println("- PSV File -");
		for (List<String> row : psv.getContent()) {
			System.out.println(row);
		}
	}
}
