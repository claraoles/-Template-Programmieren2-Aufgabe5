package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.files.SequenceFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


 //Liest Sequenzdateien im FASTQ-Format ein. Qualitätsinformationen werden verworfen.

public class FASTQFileReader implements SequenceFileReader {

    @Override
    public SequenceFile readFile(String filename) throws IOException, FileFormatException {
        SequenceFile result = new SequenceFile();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        line = reader.readLine();
        if(line == null || line.charAt(0) != '@') {
            throw new FileFormatException("FASTQ File does not start with sequence header line.");
        }

        while(line != null) { // Zeile 1: Header prüfen
            if(line.charAt(0) != '@') {
                throw new FileFormatException("Expected a sequence header line starting with '@'.");
            }
            String sequence = reader.readLine(); // Zeile 2: Sequenz lesen
            if(sequence == null) {
                throw new FileFormatException("Missing sequence line.");
            }
            String separator = reader.readLine(); // Zeile 3: "+" lesen
            if(separator == null || separator.charAt(0) != '+') {
                throw new FileFormatException("Missing separator line starting with '+'.");
            }
            String quality = reader.readLine(); // Zeile 4: Qualität lesen
            if(quality == null) {
                throw new FileFormatException("Missing quality line.");
            }
            result.addSequence(sequence.strip());
            line = reader.readLine();
        }
        return result;
    }

    @Override
    public boolean canReadFile(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".fastq") || lower.endsWith(".fq");
    }
}

//Erste Zeile lesen. Beginnt sie nicht mit @ → Exception.
// Immer 4 Zeilen pro Sequenz: Header mit @, Sequenz, Trenner +, Qualitätsinformation.
//Der Hauptunterschied zu FASTA: FASTQ hat zusätzlich eine Qualitätszeile.
// für jede Aminosäure gibt es ein Zeichen das angibt wie sicher die Messung war.
