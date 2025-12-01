package utils.files;

import java.io.File;
import java.io.IOException;

/**
 * Abstract class representing a generic file.
 * This class provides the common behaviors for all file types
 * (name, path, renaming, existence check), without assuming anything
 * about the format or content of the file.
 * <p>
 * Subclasses must define how to read and write data, depending on the file type
 * (text, binary, JSON, etc.).
 * </p>
 *
 * @author Elias Kassas
 */
public abstract class BaseFile {
	/** Folder containing the file. */
	protected final File folder;

	/** Physical file on disk. */
	protected File file;

	/** File name (with extension). */
	protected String fileName;

	/**
	 * Constructor for a generic file.
	 *
	 * @param folder      Folder where the file is (or will be) created.
	 * @param fileName    File name without the extension.
	 * @param extension   File extension (e.g.: ".txt", ".csv").
	 */
	// Elias K.
	public BaseFile(File folder, String fileName, String extension) {
		this.fileName = fileName.replace(extension, "") + extension;
		this.folder = folder;
		//this.folder.mkdirs(); // Creates the folder if it does not exist
		if (this.folder.mkdirs()) {
			this.fileName = fileName;
		}
		this.file = new File(this.folder, this.fileName);
	}

	/**
	 * Constructor for a generic file without a folder.
	 *
	 * @param fileName   File name without the extension.
	 * @param extension  File extension (e.g.: ".txt", ".csv").
	 */
	public BaseFile(String fileName, String extension) {
		this.fileName = fileName.replace(extension, "") + extension;
		this.folder = null;
		this.file = new File(this.fileName);
	}

	// ========== COMMON METHODS ==========
	//@Override
	//public abstract <T> Iterator<T> iterator();

	/**
	 * Renames the physical file on disk.
	 *
	 * @param newName New name (without extension).
	 * @throws IllegalArgumentException if the rename operation fails.
	 */
	// Elias K.
	public void rename(String newName) throws IllegalArgumentException {
		String extension = getExtension();
		File newFile = new File(folder, newName.replace(extension, "") + extension);
		if (file.renameTo(newFile)) {
			file = newFile;
			fileName = newFile.getName();
		} else {
			throw new IllegalArgumentException("Unable to rename file: " + fileName);
		}
	}

	/**
	 * Checks whether the file exists on disk.
	 *
	 * @return true if the file exists, false otherwise.
	 */
	// Elias K.
	public boolean exists() {
		return file.exists();
	}

	/**
	 * Returns the file name (with extension).
	 *
	 * @return The file name.
	 */
	// Elias K.
	public String getName() {
		return fileName;
	}

	/**
	 * Returns the absolute path of the file on disk.
	 *
	 * @return Full file path.
	 */
	// Elias K.
	public String getFilePath() {
		return file.getAbsolutePath();
	}

	// Elias K.
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[name=" + fileName + ", path=" + getFilePath() + "]";
	}

	// ========== ABSTRACT METHODS ==========
	/**
	 * Reads the file content.
	 * The processing, format, and return type are determined by the subclass.
	 *
	 * @throws IOException If thrown by the subclass.
	 * @throws ClassNotFoundException If the class cannot be found.
	 */
	// Elias K.
	public abstract void read() throws IOException, ClassNotFoundException;

	/**
	 * Writes content to the file.
	 * The processing of the parameters is defined by the subclass.
	 *
	 * @param args Optional parameters used for writing.
	 * @throws Exception If something goes wrong during writing.
	 */
	// Elias K.
	public abstract void write(Object... args) throws Exception;

	/**
	 * Returns the extension used by this file type.
	 *
	 * @return The extension (e.g. ".txt", ".bin").
	 */
	// Elias K.
	protected abstract String getExtension();

	/**
	 * Physically deletes the file from the file system.
	 *
	 * @return true if the file was successfully deleted, false otherwise.
	 */
	// Elias K.
	public abstract boolean deletePhysicalFile();

	//public abstract boolean isEmpty();
}
