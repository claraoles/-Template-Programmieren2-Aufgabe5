package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.NoValidReadersException;

import java.io.Reader;
import java.util.LinkedList;


 //Verwaltet eine Liste von Readern eines bestimmten Typs und wählt für eine gegebene Datei
 // den passenden Reader aus.
//DIese Klasse liest selbst nichts ein, sie delegiert nur.
public class ReaderManager<T extends HIVFileReader> {
     //Eine Liste von Readern. Durch T bestimmt welcher Reader, entweder SequenceFileReader oder MutationFileReader.
     // Die Liste ist am Anfang leer.
    private LinkedList<T> readers = new LinkedList<>();


    //Fügt der Liste der bekannten Reader einen neuen Reader hinzu.
    public void addReader(T reader) {
        readers.add(reader);
    }


    public T getReaderForFile(String filename) throws NoValidReadersException {
        for(T reader : readers) {
            if(reader.canReadFile(filename)) {
                return reader;
            }
        }
        throw new NoValidReadersException("No reader found that can read the file " + filename);
    }
}


//Diese Klasse braucht:

//HIVFileReader –-> als Typ-Einschränkung für T
//NoValidReadersException –-> wird geworfen wenn kein Reader passt
//LinkedList aus java.util

//Diese Klasse wird gebraucht von:

//SequenceAnalysisManager.performAnalysis() –-> erstellt zwei ReaderManager-Instanzen und benutzt addReader() und getReaderForFile()
//ReaderManagerTest –-> testet direkt ob der richtige Reader gefunden wird und ob die Exception korrekt geworfen wird

// Das Generics-Konzept <T extends HIVFileReader>:
// T ist ein Platzhalter. extends HIVFileReader bedeutet: T muss HIVFileReader implementieren.
// Dadurch weiß Java dass jedes T die Methode canReadFile() hat –-> sonst könnte man sie in getReaderForFile() nicht aufrufen.
// Ohne Generics bräuchte man zwei separate Klassen