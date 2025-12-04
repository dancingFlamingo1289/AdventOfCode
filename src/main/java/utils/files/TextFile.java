package utils.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Concrete class representing a text file (.txt).
 * It allows reading and writing strings line by line
 * while maintaining an in-memory representation. <br>
 * A text file is an Iterable structure.
 *
 * @author Elias Kassas
 */
public class TextFile extends BaseFile implements Iterable<String> {
	/** In-memory content of the file, each element representing one line. */
	private final LinkedList<String> content;

	/**
	 * Constructor for a text file. <br>
	 * The file is automatically read (loaded into memory) if it exists.
	 *
	 * @param folder     Folder containing the file.
	 * @param fileName   File name without extension.
	 * @throws IOException If the file cannot be read.
	 */
	public TextFile(File folder, String fileName) throws IOException {
		super(folder, fileName, ".txt");
		this.content = new LinkedList<>();

		if (file.exists()) {
			read();
		} else {
			write();
		}
	}

	/**
	 * Constructor for a text file. <br>
	 * The file is automatically read (loaded into memory) if it exists.
	 *
	 * @param fileName  File name without extension.
	 * @throws IOException If the file cannot be read.
	 */
	public TextFile(String fileName) throws IOException {
		super(fileName, ".txt");
		this.content = new LinkedList<>();

		if (file.exists()) {
			read();
		}
	}

	@Override
	protected String getExtension() {
		return ".txt";
	}

	/**
	 * Reads the text file and loads its lines into memory.
	 *
	 * @throws IOException If the file cannot be read.
	 */
	@Override
	public void read() throws IOException {
		if (!file.exists()) {
			throw new IOException("File does not exist and cannot be read: " + file.getAbsolutePath());
		}
		if (!file.canRead()) {
			throw new IOException("File cannot be read (permissions): " + file.getAbsolutePath());
		}

		content.clear();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();
			while (line != null) {
				this.content.add(line);
				line = reader.readLine();
			}
		}
	}

	/**
	 * Writes the in-memory content to the file, each element on a new line.
	 * If arguments are provided, args[0] is added to the in-memory content
	 * before writing.
	 *
	 * @param args Optional object or list of objects to append before writing.
	 */
	@Override
	public void write(Object... args) throws IOException {
		if (args.length > 0) {
			Object arg = args[0];

			if (arg instanceof List<?> list) {
				for (Object o : list) {
					content.add(o.toString());
				}
			} else {
				content.add(arg.toString());
			}
		}

		try {
			Files.write(file.toPath(), content);
		} catch (IOException e) {
			System.err.println("Write error: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Adds an object to the in-memory content without writing to disk.
	 *
	 * @param o Object to add.
	 */
	public void add(Object o) {
		String s = o.toString();
		content.add(s);
	}

	/**
	 * Removes an object from the in-memory content (not from disk).
	 *
	 * @param o Object to remove.
	 */
	public void remove(Object o) {
		content.removeIf(e -> e.equals(o.toString()));
	}

	/**
	 * Clears the content in memory and on disk.
	 */
	public void clear() throws IOException {
		content.clear();
		Files.write(file.toPath(), Collections.emptyList());
	}

	/**
	 * Returns a copy of the in-memory content.
	 *
	 * @return List of lines.
	 */
	public ArrayList<Object> getContent() throws IOException {
		read();
		ArrayList<Object> list = new ArrayList<>(content.size());
        list.addAll(content);
		return list;
	}

	/**
	 * Checks whether the in-memory content is empty.
	 *
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() throws IOException {
		read();
		return content.isEmpty();
	}

	/**
	 * Checks whether an element exists in the file (compared via toString()).
	 *
	 * @param o Object to search for.
	 * @return true if present.
	 */
	public boolean contains(Object o) throws IOException {
		write();
		for (Object obj : content) {
			if (obj.toString().equals(o.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Physically deletes the file from the file system.
	 *
	 * @return true if the file was successfully deleted, false otherwise.
	 */
	public boolean deletePhysicalFile() {
		if (!file.exists()) {
			System.out.println("File does not exist: " + file.getName());
			return false;
		}

		boolean deleted = file.delete();
		if (deleted) {
			System.out.println("File " + file.getName() + " was physically deleted.");
			content.clear();
		} else {
			System.err.println("Failed to physically delete the file: " + file.getName());
		}
		return deleted;
	}

	/**
	 * Returns an iterator for the file content.
	 * This allows: <br> {@code for (String line : myFile) {...}}
	 *
	 * @return Iterator over lines.
	 */
	@Override
	public Iterator<String> iterator() {
		ArrayList<String> copy = new ArrayList<>();

		for (Object obj : content) {
			copy.add(obj.toString());
		}

		return Collections.unmodifiableList(copy).iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString() + " {\n");

		for (String line : content) {
			sb.append("\t").append(line).append("\n");
		}

		sb.append("}");
		return sb.toString();
	}

	public String getLine(int line) {
		return content.get(line);
	}

	public boolean setLine(int line, String newLine) {
		content.set(line, newLine);
		return true;
	}

	public int getLineCount() {
		return content.size();
	}

	/**
	 * Main method for testing.
	 */
	public static void main(String[] args) throws IOException {

		File dir = new File("example");
		TextFile file = new TextFile(dir, "text");
		System.out.println("➡ Created text file: " + file.getFilePath());

		file.add("Hello world!");
		file.add("This is a text file.");
		file.add("Third line.");

		file.write();

		System.out.println("OK - Content written to disk.");

		int i;
		for (i = 0; i < 1e4; i++) {
			if (i % 1000 == 0) {
				System.out.println(i);
			}
			file.write("line " + i);
		}
		file.write();
		System.out.println("OK - Content written to disk. " + i + " lines.");

		file.read();

		System.out.println("File content after reading:");
		for (Object line : file.getContent()) {
			System.out.println("- " + line);
		}

		file.remove("Third line.");

		System.out.println("Line removed. New content:");
		for (Object line : file.getContent()) {
			System.out.println("- " + line);
		}

		System.out.println("File cleared. Is empty? " + file.isEmpty());
		file.deletePhysicalFile();
		dir.delete();

		System.out.println(new TextFile(new File("example"), "text"));
	}
}
