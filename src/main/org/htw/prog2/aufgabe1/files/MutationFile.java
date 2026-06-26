package org.htw.prog2.aufgabe1.files;

import java.util.LinkedList;


 // Hält die Daten einer eingelesenen Mutationsmuster-Datei (CSV).
 // speichert die Medikamentennamen und Mutationsmuster

public class MutationFile implements HIVFile {
    private LinkedList<String> drugs = new LinkedList<>();
    private LinkedList<Mutation> mutations = new LinkedList<>();

    public MutationFile() {
    }


     //Fügt der Liste der bekannten Medikamente einen neuen Namen hinzu.
    public void addDrug(String drug) {
        drugs.add(drug);
    }


     //Gibt die Liste der bekannten Medikamentennamen zurück.
    public LinkedList<String> getDrugs() {
        return drugs;
    }

    //Fügt der Liste der gespeicherten Mutationen eine neue Mutation hinzu.
    public void addMutation(Mutation variant) {
        mutations.add(variant);
    }

     //Gibt die gespeicherten Mutationen zurück.
    public LinkedList<Mutation> getMutations() {
        return mutations;
    }


     //Gibt die Anzahl der gespeicherten Mutationen zurück.
    public int getNumberOfMutations() {
        return mutations.size();
    }
}

//MutationFile ist der Behälter zwischen CSVFileReader und FullLengthSequenceAnalysis.
//Ohne diese Klasse hätte der Reader keine Möglichkeit die eingelesenen Daten zu übergeben,
//und die Analyse hätte keine Möglichkeit die Mutationen abzurufen.
//Sie ist der Datentransport zwischen Einlesen und Berechnen

//Diese Klasse braucht:

//HIVFile –-> wird implementiert (Marker-Interface)
//Mutation –-> die Liste hält Mutation-Objekte
//LinkedList aus java.util

//Diese Klasse wird gebraucht von:

//CSVFileReader.readFile() –-> erstellt ein MutationFile, befüllt es mit addDrug() und addMutation(), gibt es zurück
//SequenceAnalysisManager.performAnalysis() –-> empfängt das befüllte MutationFile als patterns
//FullLengthSequenceAnalysis –-> bekommt MutationFile im Konstruktor, ruft getMutations() auf
//SequenceAnalysis –-> bekommt MutationFile, ruft getDrugs() in getDrugDescriptions() auf
//CSVFileReaderTest –-> testet getNumberOfMutations() == 825