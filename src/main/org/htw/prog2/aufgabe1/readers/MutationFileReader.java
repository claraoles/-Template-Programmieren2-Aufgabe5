package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.files.MutationFile;

import java.io.IOException;


 //Interface für Reader, die Mutationsmuster-Dateien (z.B. CSV) einlesen können.

public interface MutationFileReader extends HIVFileReader {

    @Override
    MutationFile readFile(String filename) throws IOException, FileFormatException;
}
