package org.htw.prog2.aufgabe1.files;

import java.util.HashSet;


 //Hält die Daten einer eingelesenen Sequenzdatei (FASTA oder FASTQ).
 // dient als Datenbehälter
//speichert alle eingelesenen DNA/Protein-Sequenzen und merkt sich welche erst drin war

public class SequenceFile implements HIVFile {
    private HashSet<String> seqs = new HashSet<>();
    private String firstSeq = "";

    public SequenceFile() {
    }


     //Fügt eine neue Sequenz der Sequenzliste hinzu und merkt sich die erste hinzugefügte
     //Sequenz, falls noch keine vorhanden war.

    public void addSequence(String sequence) {
        seqs.add(sequence);
        if(firstSeq.equals("")) {
            firstSeq = sequence;
        }
    }


     //Gibt die gespeicherten Sequenzen zurück.
    public HashSet<String> getSequences() {
        return seqs;
    }


     //Gibt die erste hinzugefügte Sequenz zurück.
    //Erste Sequenz, oder "" falls noch keine hinzugefügt wurde
    public String getFirstSequence() {
        return firstSeq;
    }


    //Gibt die Anzahl der gespeicherten Sequenzen zurück.
    public int getNumberOfSequences() {
        return seqs.size();
    }


     //Prüft, ob die übergebene Sequenz in der Sequenzliste enthalten ist.
    //FullLengthSequenceAnalysis fragt für jede der 825 mutierten Sequenzen: "Ist diese beim Patienten?"
     // HashSet.contains() beantwortet das in konstanter Zeit O(1)
    public boolean containsSequence(String sequence) {
        return seqs.contains(sequence);
    }
}

//SequenceFile wird zweimal gebraucht mit unterschiedlichen Inhalten:
//Einmal für die Referenzsequenz --> enthält genau 1 Sequenz (die normale HIV-Sequenz). Davon wird nur getFirstSequence() benutzt.
//Einmal für die Patientensequenzen –-> enthält 1000 Sequenzen. Davon wird hauptsächlich containsSequence() benutzt um zu prüfen ob eine Mutation beim Patienten vorkommt.
//Ohne diese Klasse können weder FASTA- noch FASTQ-Reader ihre eingelesenen Sequenzen zurückgeben, und die Analyse kann nicht stattfinden.

//Diese Klasse braucht:

//HIVFile – wird implementiert (Marker-Interface)
//HashSet aus java.util

//Diese Klasse wird gebraucht von:

//FASTAFileReader.readFile() – erstellt SequenceFile, ruft addSequence() auf, gibt es zurück
//FASTQFileReader.readFile() –-> macht dasselbe für FASTQ-Format
//SequenceAnalysisManager.performAnalysis() –-> empfängt zwei SequenceFile-Objekte (Referenz + Patient)
//SequenceAnalysis –-> bekommt SequenceFile sequences im Konstruktor
//FullLengthSequenceAnalysis.calculateResistances() –-> ruft containsSequence() auf
//SequenceAnalysisManager –-> ruft getFirstSequence() auf für die Referenz
//FASTAFileReaderTest, FASTQFileReaderTest – testen getNumberOfSequences(), getSequences(), getFirstSequence()
//SequenceFileTest –-> testet direkt containsSequence()