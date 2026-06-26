package org.htw.prog2.aufgabe1.analysis;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.exceptions.NoValidReadersException;
import org.htw.prog2.aufgabe1.files.MutationFile;
import org.htw.prog2.aufgabe1.files.SequenceFile;
import org.htw.prog2.aufgabe1.readers.*;

import java.io.IOException;


 // Verwaltet das Einlesen der Dateien und die Durchführung der Sequenzanalyse

public class SequenceAnalysisManager {


     //Liest alle drei Dateien ein und führt die Analyse durch.

    public static SequenceAnalysis performAnalysis(String referenceFileName,
                                                   String patientSequenceFileName,
                                                   String mutationFileName)
            throws NoValidReadersException, FileFormatException, IOException {

        ReaderManager<SequenceFileReader> sequenceReaderManager = new ReaderManager<>();
        sequenceReaderManager.addReader(new FASTAFileReader());
        sequenceReaderManager.addReader(new FASTQFileReader());

        ReaderManager<MutationFileReader> mutationReaderManager = new ReaderManager<>();
        mutationReaderManager.addReader(new CSVFileReader());

        SequenceFile referenceFile = sequenceReaderManager
                .getReaderForFile(referenceFileName).readFile(referenceFileName);
        SequenceFile patientSeqs = sequenceReaderManager
                .getReaderForFile(patientSequenceFileName).readFile(patientSequenceFileName);
        MutationFile patterns = mutationReaderManager
                .getReaderForFile(mutationFileName).readFile(mutationFileName);

        return new FullLengthSequenceAnalysis(referenceFile.getFirstSequence(), patientSeqs, patterns);
    }
}

//Diese Klasse braucht:

//ReaderManager –-> verwaltet die Reader-Listen
//FASTAFileReader, FASTQFileReader, CSVFileReader – die konkreten Reader
//SequenceFileReader, MutationFileReader –-> als Typen für den ReaderManager
//SequenceFile, MutationFile –-> Rückgabetypen der readFile()-Methoden
//FullLengthSequenceAnalysis –-> wird am Ende erstellt
//FileFormatException, NoValidReadersException, IOException –-> werden weitergereicht

//Diese Klasse wird gebraucht von:

//HIVDiagnosticsCLI –-> ruft performAnalysis() auf und gibt Ergebnis auf Konsole aus
//HIVDiagnosticsGUI –-> ruft performAnalysis() auf und zeigt Ergebnis im Fenster
//SequenceAnalysisManagerTest –-> testet direkt ob performAnalysis() korrekt funktioniert