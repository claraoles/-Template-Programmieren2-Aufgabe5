package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.files.Mutation;
import org.htw.prog2.aufgabe1.files.MutationFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



// Liest die CSV-Datei mit den 825 Mutationsmustern
// baut daraus ein MutationFile Objekt

public class CSVFileReader implements MutationFileReader {

    @Override
    public MutationFile readFile(String filename) throws IOException, FileFormatException {
        MutationFile result = new MutationFile();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean firstLine = true;

        while((line = reader.readLine()) != null) {
            // Leerzeilen ignorieren
            if(line.strip().length() == 0) {
                continue;
            }
            // Kommentarzeilen ignorieren
            if(line.charAt(0) == '#') {
                continue;
            }
            if(line.startsWith("\"Mutation Patterns\"")) {
                for(String drug : parseDrugs(line)) {
                    result.addDrug(drug);
                }
                firstLine = false;
                continue;
            }
            // Falls die erste relevante Zeile keine Definitionszeile war
            if(firstLine) {
                throw new FileFormatException("The first line of the mutation patterns CSV file must be a definition line.");
            }
            String[] data = line.split(";");
            if(2 + result.getDrugs().size() != data.length) {
                throw new FileFormatException("Each line must have the same number of columns.");
            }

            String variant = data[0];
            HashMap<String, Double> resistances = new HashMap<>();
            for(int i = 0; i < result.getDrugs().size(); i++) {
                try {
                    resistances.put(result.getDrugs().get(i), Double.parseDouble(data[2 + i].strip()));
                } catch(NumberFormatException e) {
                    // Fehlende Messwerte werden als 0.0 gewertet (keine bekannte Resistenz)
                    resistances.put(result.getDrugs().get(i), 0.0);
                }
            }
            result.addMutation(new Mutation(variant, resistances));
        }
        return result;
    }

    @Override
    public boolean canReadFile(String filename) {
        return filename.toLowerCase().endsWith(".csv");
    }


    // Hilfsmethode, prüft ob die ersten zwei Spalten korrekt sind
    public static List<String> parseDrugs(String line) throws FileFormatException {
        LinkedList<String> res = new LinkedList<>();
        String[] data = line.split(";");
        if(!data[0].equals("\"Mutation Patterns\"") || !data[1].equals("\"Number of Sequences\"")) {
            throw new FileFormatException("First elements of definition line should be \"Mutation Patterns\" and " +
                    "\"Number of Sequences\"");
        }
        // Für jede Medikamentenspalte: prüfe ob sie mit  foldn" endet
        // Dann extrahiere den Namen, aus "NFV foldn" wird NFV
        // Das split(" ")[0] gibt "NFV und .split("\"")[1] gibt NFV ohne Anführungszeichen
        for(int i = 2; i < data.length; i++) {
            String drug = data[i];
            if(!drug.endsWith(" foldn\"")) {
                throw new FileFormatException("All drug definitions in first line must end with \"foldn\"\".");
            }
            res.add(drug.split(" ")[0].split("\"")[1]);
        }
        return res;
    }
}

//Diese Klasse braucht:

//MutationFileReader –-> wird implementiert
//MutationFile –-> wird erstellt und befüllt
//Mutation –-> wird für jede Datenzeile erstellt
//FileFormatException –-> wird bei falschem Format geworfen
//BufferedReader, FileReader –-> zum Einlesen
//HashMap, LinkedList, List –-> für Datenstrukturen

//Diese Klasse wird gebraucht von:

//SequenceAnalysisManager.performAnalysis() –-> fügt new CSVFileReader() dem mutationReaderManager hinzu
//CSVFileReaderTest –-> testet direkt readFile(), canReadFile() und parseDrugs()