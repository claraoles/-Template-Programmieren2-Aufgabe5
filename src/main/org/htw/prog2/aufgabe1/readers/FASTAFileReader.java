package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.files.SequenceFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

 //Liest Sequenzdateien im FASTA-Format ein.

public class FASTAFileReader implements SequenceFileReader {

    @Override
    public SequenceFile readFile(String filename) throws IOException, FileFormatException {
        SequenceFile result = new SequenceFile();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        StringBuilder seq = new StringBuilder();

        line = reader.readLine();
        if(line == null || line.charAt(0) != '>') {
            throw new FileFormatException("FASTA File does not start with sequence header line.");
        }
        //Jede Zeile wird gelesen. Beginnt sie mit > ist es ein neuer Header,
        // die bisher gesammelte Sequenz wird gespeichert und StringBuilder wird geleert.
        // Sonst wird die Zeile an die aktuelle Sequenz angehängt.
        // StringBuilder wird benutzt weil eine Sequenz über mehrere Zeilen gehen kann.
        while((line = reader.readLine()) != null) {
            if(line.charAt(0) == '>') {
                if(seq.length() == 0) {
                    throw new FileFormatException("Two header lines are directly following each other.");
                }
                result.addSequence(seq.toString());
                seq = new StringBuilder();
            } else {
                seq.append(line.strip());
            }
        }
        //Die letzte Sequenz wird noch hinzugefügt,
        //die Schleife endet beim letzten Header ohne Sequenz danach → Exception
        if(seq.length() == 0) {
            throw new FileFormatException("The last line is a sequence header.");
        }
        result.addSequence(seq.toString());
        return result;
    }

    @Override
    public boolean canReadFile(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".fasta") || lower.endsWith(".fa");
    }
}

// Immer 2 Zeilen pro Sequenz: Header mit > und die Sequenz selbst.
//Erste Zeile lesen. Beginnt sie nicht mit > → sofort Exception.
// Das ist die Grundvoraussetzung für FASTA.

