package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.files.HIVFile;

import java.io.IOException;

//Basis-Interface für alle Reader, die eine HIV-bezogene Datei einlesen können.

public interface HIVFileReader {


    //Liest die übergebene Datei ein.
    HIVFile readFile(String filename) throws IOException, FileFormatException;


     //Prüft anhand eines ersten "Reinguckens", ob diese Datei von diesem Reader gelesen werden kann.
    boolean canReadFile(String filename);
}
