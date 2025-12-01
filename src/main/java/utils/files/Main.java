package utils.files;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TabularFile f = new TabularFile("/Users/elias/Downloads/intra20251129-2.tsv", ';');
        System.out.println(f.toAlignedTableString());
        var contenu = f.getRow(1);
        
        double somme = 0;
        
        for (int i = 1 ; i < contenu.size() - 1 ; i++) {
        	double d = Double.parseDouble(contenu.get(i));
        	System.out.println(d);
        	somme += d;
        }
        
        f.setSeparator(';');
        f.write();
        System.out.println(f.toAlignedTableString());
        System.out.println("Somme pour vérification : " + somme);
    }
}