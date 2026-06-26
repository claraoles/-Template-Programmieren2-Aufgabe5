package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.files.SequenceFile;

import java.io.IOException;

// Interface für Reader, die Sequenzdateien ( FASTA, FASTQ) einlesen können.

public interface SequenceFileReader extends HIVFileReader {

    @Override
    SequenceFile readFile(String filename) throws IOException, FileFormatException;
}
