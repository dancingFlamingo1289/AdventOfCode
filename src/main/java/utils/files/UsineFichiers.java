package utils.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import utils.files.medias.Audio;
import utils.files.medias.Image;

public class UsineFichiers {
	public static BaseFile depuisChemin(String path) throws FileNotFoundException, ClassNotFoundException, IOException {
		File f = new File(path);

		if (!f.exists()) {
			throw new IllegalArgumentException("Le fichier n'existe pas : " + path);
		}

		if (f.isDirectory()) {
			throw new IllegalArgumentException("Ce chemin correspond à un dossier, pas à un fichier : " + path);
		}

		String name = f.getName().toLowerCase(Locale.ROOT);
		String ext = getExtension(f);
		
		// --- fichiers texte
		if (name.endsWith(".txt") || name.endsWith(".md") || name.endsWith(".log")) {
			return new TextFile(f.getParentFile(), f.getName().replace(ext, "")) ;
		}

		// --- fichiers tabulaires
		if (name.endsWith(".csv") || name.endsWith(".tsv")) {
			return new TabularFile(f.getParentFile(), f.getName(), ext.equals("tsv") ? '\t' : ',') ;
		}

		// --- fichiers images
		if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") ||
				name.endsWith(".gif") || name.endsWith(".bmp")) {
			return new Image(f.getParentFile(), f.getName(), ext);
		}

		// --- fichiers audio
		if (name.endsWith(".mp3") || name.endsWith(".wav")) {
			return new Audio(f.getParentFile(), f.getName());
		}

		// --- fichiers binaires
		if (name.endsWith(".exe") || name.endsWith(".bin") || name.endsWith(".dat")) {
			return new BinaryFile(f.getParentFile(), f.getName().replace(ext, ""), ext);
		}

		// --- si rien ne correspond
		throw new UnsupportedOperationException("Type de fichier non supporté : " + name);
	}
	
	private static String getExtension(File file) {
	    String name = file.getName();
	    int lastDot = name.lastIndexOf('.');
	    if (lastDot == -1 || lastDot == name.length() - 1) {
	        return ""; // pas d'extension
	    }
	    return name.substring(lastDot + 1).toLowerCase(Locale.ROOT);
	}

}
