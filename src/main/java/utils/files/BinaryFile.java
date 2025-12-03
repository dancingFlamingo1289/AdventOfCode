package utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Class representing a binary file.
 * Allows writing and reading a serializable Java object,
 * without enforcing a specific extension (e.g., .bin).
 *
 * @author Elias Kassas
 */
public class BinaryFile extends BaseFile {
	/** In-memory content loaded from the file. */
	private Object content;

	/** Extension used for this file. */
	private final String extension;

	/**
	 * Constructor for a binary file with a configurable extension.
	 *
	 * @param folder      Folder containing the file.
	 * @param fileName    File name without extension.
	 * @param extension   File extension (e.g.: ".dat", ".save", ".bkp").
     */
	public BinaryFile(File folder, String fileName, String extension)
			throws FileNotFoundException, ClassNotFoundException, IOException {
		super(folder, fileName, extension.startsWith(".") ? extension : "." + extension);
        this.extension = extension.startsWith(".") ? extension : "." + extension;

		if (file.exists()) {
			read();
		}
	}

	/**
	 * Constructor for a binary file without a folder.
	 *
	 * @param fileName   File name without extension.
	 * @param extension  File extension.
	 */
	public BinaryFile(String fileName, String extension)
			throws FileNotFoundException, ClassNotFoundException, IOException {

		super(fileName, extension.startsWith(".") ? extension : "." + extension);
        this.extension = extension.startsWith(".") ? extension : "." + extension;

		if (file.exists()) {
			read();
		}
	}

	@Override
	protected String getExtension() {
		return extension;
	}

	@Override
	public void write(Object... args) {
		if (args.length == 0 || !(args[0] instanceof Serializable serializable)) {
			System.err.println("Error: the provided object is not Serializable.");
			return;
		}

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(serializable);
			content = serializable;
		} catch (IOException e) {
			System.err.println("Error while writing to the file: " + e.getMessage());
		}
	}

	@Override
	public void read() throws FileNotFoundException, IOException, ClassNotFoundException {
		if (!file.exists()) return;

		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		content = in.readObject();
	}

	/**
	 * Returns the object loaded from the file.
	 *
	 * @return The object loaded, or null if empty.
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * Clears the content of the file and memory.
	 */
	public void clear() {
		content = null;

		try (FileOutputStream out = new FileOutputStream(file)) {
			// Overwrites with an empty file
			;
		} catch (IOException e) {
			System.err.println("Error while clearing the file: " + e.getMessage());
		}
	}

	/**
	 * Physically removes the file from the file system.
	 *
	 * @return true if deletion succeeded, false otherwise.
	 */
	public boolean deletePhysicalFile() {
		if (file.exists()) {
			boolean deleted = file.delete();
			if (deleted) {
				System.out.println("File " + file.getName() + " was physically deleted.");
				content = null;
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
        return super.toString() + "{ \n" + content +
                "\n}";
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args)
			throws FileNotFoundException, ClassNotFoundException, IOException {

		BinaryFile file = new BinaryFile(new File("Example"), "gameState", ".save");

		int[] array = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		file.write((Serializable) array);

		file.read();
		System.out.println(file);
	}
}
